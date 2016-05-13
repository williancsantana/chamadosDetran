/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import static com.querydsl.collections.CollQueryFactory.*;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_servico")
public class TicketService extends AbstractEntity{
    @Column(name = "descricao", nullable = false)
    private String descricao;
    
    @Column(name = "categoria", nullable = false, length = 1000)
    private String categoria;        
    
    @Column(name = "solucoes", nullable = false, length = 1000)
    private String solucoes;        
        
    @Column(name = "campos_json", nullable = false, length = 4000)
    private String campos_json;        
    
    @OneToMany(mappedBy = "servico", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketGroupService> permissoes;
    
    @JoinColumn(name = "fk_pagina_sistema")
    @ManyToOne
    private PaginaSistema pagina;
    
    
    @Column(name = "padrao", nullable = true)
    private Boolean padrao = false;
    
    @Transient
    private List<TicketServiceField> fields = new ArrayList<>();
    
    @Transient
    private List<TicketGroupService> atendentes;
    
    @Transient
    private List<TicketGroupService> solicitantes;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<TicketGroupService> getPermissoes() {        
        return permissoes;
    }

    public void setPermissoes(List<TicketGroupService> permissoes) {
        this.permissoes = permissoes;
    }                    

    public String getCampos_json() {
        return campos_json;
    }

    public void setCampos_json(String campos_json) {
        this.campos_json = campos_json;
    }        

    public String getSolucoes() {
        return solucoes;
    }

    public void setSolucoes(String solucoes) {
        this.solucoes = solucoes;
    }
    
     public List<TicketServiceField> getFields() {
        return fields;
    }

    public void setFields(List<TicketServiceField> fields) {
        this.fields = fields;
    } 

    public void mountFields() {
        Type ticketServiceField = new TypeToken<ArrayList<TicketServiceField>>(){}.getType();
        Gson gson = new Gson();                        
        this.fields = gson.fromJson(this.campos_json, ticketServiceField);
    }
    
    public String mainPath(){
        String[] split = this.categoria.split(":");
        return split[0];
    }
    
    private List<TicketGroupService> atendentes(){        
        return from(QTicketGroupService.ticketGroupService, this.permissoes)
                .where(QTicketGroupService.ticketGroupService.tipo.eq(TicketGroupServiceType.ATENDENTE)).fetch();
    }
    
    private List<TicketGroupService> solicitantes(){
        return from(QTicketGroupService.ticketGroupService, this.permissoes)
                .where(QTicketGroupService.ticketGroupService.tipo.eq(TicketGroupServiceType.SOLICITANTE)).fetch();
    }

    public List<TicketGroupService> getAtendentes() {
        if(this.atendentes == null){
            this.atendentes = this.atendentes();
        }
        return atendentes;
    }

    public void setAtendentes(List<TicketGroupService> atendentes) {
        this.atendentes = atendentes;
    }

    public List<TicketGroupService> getSolicitantes() {
        if(this.solicitantes == null){
            this.solicitantes = this.solicitantes();
        }
        return solicitantes;
    }

    public void setSolicitantes(List<TicketGroupService> solicitantes) {
        this.solicitantes = solicitantes;
    }        
    
    public List<String> getTips(){
    	List<String> tips = new ArrayList<>();
    	for(String tp : this.solucoes.split("\n")){
    		if(!tp.isEmpty()){
    			tips.add(tp);
    		}    		
    	}
    	
        return tips;
    }

    public PaginaSistema getPagina() {
        return pagina;
    }

    public void setPagina(PaginaSistema pagina) {
        this.pagina = pagina;
    }     
    
    public String pageChamado(){
        try{
            return "/" + this.pagina.getArquivo() + ".jsf";            
        }catch(Exception e){
            return "/abrirchamado.jsf";
        }
    }
    
    public Boolean getPadrao() {		
		return padrao != null ? padrao : false; 
	}

	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}
    
}
