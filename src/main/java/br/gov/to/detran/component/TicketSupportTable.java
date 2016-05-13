/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.component;

import br.gov.to.detran.dao.LazyResult;
import br.gov.to.detran.domain.QTicketSupport;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.repository.TicketSupportRepository;
import br.gov.to.detran.util.FacesUtil;
import com.querydsl.core.BooleanBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.joda.time.DateTime;

/**
 * Classes responsavel para realizar a filtragem e paginação das tabelas do
 * primefaces.
 *
 * @author Maycon
 *
 */
@SessionScoped
public class TicketSupportTable implements java.io.Serializable {

    private @Inject
    TicketSupportRepository repository;

    public enum TicketSupportTablePeriod {
        HOJE, U7DIAS, U30DIAS, TODOS;
    }

    public enum TicketSupportTableFilter {
        TODOS, ABERTOS, PENDENTES, FECHADOS;
    }

    public enum TicketSupportTableGrupo {
        MEUS, TODOS;
    }

    private int currentPage;
    private int pageSize = 10;
    private int rowCount;
    private String filterValue;
    private String[] filterColumns;
    private List<TicketSupport> data;
    private TicketSupportTableFilter status = TicketSupportTableFilter.TODOS;
    private TicketSupportTablePeriod periodo = TicketSupportTablePeriod.HOJE;
    private TicketSupportTableGrupo grupos = TicketSupportTableGrupo.MEUS;
    private TicketSupportCount updateCount = new TicketSupportCount(0L, 0L, 0L, 0L);

    public TicketSupportTable() {
        this.currentPage = 0;
        this.pageSize = 5;
        this.filterColumns = new String[]{"numero", "assunto", "solicitante.name", "atendente.name"};
    }

    public void update() {
        this.data = this.load();
        this.countUpdates();
    }        

    public void nextPage() {
        if (isNextPage(this.currentPage + 1)) {
            this.currentPage = this.currentPage + 1;
            this.data = this.load();
        }
    }

    public void previousPage() {
        if (isPreviousPage(this.currentPage - 1)) {
            this.currentPage = this.currentPage - 1;
            this.data = this.load();
        }
    }

    public void actionFilter() {
        this.currentPage = 0;        
        this.update();
    }
    
    public void changeSize(Integer size){
        this.pageSize = size;
        this.update();
    }
    
    public void changeGroup(String group){
        this.grupos = TicketSupportTableGrupo.valueOf(group);
        this.update();
    }
    
    public void changeInterval(String interval){
        this.periodo = TicketSupportTablePeriod.valueOf(interval);
        this.update();
    }

    public void changeStatus(String status) {
        this.status = TicketSupportTableFilter.valueOf(status);
        this.update();
    }

    public List<TicketSupport> load() {
    	BooleanBuilder booleanBuilder = new BooleanBuilder();               
        if (null != this.status) {
            switch (this.status) {
                case ABERTOS:
                    booleanBuilder.and(QTicketSupport.ticketSupport.status
                            .eq(TicketSupportStatus.ABERTO).or(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.REABERTO)));
                    break;
                case FECHADOS:
                    booleanBuilder.and(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.FECHADO));
                    break;
                case PENDENTES:
                    booleanBuilder.and(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.PENDENTE_USUARIO).or(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.PENDENTE_TERCEIROS)));
                    break;
                default:
            }
        }
        DateTime time = new DateTime();
        time = time.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        
        if (null != this.periodo) {
            switch (this.periodo) {
                case HOJE:                    
                    booleanBuilder.and(QTicketSupport.ticketSupport.ultimaResposta.goe(time.toDate()));
                    break;                
                case U30DIAS:                    
                    time = time.minusMonths(1);
                    booleanBuilder.and(QTicketSupport.ticketSupport.ultimaResposta.goe(time.toDate()));
                    break;
                case U7DIAS:
                    time = time.minusWeeks(1);
                    booleanBuilder.and(QTicketSupport.ticketSupport.ultimaResposta.goe(time.toDate()));
                    break;
                default:
            }
        }
        UserSecurity user = FacesUtil.loggedUser();
        if (null != this.grupos) {
            switch (this.grupos) {
                case MEUS:
                    booleanBuilder.and(QTicketSupport.ticketSupport.solicitante.id.eq(user.getId()).or(QTicketSupport.ticketSupport.atendente.id.eq(user.getId())));
                    break;                
                case TODOS:                                        
                    List<Long> ids = repository.getUserServices();
                    System.out.println(ids);                    
                    booleanBuilder.and(QTicketSupport.ticketSupport.servico.id.in(ids));
                    break;             
            }
        }
        
        /*HashMap<String, Object> filterHash = new HashMap<>();
        if (this.filterValue != null && !this.filterValue.isEmpty()) {
            for (String filterColumn : filterColumns) {
                filterHash.put(filterColumn, filterValue);
            }
        }*/
        
        HashMap<String, Object> filterHash = null;
        if (this.filterValue != null && !this.filterValue.isEmpty()) {
        	booleanBuilder.andAnyOf(QTicketSupport.ticketSupport.assunto.containsIgnoreCase(filterValue),
            		QTicketSupport.ticketSupport.numero.containsIgnoreCase(filterValue),
            		QTicketSupport.ticketSupport.atendente.name.containsIgnoreCase(filterValue));
        }        
        
        LazyResult<TicketSupport> lazyResult = this.repository.lazyLoad(this.currentPage * pageSize, pageSize, "ultimaResposta",
                "desc", filterHash, booleanBuilder, true);
        
        this.setRowCount(lazyResult.getCount().intValue());
        return lazyResult.getResult();
    }

    public void countUpdates() {
        UserSecurity onlineUser = FacesUtil.loggedUser();
        this.updateCount.setTodos(this.repository.countTodos(onlineUser));
        this.updateCount.setAbertos(this.repository.countAbertos(onlineUser));
        this.updateCount.setFechados(this.repository.countFechados(onlineUser));
        this.updateCount.setPendentes(this.repository.countPendentes(onlineUser));
    }

    public boolean isNextPage(Integer next) {
        return next > 0 && next * pageSize <= this.getRowCount();
    }

    public boolean isPreviousPage(Integer previous) {
        return previous >= 0 && previous * pageSize >= 0;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<TicketSupport> getData() {
        if (this.data == null) {
            this.data = this.load();
            this.countUpdates();
        }
        return data;
    }

    public void setData(List<TicketSupport> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String[] getFilterColumns() {
        return filterColumns;
    }

    public void setFilterColumns(String[] filterColumns) {
        this.filterColumns = filterColumns;
    }

    public Integer getFirst() {
        return (this.rowCount == 0 ? 0 : currentPage * pageSize + 1);
    }

    public Integer getLast() {
        Integer value = this.getFirst() + pageSize;
        if (this.rowCount < value) {
            return this.rowCount;
        }
        return value - 1;
    }

    public TicketSupportRepository getRepository() {
        return repository;
    }

    public void setRepository(TicketSupportRepository repository) {
        this.repository = repository;
    }

    public TicketSupportTableFilter getStatus() {
        return status;
    }

    public void setStatus(TicketSupportTableFilter status) {
        this.status = status;
    }

    public TicketSupportTablePeriod getPeriodo() {
        return periodo;
    }

    public void setPeriodo(TicketSupportTablePeriod periodo) {
        this.periodo = periodo;
    }

    public TicketSupportTableGrupo getGrupos() {
        return grupos;
    }

    public void setGrupos(TicketSupportTableGrupo grupos) {
        this.grupos = grupos;
    }

    public boolean isStatus(String status) {
        TicketSupportTableFilter statusTemp = TicketSupportTableFilter.valueOf(status);
        return this.status == statusTemp;
    }

    public String getGrupoFilterActive(String status) {
        TicketSupportTableGrupo statusTemp = TicketSupportTableGrupo.valueOf(status);
        if (this.grupos == statusTemp) {
            return "selected active";
        }
        return "";
    }
    
    public String getSizeFilterActive(String valor) {
        Integer size = Integer.parseInt(valor);
        if (this.pageSize == size) {
            return "selected active";
        }
        return "";
    }

    public String getTimeIntervalFilterActive(String status) {
        TicketSupportTablePeriod statusTemp = TicketSupportTablePeriod.valueOf(status);
        if (this.periodo == statusTemp) {
            return "selected active";
        }
        return "";
    }

    public String activeTitle() {
        if (null != this.status) {
            switch (this.status) {
                case ABERTOS:
                    return "Chamados abertos";
                case FECHADOS:
                    return "Chamados fechados";
                case PENDENTES:
                    return "Chamados pendentes";
                default:
                    return "Todos os chamados";
            }
        }
        return "";
    }

    public String icon(TicketSupport suporte) {
        if (null != suporte.getStatus()) {
            switch (suporte.getStatus()) {
                case ABERTO:
                    return "green comments outline";
                case REABERTO:
                    return "green refresh";
                case FECHADO:
                    return "red ban";
                case PENDENTE_TERCEIROS:
                case PENDENTE_USUARIO:
                    return "orange warning sign";
            }
        }
        return "";
    }

    public String groupsFilterIcon() {
        if (null != this.grupos) {
            switch (grupos) {
                case MEUS:
                    return "user";
                case TODOS:
                    return "users";
                default:
                    return "help";
            }
        }
        return "help";
    }

    public String timeIntervalFilterIcon() {
        if (null != this.periodo) {
            switch (periodo) {
                case HOJE:
                    return "calendar outline";
                case TODOS:
                    return "asterisk";
                case U30DIAS:
                    return "calendar";
                case U7DIAS:
                    return "calendar";
                default:
                    return "help";
            }
        }
        return "help";
    }

    public TicketSupportCount getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(TicketSupportCount updateCount) {
        this.updateCount = updateCount;
    }

}
