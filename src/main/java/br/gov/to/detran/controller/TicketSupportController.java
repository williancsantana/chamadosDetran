/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.jsoup.Jsoup;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.group.Group;

import br.gov.to.detran.domain.TicketAttachment;
import br.gov.to.detran.domain.TicketReply;
import br.gov.to.detran.domain.TicketReplyType;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.TicketServiceField;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.ViewServidorChamado;
import br.gov.to.detran.enumeration.DiaSemana;
import br.gov.to.detran.push.NotifyMessage;
import br.gov.to.detran.push.NotifySessions;
import br.gov.to.detran.repository.DetranERPRepository;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.TicketServiceRepository;
import br.gov.to.detran.repository.TicketSupportRepository;
import br.gov.to.detran.repository.UserSecurityRepository;
import br.gov.to.detran.util.FacesUtil;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class TicketSupportController extends BaseController<TicketSupport> implements java.io.Serializable {

    private @Inject
    TicketSupportRepository repository;
    private @Inject
    TicketServiceRepository ticketServiceRepository;
    private @Inject
    UserSecurityRepository userSecurityRepository;
    private @Inject
    NotifySessions notifySessions;
    private @Inject
    DetranERPRepository detranERPRepository;
    private String cpfServidor;
    private String categoriePath;
    private String oldCategoriePath;
    private List<TicketService> services;
    private TicketService service;
    private TicketReply reply;
    private ViewServidorChamado servidor = new ViewServidorChamado();

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        TicketSupport ticketSupport = this.getFlash("chamadoInstance");
        if (ticketSupport != null) {
            this.instance = ticketSupport;
        }
        TicketService serviceInstance = (TicketService) this.getFlashObject("chamadoServiceInstance");
        if (serviceInstance != null) {
            this.service = serviceInstance;
            this.service.mountFields();
        }
    }

    public void checkChamado() throws IOException {
        try {
            System.out.println("id = " + id);
            if (this.instance == null || this.instance.getId() == 0) {
                this.instance = this.repository.getTicketSupport(Long.parseLong(id));
                if (this.instance == null) {
                    this.redirectWithMensagem("Chamado não foi encontrado!", FacesUtil.ERROR);
                }
            }            
            this.repository.updateViewat(this.instance);
            this.instance.setRespostas(this.repository.getAllReplys(this.instance.getId()));
            this.instance.mountFields();
            this.reply = new TicketReply();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            this.redirectWithMensagem("Chamado não foi encontrado!", FacesUtil.ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.redirectWithMensagem("Chamado não foi encontrado!", FacesUtil.ERROR);
        }
    }
    
    public void checkChamadoUpdate() throws IOException {
        try {            
            this.instance = this.repository.getTicketSupport(Long.parseLong(id));
            this.repository.updateViewat(this.instance);
            this.instance.setRespostas(this.repository.getAllReplys(this.instance.getId()));
            this.instance.mountFields();
            this.reply = new TicketReply();
        }catch (Exception ex) {
            ex.printStackTrace();
            this.redirectWithMensagem("Chamado não foi encontrado!", FacesUtil.ERROR);
        }
    }

    public void viewChamado(TicketSupport chamado) {
        try {
            this.setFlash("chamadoInstance", chamado);
            this.redirect("/chamado.jsf?id=" + chamado.getId());
        } catch (IOException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Redirecionar", "Não foi possivel redirecionar para o chamado");
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectWithMensagem(String title, String msg, FacesMessage.Severity s) throws IOException {
        addMenssage(s, title, msg);
        redirect("/index.jsf");
    }

    public void redirectWithMensagem(String title, FacesMessage.Severity s) throws IOException {
        addMenssage(s, "Chamado", title);
        redirect("/index.jsf");
    }

    public void insert() {
        try {            
            this.criarChamado(instance, service);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() throws Exception {
        try {
            System.out.println(this.instance.getRespostas().size());
            this.repository.update(this.instance);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void removeAndRedirect(TicketSupport instance, String link) {
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(TicketSupportController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(TicketSupport instance) {
        try {
            this.repository.remove(instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void buscarServidor(){
        try{
            servidor = detranERPRepository.findServidor(this.instance.getRequisaoAcesso().getCpf());
            System.out.println(servidor.getNomeMae());
        }catch(Exception e){
            servidor = new ViewServidorChamado();
            e.printStackTrace();
        }
    }

    public void criarChamado(TicketSupport instance, TicketService service) throws Exception {
        instance.setCreated(new Date());
        instance.setUpdated(new Date());
        instance.setServico(service);
        instance.setSolicitante(FacesUtil.loggedUser());
        instance.setStatus(TicketSupportStatus.ABERTO);
        /* - Definindo o atendente do chamado de acordo com seu grupo e a quantidade de chamados
         * - Onde está solicitanteId era pra ser atendenteId
         */
        List<Long> solicitanteIds = this.userSecurityRepository.solicitanteIds(FacesUtil.loggedUser(), service, DiaSemana.SEG, new Date());
        
        System.out.println(Arrays.deepToString(solicitanteIds.toArray()));
        Collections.shuffle(solicitanteIds);
        Map<Integer, Group> contarChamados = this.repository.contarChamados(solicitanteIds);        
        Long id = Long.MAX_VALUE;
        Long count = Long.MAX_VALUE;
        for (Long atendente : solicitanteIds) {
            Long tempCount = 0L;
            if (contarChamados.containsKey(atendente.intValue())) {
                tempCount = (Long) contarChamados.get(atendente.intValue()).toArray()[0];                
            }
            if (tempCount < count) {
                id = atendente;
                count = tempCount;
            }
        }
        try {
            UserSecurity user = userSecurityRepository.getInstancePorId(id);
            this.instance.setAtendente(user);
        } catch (Exception e) {
            this.instance.setAtendente(null);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String sequence = String.valueOf(this.repository.getNextSequence());
        String theSequence = StringUtils.leftPad(sequence, 7 - sequence.length(), "0");
        instance.setNumero(format.format(new Date()) + theSequence);
        instance.setUltimaResposta(new Date());
        Type ticketServiceField = new TypeToken<ArrayList<TicketServiceField>>() {}.getType();
        Gson gson = new Gson();
        try {
            instance.setCamposJsonValues(gson.toJson(this.service.getFields(), ticketServiceField));
        } catch (Exception e) {
            instance.setCamposJsonValues("");
        }
        this.repository.insert(instance);
        
        if(this.instance.getAtendente() != null){
            notifySessions.sendMensagem(this.instance.getAtendente().getId().intValue(), NotifyMessage.builderNew(instance, this.instance.getAtendente()));
        }        
        this.redirectWithMensagem("Chamado", "Numero do Chamado: " + this.instance.getNumero(), FacesUtil.INFO);
    }

    public void responderChamado(String status) {
        try {
            if (Jsoup.parse(reply.getMensagem()).text().trim().isEmpty()) {
                throw new Exception("É necessário informar uma mensagem!");
            }
            UserSecurity user = FacesUtil.loggedUser();
            if (!Objects.equals(user.getId(), this.instance.getSolicitante().getId())
                    && !Objects.equals(user.getId(), this.instance.getAtendente().getId())) {
                throw new Exception("Você não tem permissão pra executar essa ação!");
            }
            HashMap<String, HashMap<String, Object>> transacoes = repository.getTransacoes();
            if (transacoes.containsKey(this.instance.getStatus().name())
                    && transacoes.get(this.instance.getStatus().name()).containsKey(status)) {
                TicketSupportStatus novoStatus = TicketSupportStatus.valueOf((String) transacoes.get(this.instance.getStatus().name()).get(status));
                if (this.instance.getStatus() == TicketSupportStatus.PENDENTE_TERCEIROS
                        && "RETIRAR PENDENCIA".equals(status)) {
                    if (this.instance.getReabertoEm() != null) {
                        novoStatus = TicketSupportStatus.REABERTO;
                    } else {
                        novoStatus = TicketSupportStatus.ABERTO;
                    }
                } else if (this.instance.getStatus() == TicketSupportStatus.PENDENTE_USUARIO
                        && "AUTO".equals(status)) {
                    if (this.instance.getReabertoEm() != null) {
                        novoStatus = TicketSupportStatus.REABERTO;
                    } else {
                        novoStatus = TicketSupportStatus.ABERTO;
                    }
                }

                if (novoStatus == TicketSupportStatus.REABERTO && this.instance.getStatus() == TicketSupportStatus.FECHADO) {
                    TicketReply lastReply = this.repository.lastClosing(this.instance.getId());
                    DateTime now = new DateTime();
                    DateTime closedDate = new DateTime(lastReply.getCreated());
                    if (Hours.hoursBetween(now, closedDate).getHours() > 24) {
                        throw new Exception("Não é possível reabrir um chamado fechado há mais de 24 horas");
                    }
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem(novoStatus.name());
                    this.instance.setReabertoEm(new Date());
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                }
                this.reply.setAutor(user);
                this.reply.setChamado(this.instance);
                this.reply.setTipo(TicketReplyType.REPLY);
                if (this.instance.getSolicitante().getId().equals(user.getId())) {
                    this.instance.setSolViewat(new Date());
                } else if (this.instance.getAtendente().getId().equals(user.getId())) {
                    this.instance.setAtViewat(new Date());
                }
                this.instance.setUltimaResposta(new Date());
                this.instance.getRespostas().add(reply);
                TicketSupportStatus ticketSupportStatus = this.instance.getStatus();
                if (ticketSupportStatus == TicketSupportStatus.PENDENTE_USUARIO
                        && novoStatus != TicketSupportStatus.PENDENTE_USUARIO
                        && novoStatus != TicketSupportStatus.FECHADO) {
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem("PENDENCIA USUARIO REMOVIDA");
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                } else if (ticketSupportStatus == TicketSupportStatus.PENDENTE_TERCEIROS
                        && novoStatus != TicketSupportStatus.PENDENTE_TERCEIROS) {
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem("PENDENCIA TERCEIROS RESOLVIDA");
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                } else if (novoStatus != TicketSupportStatus.REABERTO
                        && ticketSupportStatus != novoStatus) {
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem(novoStatus.name());
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                }
                this.update();                
                if (!Objects.equals(user.getId(), this.instance.getAtendente().getId())) {
                    notifySessions.sendMensagem(this.instance.getAtendente().getId().intValue(),
                            NotifyMessage.builderReply(instance, this.instance.getAtendente(), this.reply));
                }
                if (!Objects.equals(user.getId(), this.instance.getSolicitante().getId())) {
                    notifySessions.sendMensagem(this.instance.getSolicitante().getId().intValue(),
                            NotifyMessage.builderReply(instance, this.instance.getSolicitante(), this.reply));
                }
                this.reply = new TicketReply();
                addMenssage(FacesUtil.INFO, "Chamado", "Chamado respondido");
            } else {
                throw new Exception("Sua requisição foi inválida");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    }

    public void validar() {

    }

    @Override
    public Repository getRepository() {
        return this.repository;
    }

    public String getCategoriePath() {
        return categoriePath;
    }

    public void setCategoriePath(String categoriePath) {
        this.categoriePath = categoriePath;
    }

    public String getAllcategoriesJson() {
        return new Gson().toJson(ticketServiceRepository.getCategories(FacesUtil.loggedUser().getId()).toArray());
    }

    public List<TicketService> getServices() {
        return services;
    }

    public void setServices(List<TicketService> services) {
        this.services = services;
    }

    public void filter() {
        if (oldCategoriePath == null ? categoriePath != null : !oldCategoriePath.equals(categoriePath)) {
            oldCategoriePath = categoriePath;
            this.services = ticketServiceRepository.getServicesFilter(oldCategoriePath, FacesUtil.loggedUser().getId());
        }
    }
    
    public void enviarAnexo(FileUploadEvent event) {
    	try{
    		UploadedFile file = event.getFile();
            if(file != null){
            	TicketAttachment attach = new TicketAttachment();
            	attach.setChamado(this.instance);
            	attach.setCreated(new Date());
            	attach.setMimeType(file.getContentType());
            	attach.setName(file.getFileName());
            	attach.setSize(file.getSize());
            	attach.setDataByte(file.getContents());
            	this.instance.getAnexos().add(attach);
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    		addMenssage(FacesUtil.ERROR, "Anexo", ex.getMessage());
    	}            	   
    }
    
    public void removerAnexo(TicketAttachment attach){
    	if(this.instance.getAnexos() != null){
    		this.instance.getAnexos().remove(attach);
    	}
    }

    public TicketService getService() {
        return service;
    }

    public void setService(TicketService service) {
        this.service = service;
    }

    public TicketReply getReply() {
        return reply;
    }

    public void setReply(TicketReply reply) {
        this.reply = reply;
    }

    public String getCpfServidor() {
        return cpfServidor;
    }

    public void setCpfServidor(String cpfServidor) {
        this.cpfServidor = cpfServidor;
    }

    public ViewServidorChamado getServidor() {
        return servidor;
    }

    public void setServidor(ViewServidorChamado servidor) {
        this.servidor = servidor;
    }            
}
