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

import br.gov.to.detran.domain.SetorAtendimento;
import br.gov.to.detran.domain.view.ViewLotacao;
import br.gov.to.detran.repository.SetorAtendimentoRepository;
import br.gov.to.detran.repository.view.ViewLotacaoRepository;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class TicketSetorAtendimentoController extends BaseController<SetorAtendimento> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private @Inject SetorAtendimentoRepository repository;   
    private @Inject ViewLotacaoRepository viewLotacaoRepository;

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        SetorAtendimento setor = this.getFlash("setorInstance");
        if (setor != null) {
            this.instance = setor;            
        }           
    }

    public void insert() {
        try {
        	if(repository.existe(instance.getIdSetor(), null)){
        		throw new Exception("Setor já existente!"); 
        	}
        	ViewLotacao viewLotacao = viewLotacaoRepository.buscarLotacaoId(instance.getIdSetor().longValue());
        	this.instance.setSetor(viewLotacao.getNome());
            this.instance.setCreated(new Date());
            this.instance.setUpdated(new Date());            
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void update() {
        try {
        	if(repository.existe(instance.getIdSetor(), instance.getId())){
        		throw new Exception("Setor já existente!"); 
        	}
        	ViewLotacao viewLotacao = viewLotacaoRepository.buscarLotacaoId(instance.getId());
        	this.instance.setSetor(viewLotacao.getNome());
            this.instance.setUpdated(new Date());            
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeAndRedirect(SetorAtendimento instance, String link){        
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(TicketSetorAtendimentoController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(SetorAtendimento instance) {
        try {
            this.repository.remove(instance, true);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validar() {

    }
    
    public List<ViewLotacao> completeLotacao(String query) {
        return viewLotacaoRepository.buscarLotacao(query);
    }
    
    public List<SetorAtendimento> getAllGroups(){
    	return this.repository.getAll();
    }

    @Override
    public SetorAtendimentoRepository getRepository() {
        return this.repository;
    }
	
}
