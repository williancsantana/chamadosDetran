/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.leilao.controller;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.to.detran.controller.BaseController;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.leilao.domain.Categoria;
import br.gov.to.detran.leilao.domain.Situacao;
import br.gov.to.detran.leilao.repository.CategoriaRepository;
import br.gov.to.detran.repository.Repository;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class CategoriaController extends BaseController<Categoria> implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private @Inject CategoriaRepository repository;     

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        Categoria itemCategoria = this.getFlash("itemLeilaoInstance");
        if (itemCategoria != null) {
            this.instance = itemCategoria;            
        }           
    }

    public void insert() {
        try {
            this.instance.setCreated(new Date());
            this.instance.setUpdated(new Date());            
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Adicionado", "Cadastro");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        try {
            this.instance.setUpdated(new Date());            
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeAndRedirect(Categoria instance, String link){        
        try {
            this.remove(instance);
            this.redirect(link);
        } catch (IOException ex) {
            Logger.getLogger(CategoriaController.class.getName()).log(Level.SEVERE, null, ex);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Não foi possivel redirecionar para a página", "Redirecionar");
        }
    }

    public void remove(Categoria instance) {
        try {
            this.repository.remove(instance, true);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Registro Excluído", "Exclusão");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
    
    public void viewVeiculos(Categoria categoria) {
        try {            
            this.redirect("/leilao/veiculo/list.jsf?id=" + String.valueOf(categoria.getId()));
        } catch (IOException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Redirecionar", "Não foi possivel redirecionar para o chamado");
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Repository getRepository() {
        return this.repository;
    }
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}
    

}
