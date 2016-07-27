/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.to.detran.domain.GroupPermissions;
import br.gov.to.detran.domain.SetorAtendimento;
import br.gov.to.detran.domain.TicketGroup;
import br.gov.to.detran.domain.TicketGroupService;
import br.gov.to.detran.domain.TicketGroupServiceType;
import br.gov.to.detran.repository.AbstractRepository;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.TicketGroupRepository;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class TicketGroupController extends BaseController<TicketGroup> implements java.io.Serializable {

    private @Inject TicketGroupRepository repository;    

    private GroupPermissions chamadoAbrir = new GroupPermissions("chamado:abrir", Boolean.FALSE);
    private GroupPermissions chamadoAtender = new GroupPermissions("chamado:atender", Boolean.FALSE);
    private GroupPermissions chamadoVisualizar = new GroupPermissions("chamado:visualizar", Boolean.FALSE);
    private GroupPermissions adminUsuarios = new GroupPermissions("admin:usuarios", Boolean.FALSE);
    private GroupPermissions adminGrupos = new GroupPermissions("admin:grupos", Boolean.FALSE);
    private GroupPermissions adminServicos = new GroupPermissions("admin:servicos", Boolean.FALSE);
    private GroupPermissions adminChamados = new GroupPermissions("admin:chamados", Boolean.FALSE);
    private GroupPermissions adminSetor = new GroupPermissions("admin:setor", Boolean.FALSE);
    private GroupPermissions relatorioAtendimento = new GroupPermissions("relatorio:atendimento", Boolean.FALSE);
    private GroupPermissions relatorioSistema = new GroupPermissions("relatorio:sistema", Boolean.FALSE);
    
    private SetorAtendimento ticketSetor;
    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        TicketGroup grupo = this.getFlash("grupoInstance");
        if (grupo != null) {
            this.instance = grupo;
            this.restorePermissionFromString();
        }           
    }

    public void insert() {
        try {
            this.instance.setCreated(new Date());
            this.instance.setUpdated(new Date());
            this.instance.setPermissoes(this.mountPermissionFromString());
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        try {
            this.instance.setUpdated(new Date());
            this.instance.setPermissoes(this.mountPermissionFromString());
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeAndRedirect(TicketGroup instance, String link){        
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(TicketGroupController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(TicketGroup instance) {
        try {
            this.repository.remove(instance, true);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validar() {

    }
    
    public List<TicketGroup> getAllGroups(){
    	return this.repository.getAll();
    }

    @Override
    public AbstractRepository getRepository() {
        return this.repository;
    }

    public String mountPermissionFromString() {
        String permissions = "";
        permissions += this.genericPermission(chamadoAbrir);
        permissions += this.genericPermission(chamadoAtender);
        permissions += this.genericPermission(chamadoVisualizar);
        permissions += this.genericPermission(adminUsuarios);
        permissions += this.genericPermission(adminGrupos);
        permissions += this.genericPermission(adminServicos);
        permissions += this.genericPermission(adminChamados);
        permissions += this.genericPermission(adminSetor);
        permissions += this.genericPermission(relatorioAtendimento);
        permissions += this.genericPermission(relatorioSistema);
        return permissions;
    }

    public void restorePermissionFromString() {
        this.genericRestorePermission(chamadoAbrir);
        this.genericRestorePermission(chamadoAtender);
        this.genericRestorePermission(chamadoVisualizar);
        this.genericRestorePermission(adminUsuarios);
        this.genericRestorePermission(adminGrupos);
        this.genericRestorePermission(adminServicos);
        this.genericRestorePermission(adminChamados);
        this.genericRestorePermission(adminSetor);
        this.genericRestorePermission(relatorioAtendimento);
        this.genericRestorePermission(relatorioSistema);
    }

    public void genericRestorePermission(GroupPermissions permission) {
        if (this.instance.containPermission(permission.getKey())) {
            permission.setSelected(true);
        }
    }
    
    public void addTicketSetor(SetorAtendimento setorAtendimento){
    	this.ticketSetor = setorAtendimento;
    	
    }
    
    public void removeTicketSetor(){
    	
    	this.instance.setSetorAtendimento(null);
    	this.addMenssage(FacesMessage.SEVERITY_INFO, "Setor Removido", "O setor foi removido com sucesso");
        
        return;
    }
    
    public void setSetorAtendimento(){
    	if (this.ticketSetor != null) {            
            this.instance.setSetorAtendimento(ticketSetor);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Setor adicionado", "Adicionar");
            
            return;
        }
        this.addMenssage(FacesMessage.SEVERITY_ERROR, "Nenhum Setor selecionado", "Adicionar");
    	
    	
    }

    public String genericPermission(GroupPermissions permission) {
        if (permission.getSelected()) {
            return permission.getKey() + ";";
        }
        
        return "";
    }
    public boolean hasATicketSetor(){
    	if(ticketSetor!=null) return true;
    	return false;
    }
    public GroupPermissions getChamadoAbrir() {
        return chamadoAbrir;
    }

    public void setChamadoAbrir(GroupPermissions chamadoAbrir) {
        this.chamadoAbrir = chamadoAbrir;
    }

    public GroupPermissions getChamadoAtender() {
        return chamadoAtender;
    }

    public void setChamadoAtender(GroupPermissions chamadoAtender) {
        this.chamadoAtender = chamadoAtender;
    }

    public GroupPermissions getChamadoVisualizar() {
        return chamadoVisualizar;
    }

    public void setChamadoVisualizar(GroupPermissions chamadoVisualizar) {
        this.chamadoVisualizar = chamadoVisualizar;
    }

    public GroupPermissions getAdminUsuarios() {
        return adminUsuarios;
    }

    public void setAdminUsuarios(GroupPermissions adminUsuarios) {
        this.adminUsuarios = adminUsuarios;
    }

    public GroupPermissions getAdminGrupos() {
        return adminGrupos;
    }

    public void setAdminGrupos(GroupPermissions adminGrupos) {
        this.adminGrupos = adminGrupos;
    }

    public GroupPermissions getAdminServicos() {
        return adminServicos;
    }

    public void setAdminServicos(GroupPermissions adminServicos) {
        this.adminServicos = adminServicos;
    }

    public GroupPermissions getAdminChamados() {
        return adminChamados;
    }

    public void setAdminChamados(GroupPermissions adminChamados) {
        this.adminChamados = adminChamados;
    }

    public GroupPermissions getRelatorioAtendimento() {
        return relatorioAtendimento;
    }

    public void setRelatorioAtendimento(GroupPermissions relatorioAtendimento) {
        this.relatorioAtendimento = relatorioAtendimento;
    }

    public GroupPermissions getRelatorioSistema() {
        return relatorioSistema;
    }

    public void setRelatorioSistema(GroupPermissions relatorioSistema) {
        this.relatorioSistema = relatorioSistema;
    }

	public void setRepository(TicketGroupRepository repository) {
		this.repository = repository;
	}

	public GroupPermissions getAdminSetor() {
		return adminSetor;
	}

	public void setAdminSetor(GroupPermissions adminSetor) {
		this.adminSetor = adminSetor;
	}	

	
}
