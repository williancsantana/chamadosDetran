/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.component;

import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.joda.time.DateTime;

import com.querydsl.core.BooleanBuilder;

import br.gov.to.detran.dao.LazyResult;
import br.gov.to.detran.domain.QTicketStickerSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.view.QViewTicketSupport;
import br.gov.to.detran.domain.view.ViewTicketSupport;
import br.gov.to.detran.repository.TicketStickerRepository;
import br.gov.to.detran.repository.view.ViewTicketSupportRepository;
import br.gov.to.detran.util.FacesUtil;
import br.gov.to.detran.util.LDAP.User;

/**
 * Classes responsavel para realizar a filtragem e paginação das tabelas do
 * primefaces.
 *
 * @author Maycon
 *
 */
@SessionScoped
public class ViewTicketSupportTable implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private @Inject
    ViewTicketSupportRepository repository;   
	
    public enum TicketSupportTablePeriod {
        HOJE, U7DIAS, U30DIAS, TODOS;
    }

    public enum TicketSupportTableFilter {
        TODOS, ABERTOS, PENDENTES, FECHADOS, NAO_DEFINIDO, LEMBRETES;
    }

    public enum TicketSupportTableGrupo {
        MEUS, TODOS;
    }
    
    

    private int currentPage;
    private int pageSize = 10;
    private int rowCount;
    private String filterValue;
    private String[] filterColumns;
    private List<ViewTicketSupport> data;
    private TicketSupportTableFilter status = TicketSupportTableFilter.TODOS;
    private TicketSupportTablePeriod periodo = TicketSupportTablePeriod.TODOS;
    private TicketSupportTableGrupo grupos = TicketSupportTableGrupo.MEUS;
    private TicketSupportCount updateCount = new TicketSupportCount(0L, 0L, 0L, 0L, 0L,0L);

    public ViewTicketSupportTable() {
        this.currentPage = 0;
        this.pageSize = 5;
        this.filterColumns = new String[]{"numero", "assunto", "solicitante", "atendente"};
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
        this.currentPage = 0;
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
    	this.currentPage = 0;
        this.status = TicketSupportTableFilter.valueOf(status);
        this.update();
    }

    public List<ViewTicketSupport> load() {
    	BooleanBuilder booleanBuilder = new BooleanBuilder();               
        if (null != this.status) {
            switch (this.status) {
                case ABERTOS:
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.status
                            .eq(TicketSupportStatus.ABERTO).or(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.REABERTO)));
                    break;
                case FECHADOS:
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.FECHADO));
                    break;
                case PENDENTES:
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.PENDENTE_USUARIO).or(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.PENDENTE_TERCEIROS)));
                    break;
                case NAO_DEFINIDO:
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.idAtendente.isNull());
                    break;
                    
                case LEMBRETES:
                	UserSecurity user = FacesUtil.loggedUser();
                	booleanBuilder.andNot(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.FECHADO)).and(QViewTicketSupport.viewTicketSupport.idAtendente.eq(user.getId()));
                break;
                default:
            }
        }
        DateTime time = new DateTime();
        time = time.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        
        if (null != this.periodo) {
            switch (this.periodo) {
                case HOJE:                    
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.ultimaResposta.goe(time.toDate()));
                    break;                
                case U30DIAS:                    
                    time = time.minusMonths(1);
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.ultimaResposta.goe(time.toDate()));
                    break;
                case U7DIAS:
                    time = time.minusWeeks(1);
                    booleanBuilder.and(QViewTicketSupport.viewTicketSupport.ultimaResposta.goe(time.toDate()));
                    break;
                default:
            }
        }
        UserSecurity user = FacesUtil.loggedUser();
        
        if (null != this.grupos) {
            switch (this.grupos) {
                case MEUS:        
                	if(this.status == TicketSupportTableFilter.NAO_DEFINIDO){
                		booleanBuilder.and(QViewTicketSupport.viewTicketSupport.idSolicitante.eq(user.getId()).and(QViewTicketSupport.viewTicketSupport.idAtendente.isNull()));
                	}else{
                		booleanBuilder.and(QViewTicketSupport.viewTicketSupport.idSolicitante.eq(user.getId()).or(QViewTicketSupport.viewTicketSupport.idAtendente.eq(user.getId())));
                	}
                    break;                
                case TODOS:
                	List<Long> ids = repository.getUserServices();                          
                	if(this.status == TicketSupportTableFilter.NAO_DEFINIDO){
                		booleanBuilder.and(QViewTicketSupport.viewTicketSupport.idServico.in(ids).and(QViewTicketSupport.viewTicketSupport.idAtendente.isNull()));
                	}else{
                		booleanBuilder.and(QViewTicketSupport.viewTicketSupport.idServico.in(ids));
                	}                                 
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
        	booleanBuilder.andAnyOf(QViewTicketSupport.viewTicketSupport.assunto.containsIgnoreCase(filterValue),
            		QViewTicketSupport.viewTicketSupport.numero.containsIgnoreCase(filterValue),
            		QViewTicketSupport.viewTicketSupport.atendente.containsIgnoreCase(filterValue),
            		QViewTicketSupport.viewTicketSupport.solicitante.containsIgnoreCase(filterValue));
        }        
        
        LazyResult<ViewTicketSupport> lazyResult = this.repository.lazyLoad(this.currentPage * pageSize, pageSize, "ultimaResposta",
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
        this.updateCount.setNaoDefinido(this.repository.countNaoDefinido(onlineUser));
        this.updateCount.setComLembretes(this.repository.countLembretes(onlineUser));
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

    public List<ViewTicketSupport> getData() {
        if (this.data == null) {
            this.data = this.load();
            this.countUpdates();
        }
        return data;
    }

    public void setData(List<ViewTicketSupport> data) {
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
    	System.out.println("Quantidade Inicial: " + (currentPage * pageSize + 1));
        return (this.rowCount == 0 ? 0 : currentPage * pageSize + 1);
    }

    public Integer getLast() {
        Integer value = this.getFirst() + pageSize;
        System.out.println("Quantidade Final: " + (this.getFirst() + pageSize));
        if (this.rowCount < value) {
            return this.rowCount;
        }
        return value - 1;
    }
    //- #{ticketSupportTableController.supportTable.getLast()} de #{ticketSupportTableController.supportTable.rowCount}
    public String getPaginationInfor(){
    	String info =  getFirst() + " - " + getLast() + " de " + rowCount;
    	System.out.println(info);
    	return info;
    }

    public ViewTicketSupportRepository getRepository() {
        return repository;
    }

    public void setRepository(ViewTicketSupportRepository repository) {
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
                case NAO_DEFINIDO:
                    return "Chamados Aguardando Atendimento";
                default:
                    return "Todos os chamados";
            }
        }
        return "";
    }       

    public String icon(ViewTicketSupport suporte) {
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
