/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import br.gov.to.detran.domain.TicketGroup;
import br.gov.to.detran.domain.TicketGroupService;
import br.gov.to.detran.domain.TicketGroupServiceType;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.TicketServiceField;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.TicketGroupRepository;
import br.gov.to.detran.repository.TicketServiceRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class TicketServicesController extends BaseController<TicketService> implements java.io.Serializable {

    private @Inject TicketServiceRepository repository;
    private @Inject TicketGroupRepository ticketGroupRepository;
    private TicketServiceField field;
    
    private List<TicketGroup> listGrupos;
    private TicketGroup ticketGroup;    
    
    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        TicketService service = this.getFlash("serviceInstance");
        if (service != null) {
            this.instance = service;          
            this.instance.mountFields();
        }else{
            this.instance.setPermissoes(new ArrayList<TicketGroupService>());
        }  
        this.field = new TicketServiceField();        
    }

    public void insert() {
        try {
            this.instance.setCreated(new Date());
            this.instance.setUpdated(new Date());            
            this.instance.setCategoria(this.instance.getCategoria().toUpperCase().trim());
            Type ticketServiceField = new TypeToken<ArrayList<TicketServiceField>>(){}.getType();
            Gson gson = new Gson();                        
            this.instance.setCampos_json(gson.toJson(this.instance.getFields(), ticketServiceField));
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        try {
            this.instance.setUpdated(new Date());
            this.instance.setCategoria(this.instance.getCategoria().toUpperCase().trim());
            Type ticketServiceField = new TypeToken<ArrayList<TicketServiceField>>(){}.getType();
            Gson gson = new Gson();                        
            this.instance.setCampos_json(gson.toJson(this.instance.getFields(), ticketServiceField));
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeAndRedirect(TicketService instance, String link){        
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(TicketServicesController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(TicketService instance) {
        try {
            this.repository.remove(instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validar() {

    }

    @Override
    public Repository getRepository() {
        return this.repository;
    }
    
    public void insertField(){
        if(this.field != null){
            this.instance.getFields().add(field);
            this.field = new TicketServiceField();
        }
    }
    
    public void removeField(TicketServiceField field){
        this.instance.getFields().remove(field);
    }
    
    public void adicionarPermissaoAtendente() {
        if (this.ticketGroup != null) {            
            this.instance.getPermissoes().add(new TicketGroupService(this.instance, ticketGroup, TicketGroupServiceType.ATENDENTE));
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Grupo adicionado", "Adicionar");
            this.ticketGroup = null;
            return;
        }
        this.addMenssage(FacesMessage.SEVERITY_ERROR, "Nenhum grupo selecionado", "Adicionar");
    }
    
    public void adicionarPermissaoSolicitante() {
        if (this.ticketGroup != null) {            
            this.instance.getPermissoes().add(new TicketGroupService(this.instance, ticketGroup, TicketGroupServiceType.SOLICITANTE));
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Grupo adicionado", "Adicionar");
            this.ticketGroup = null;
            return;
        }
        this.addMenssage(FacesMessage.SEVERITY_ERROR, "Nenhum grupo selecionado", "Adicionar");
    }
    
    public void removeGrupo(TicketGroupService grupo){
        this.instance.getPermissoes().remove(grupo);
    }

    public void addTicketGroup(TicketGroup g) {        
        ticketGroup = g;
    }

    public TicketServiceField getField() {
        return field;
    }

    public void setField(TicketServiceField field) {
        this.field = field;
    }
    
    public List<TicketGroup> getListGrupos() {
        if (this.listGrupos == null) {
            this.listGrupos = ticketGroupRepository.getGroupsAtendenteAndSolicitantes();
        }
        return this.listGrupos;
    }
    
    public boolean isGroupSelectedAtendente(){
        if(this.ticketGroup == null){
            return false;
        }
        return this.ticketGroup.containPermission("chamado:atender");
    }
    
    public boolean isGroupSelectedSolicitante(){
        if(this.ticketGroup == null){
            return false;
        }
        return this.ticketGroup.containPermission("chamado:abrir");
    }
           
}
