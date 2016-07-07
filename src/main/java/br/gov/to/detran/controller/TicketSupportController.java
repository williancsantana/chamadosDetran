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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import org.primefaces.push.EventBus.Reply;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.gov.to.detran.domain.TicketAttachment;
import br.gov.to.detran.domain.TicketAttachmentReply;
import br.gov.to.detran.domain.TicketGroup;
import br.gov.to.detran.domain.TicketGroupService;
import br.gov.to.detran.domain.TicketReply;
import br.gov.to.detran.domain.TicketReplyType;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.TicketServiceField;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.ViewServidorChamado;
import br.gov.to.detran.domain.ViewDadosServidor;
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
    private String categorieVoltar;
    private String categoriePath;
    private String oldCategoriePath;
    private List<TicketService> services;
    private TicketService service;
    private TicketReply reply;
    private ViewServidorChamado servidor = new ViewServidorChamado();
    private List<UserSecurity> possibleAttendants;
    private TicketGroup tempGroup = null;
    private List<UserSecurity> atendentesGrupo;
    private UserSecurity tempAtendente;
    private String respostaModal = "";
    private String cpfConsulta = "";
    private Boolean resultadoConsultaCpf;
    ViewServidorChamado servidorBuscaCpf = new ViewServidorChamado();
    ViewDadosServidor dadosServidor = new ViewDadosServidor();

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
        }catch(java.io.IOException ex){
        	ex.printStackTrace();            
        } catch (Exception ex) {
            ex.printStackTrace();
            this.addMenssage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Validação");
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
        }catch(Exception e){
            servidor = new ViewServidorChamado();
            e.printStackTrace();
        }
    }

    public void criarChamado(TicketSupport instance, TicketService service) throws Exception {
    	
    	if(instance.getDescricao().trim().isEmpty() || instance.getDescricao().trim().length() < 30){
    		throw new Exception("É necessário informar uma descrição do chamado de no minimo 30 caracteres!");
    	}
    	if(instance.getAssunto().trim().isEmpty()){
    		throw new Exception("É obrigatório informar o assunto");
    	}
        instance.setCreated(new Date());
        instance.setUpdated(new Date());
        instance.setServico(service);
        instance.setSolicitante(FacesUtil.loggedUser());
        instance.setStatus(TicketSupportStatus.ABERTO);
        /* - Definindo o atendente do chamado de acordo com seu grupo e a quantidade de chamados
         * - Onde está solicitanteId era pra ser atendenteId
         */
        atribuiChamadoAAtendenteDisponivel(FacesUtil.loggedUser(), service);
        
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
                    notification.setCreated(new Date());
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
                this.reply.setCreated(new Date());
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
                    notification.setCreated(new Date());
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                } else if (ticketSupportStatus == TicketSupportStatus.PENDENTE_TERCEIROS
                        && novoStatus != TicketSupportStatus.PENDENTE_TERCEIROS
                        && novoStatus != TicketSupportStatus.FECHADO
                        && status.equals("RETIRAR_PENDENCIA_TERCEIROS_SOLICITANTE")) {
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem("PENDENCIA TERCEIROS RESOLVIDA PENDENTE USUARIO");
                    notification.setCreated(new Date());
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                } else if (ticketSupportStatus == TicketSupportStatus.PENDENTE_TERCEIROS
                        && novoStatus != TicketSupportStatus.PENDENTE_TERCEIROS) {
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem("PENDENCIA TERCEIROS RESOLVIDA");
                    notification.setCreated(new Date());
                    this.instance.setStatus(novoStatus);
                    this.instance.getRespostas().add(notification);
                } else if (novoStatus != TicketSupportStatus.REABERTO
                        && ticketSupportStatus != novoStatus) {
                    TicketReply notification = new TicketReply();
                    notification.setAutor(user);
                    notification.setChamado(this.instance);
                    notification.setTipo(TicketReplyType.NOTIFICATION);
                    notification.setMensagem(novoStatus.name());
                    notification.setCreated(new Date());
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
        }catch(java.io.IOException ex){
        	ex.printStackTrace();            
        }  catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    }
    
    public void apropiarChamado() {
        try {               
        	 UserSecurity user = FacesUtil.loggedUser();
            if(!this.permissaoApropirar()){
            	throw new Exception("Você não tem permissão pra executar essa ação!");
            }
            
            TicketReply notification = new TicketReply();
            notification.setAutor(user);
            notification.setChamado(this.instance);
            notification.setTipo(TicketReplyType.NOTIFICATION);
            notification.setMensagem("APROPRIACAO");
            notification.setCreated(new Date());
                                    
            this.instance.getRespostas().add(notification);
            this.instance.setAtendente(user);
            this.instance.setAtViewat(new Date());
            this.instance.setUltimaResposta(new Date());
            
            this.update();                
           /* if (!Objects.equals(user.getId(), this.instance.getAtendente().getId())) {
                notifySessions.sendMensagem(this.instance.getAtendente().getId().intValue(),
                        NotifyMessage.builderReply(instance, this.instance.getAtendente(), notification));
            }*/
            if (!Objects.equals(user.getId(), this.instance.getSolicitante().getId())) {
                notifySessions.sendMensagem(this.instance.getSolicitante().getId().intValue(),
                        NotifyMessage.builderReply(instance, this.instance.getSolicitante(), notification));
            }
        }catch(java.io.IOException ex){
        	ex.printStackTrace();                       
        } catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    }
    
    public void escalonarChamado(UserSecurity user) {
        try {                       	                        
            TicketReply notification = new TicketReply();
            notification.setAutor(user);
            notification.setChamado(this.instance);
            notification.setTipo(TicketReplyType.NOTIFICATION);
            notification.setMensagem("ESCALONAMENTO");
            notification.setCreated(new Date());
                                    
            this.instance.getRespostas().add(notification);
            this.instance.setAtendente(user);
            this.instance.setAtViewat(new Date());
            this.instance.setUltimaResposta(new Date());
            
            UserSecurity userSecurity = FacesUtil.loggedUser();
            
            this.update();
            if (!Objects.equals(userSecurity.getId(), user.getId())) {
                notifySessions.sendMensagem(user.getId().intValue(),
                        NotifyMessage.builderNew(instance, user));
            }
            if (!Objects.equals(userSecurity.getId(), this.instance.getSolicitante().getId())) {
                notifySessions.sendMensagem(this.instance.getSolicitante().getId().intValue(),
                        NotifyMessage.builderNew(instance, this.instance.getSolicitante()));
            }
                                    
        }catch(java.io.IOException ex){
        	ex.printStackTrace();            
        } catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    }
    
    public void escalonarAtendenteGrupo(UserSecurity user){
    	try{
    		
    		TicketReply notification = new TicketReply();
    		UserSecurity autor = FacesUtil.loggedUser();
    		TicketReply mensagem = new TicketReply();
    		
    		if(user == null){//Caso não tenha sido escolhido um atendente para o chamado, o escalonamento é automático
            	System.out.println("Nenhum atendente selecionado");
            	atribuiChamadoAAtendenteDisponivel(autor,this.instance.getServico());            	
            }
            else{//inserir os passos para escalonamento
            	System.out.println("Atendente escolhido: "+user.getName());
            	
	            this.instance.setAtendente(user);
	        }
    		
    		System.out.println("Mensagem: "+respostaModal);
        	System.out.println("Usuário logado: "+autor.getName());
        	mensagem.setTipo(TicketReplyType.REPLY);
        	mensagem.setAutor(autor);
        	mensagem.setChamado(this.instance);
        	
        	mensagem.setCreated(new Date());
        	
        	notification.setAutor(autor);
        	notification.setChamado(this.instance);
            notification.setTipo(TicketReplyType.NOTIFICATION);
            notification.setMensagem("ESCALONAMENTO");
            notification.setChamado(this.instance);
            notification.setCreated(new Date());
            this.instance.getRespostas().add(notification);
            this.instance.getRespostas().add(mensagem);
            
            mensagem.setMensagem("Escalonado para: "+this.instance.getAtendente().getName()+". <br/> Motivo do escalonamento: "+respostaModal);
            
            this.instance.setAtViewat(new Date());
            this.instance.setUltimaResposta(new Date());
            
            this.update();
            if (!Objects.equals(autor.getId(), this.instance.getAtendente().getId())) {
            	
                notifySessions.sendMensagem(this.instance.getAtendente().getId().intValue(),
                        NotifyMessage.builderNew(instance, this.instance.getAtendente()));
                System.out.println("O autor é diferente do atendente");
            }
            if (!Objects.equals(autor.getId(), this.instance.getSolicitante().getId())) {
                notifySessions.sendMensagem(this.instance.getSolicitante().getId().intValue(),
                        NotifyMessage.builderNew(instance, this.instance.getSolicitante()));
            }
            
    		
    	}
    	catch(java.io.IOException ex){
        	ex.printStackTrace();            
        } catch (Exception ex) {
            ex.printStackTrace();
            addMenssage(FacesUtil.ERROR, "Validação", ex.getMessage());
        }
    	user = null;
        tempGroup = null;
        tempAtendente = null;
        respostaModal = "";
    	
    }
    
    public boolean permissaoEscalonar() throws Exception{    	
        UserSecurity user = FacesUtil.loggedUser();
        if(instance.getAtendente() != null && Objects.equals(user.getId(), this.instance.getSolicitante().getId())){
        	return false;
        }
        boolean permissao = false;
        user = userSecurityRepository.getInstancePorId(FacesUtil.loggedUser().getId());
        for(TicketGroupService service : this.instance.getServico().getAtendentes()){        	
        	TicketGroup g = service.getGrupo();
        	if(user.getAllGrupos().contains(g)){
        		permissao = true;
        		break;
        	}
        }
        return permissao;                    
    }
    
    public boolean permissaoApropirar() throws Exception{    	
        UserSecurity user = FacesUtil.loggedUser();
        if(instance.getAtendente() != null){
        	if (Objects.equals(user.getId(), this.instance.getAtendente().getId())
                    || Objects.equals(user.getId(), this.instance.getSolicitante().getId())) {
                return false;
            }
        }
        else if(Objects.equals(user.getId(), this.instance.getSolicitante().getId())){
        	return false;
        }
        boolean permissao = false;
        user = userSecurityRepository.getInstancePorId(FacesUtil.loggedUser().getId());
        for(TicketGroupService service : this.instance.getServico().getAtendentes()){        	
        	TicketGroup g = service.getGrupo();
        	if(user.getAllGrupos().contains(g)){
        		permissao = true;
        		break;
        	}
        }
        return permissao;                    
    }
    
	public Boolean consultarDadosSolicitante(String cpf){
    	
    	Boolean resultado = false;
    	System.out.println(cpf);
    	try{
    		servidorBuscaCpf = detranERPRepository.findServidor(cpf);
    		String cpfFormatado = cpf.substring(0,3) + "." + cpf.substring(3,6)+ "." + cpf.substring(6,9)+ "-" + cpf.substring(9);
    		dadosServidor = detranERPRepository.findDadosServidor(cpfFormatado);
    		//System.out.println("Servidor: "+servidorBuscaCpf.getNome()+"\nCPF: "+servidorBuscaCpf.getCpf()+"\nSetor: "+viewDadosServidor.getNomesetor());
    		resultadoConsultaCpf =  resultado = true;
    	}catch(NullPointerException ex){
    		resultadoConsultaCpf = false;
    		ex.printStackTrace();
    	}finally{
    		System.out.println(servidorBuscaCpf.getNome());
    		//System.out.println(dadosServidor);
    		System.out.println(cpf.substring(0,3) + "." + cpf.substring(3,6)+ "." + cpf.substring(6,9)+ "-" + cpf.substring(9));
    	}
    	
    	return resultado;
    	
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
    
    public void filterVoltar() {    		
    		String[] back = categorieVoltar.split(":");
    		Integer index = back.length - 2;
    		String voltar = "";
    		for(int i = 0; i<= index; i++){
    			voltar += back[i];
    			if(i + 1 <= index){
    				voltar += ":";
    			}
    		}
    		oldCategoriePath = categorieVoltar;
    		categorieVoltar = voltar;    		
            this.services = ticketServiceRepository.getServicesFilter(oldCategoriePath, FacesUtil.loggedUser().getId());
        
    }

    public void filter() {    	
        if (oldCategoriePath == null ? categoriePath != null : !oldCategoriePath.equals(categoriePath)) {
        	categorieVoltar = oldCategoriePath;
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
    
    public void enviarAnexoResposta(FileUploadEvent event) {
    	try{
    		UploadedFile file = event.getFile();
            if(file != null){
            	TicketAttachmentReply attachReply = new TicketAttachmentReply();
            	attachReply.setChamadoResposta(reply);
            	attachReply.setCreated(new Date());
            	attachReply.setMimeType(file.getContentType());
            	attachReply.setName(file.getFileName());
            	attachReply.setSize(file.getSize());
            	attachReply.setDataByte(file.getContents());
            	reply.getAnexoRespostas().add(attachReply);
            }
    	}catch(Exception ex){
    		ex.printStackTrace();
    		addMenssage(FacesUtil.ERROR, "Anexo", ex.getMessage());
    	}            	   
    }
    
    public void removerAnexoResposta(TicketAttachmentReply attachReply){
    	if(reply.getAnexoRespostas()!=null){
    		reply.getAnexoRespostas().remove(attachReply);
    	}
    }
    
    
    public void removerAnexo(TicketAttachment attach){
    	if(this.instance.getAnexos() != null){
    		this.instance.getAnexos().remove(attach);
    	}
    }
    
    public void groupListener(){
    	//System.out.println("Item selecionado: " + tempGroup.getDescricao());
    	
    	String grupoAtendentes = tempGroup.getDescricao();
    	atendentesGrupo = userSecurityRepository.getAtendentesGrupo(tempGroup);
    	
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

	public String getCategorieVoltar() {
		return categorieVoltar;
	}

	public void setCategorieVoltar(String categorieVoltar) {
		this.categorieVoltar = categorieVoltar;
	}

	public String getOldCategoriePath() {
		return oldCategoriePath;
	}

	public void setOldCategoriePath(String oldCategoriePath) {
		this.oldCategoriePath = oldCategoriePath;
	}            
	
	public List<UserSecurity> possibleAttendants(){
		if(possibleAttendants == null){
			possibleAttendants = ticketServiceRepository.getPossibleAttendants(this.instance.getServico());
			possibleAttendants.remove(instance.getAtendente());
		}
		return possibleAttendants;
	}

	public TicketGroup getTempGroup() {
		return tempGroup;
	}

	public void setTempGroup(TicketGroup tempGroup) {
		this.tempGroup = tempGroup;
	}

	public List<UserSecurity> getAtendentesGrupo() {
		return atendentesGrupo;
	}

	public void setAtendentesGrupo(List<UserSecurity> atendentesGrupo) {
		this.atendentesGrupo = atendentesGrupo;
	}

	public UserSecurity getTempAtendente() {
		return tempAtendente;
	}

	public void setTempAtendente(UserSecurity tempAtendente) {
		this.tempAtendente = tempAtendente;
	}

	public String getRespostaModal() {
		return respostaModal;
	}

	public void setRespostaModal(String respostaModal) {
		this.respostaModal = respostaModal;
	}
	
    public void atribuiChamadoAAtendenteDisponivel(UserSecurity User, TicketService service){
    	/**
         * 0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday
         */        
        DiaSemana diaSemana = DiaSemana.getDiaSemana(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        List<Long> solicitanteIds = this.userSecurityRepository.solicitanteIds(FacesUtil.loggedUser(), service, diaSemana, new Date());
        
        System.out.println(Arrays.deepToString(solicitanteIds.toArray()));
        Collections.shuffle(solicitanteIds);
        Map<Long, Object> contarChamados = this.repository.contarChamados(solicitanteIds);        
        Long id = Long.MAX_VALUE;
        Long count = Long.MAX_VALUE;
        for (Long atendente : solicitanteIds) {        	
            Long tempCount = 0L;
            Integer key = atendente.intValue();            
            if (contarChamados.containsKey(atendente)) {
            //if(lista.contains(atEscalonamento)){
            	/*Group g = contarChamados.get(atendente);*/
            	//.toArray()[2];
            	LinkedHashSet obj = (LinkedHashSet) contarChamados.get(atendente);
            	Long value = (Long) obj.toArray()[0];
                tempCount = value;                 
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
        	e.printStackTrace();
            this.instance.setAtendente(null);
        }
    	
    }

	public String getCpfConsulta() {
		return cpfConsulta;
	}

	public void setCpfConsulta(String cpfConsulta) {
		this.cpfConsulta = cpfConsulta;
	}

	public Boolean getResultadoConsultaCpf() {
		return resultadoConsultaCpf;
	}

	public void setResultadoConsultaCpf(Boolean resultadoConsultaCpf) {
		this.resultadoConsultaCpf = resultadoConsultaCpf;
	}

	public ViewServidorChamado getServidorBuscaCpf() {
		return servidorBuscaCpf;
	}

	public void setServidorBuscaCpf(ViewServidorChamado servidorBuscaCpf) {
		this.servidorBuscaCpf = servidorBuscaCpf;
	}

	public ViewDadosServidor getDadosServidor() {
		return dadosServidor;
	}

	public void setDadosServidor(ViewDadosServidor dadosServidor) {
		this.dadosServidor = dadosServidor;
	}

	
    
	
    
}

