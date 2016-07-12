/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.gov.to.detran.util.FacesUtil;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_chamado")
public class TicketSupport extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "numero", nullable = false)
    private String numero;
    
    @Column(name = "assunto", nullable = false, length = 4000)
    private String assunto;
    
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "campos_json_values", nullable = true, columnDefinition = "TEXT")
    private String camposJsonValues;
       
    @JoinColumn(name = "fk_solicitante", nullable = false)
    @ManyToOne
    private UserSecurity solicitante;
    
    @JoinColumn(name = "fk_atendente", nullable = true)
    @ManyToOne
    private UserSecurity atendente;
        
    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TicketReply> respostas = new ArrayList<>();
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketSupportStatus status;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ultima_resposta", nullable = false)
    private Date ultimaResposta;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reaberto_em", nullable = true)
    private Date reabertoEm;
        
    @JoinColumn(name = "fk_service")
    @ManyToOne
    private TicketService servico;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sol_viewat", nullable = true)
    private Date solViewat;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "at_viewat", nullable = true)
    private Date atViewat;
           
    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TicketAttachment> anexos = new ArrayList<>();
    
    @Column(name = "existe_anexo")
    private Boolean withAttachment = false;
    
    //@OneToOne(mappedBy = "chamado", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @Transient
    private RequisicaoAcesso requisaoAcesso = new RequisicaoAcesso();
    
    @Transient
    private List<TicketServiceField> fields = new ArrayList<>();

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCamposJsonValues() {
        return camposJsonValues;
    }

    public void setCamposJsonValues(String camposJsonValues) {
        this.camposJsonValues = camposJsonValues;
    }

    public TicketService getServico() {
        return servico;
    }

    public void setServico(TicketService servico) {
        this.servico = servico;
    }   

    public UserSecurity getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(UserSecurity solicitante) {
        this.solicitante = solicitante;
    }

    public UserSecurity getAtendente() {
        return atendente;
    }

    public void setAtendente(UserSecurity atendente) {
        this.atendente = atendente;
    }

    public List<TicketReply> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<TicketReply> respostas) {
        this.respostas = respostas;
    }

    public TicketSupportStatus getStatus() {
        return status;
    }

    public void setStatus(TicketSupportStatus status) {
        this.status = status;
    }    

    public Date getUltimaResposta() {
        return ultimaResposta;
    }

    public void setUltimaResposta(Date ultimaResposta) {
        this.ultimaResposta = ultimaResposta;
    }

    public Date getReabertoEm() {
        return reabertoEm;
    }

    public void setReabertoEm(Date reabertoEm) {
        this.reabertoEm = reabertoEm;
    }

    public Date getSolViewat() {
        return solViewat;
    }

    public void setSolViewat(Date solViewat) {
        this.solViewat = solViewat;
    }

    public Date getAtViewat() {
        return atViewat;
    }

    public void setAtViewat(Date atViewat) {
        this.atViewat = atViewat;
    }

    public List<TicketServiceField> getFields() {
        return fields;
    }

    public void setFields(List<TicketServiceField> fields) {
        this.fields = fields;
    }        
            
    public boolean isUnseen(){        
       UserSecurity onlineUser = FacesUtil.loggedUser();
       return (this.solicitante != null && Objects.equals(this.solicitante.getId(), onlineUser.getId()) && (this.solViewat == null || this.solViewat.before(ultimaResposta))) ||
               (this.atendente != null && Objects.equals(this.atendente.getId(), onlineUser.getId()) && (this.atViewat == null || this.atViewat.before(ultimaResposta)));
    }
    
    public String printUnseenBadge(){
        UserSecurity onlineUser = FacesUtil.loggedUser();                
        if(this.solicitante != null && Objects.equals(this.solicitante.getId(), onlineUser.getId())) {
            if(this.solViewat == null){
                return "<div class='ui green horizontal tiny label'>Novo</div>";
            }else if(this.solViewat.before(ultimaResposta)){
                return "<div class='ui blue horizontal tiny label'>Atualizado</div>";
            }else{
                return "";
            }
        }
        else if(this.atendente != null && Objects.equals(this.atendente.getId(), onlineUser.getId())) {
            if(this.atViewat == null){
                return "<div class='ui green horizontal tiny label'>Novo</div>";
            }else if(this.atViewat.before(ultimaResposta)){
                return "<div class='ui blue horizontal tiny label'>Atualizado</div>";
            }else{
                return "";
            }
        }
        else{
            return "";
        }
    }
    
    public void mountFields() {
        Type ticketServiceField = new TypeToken<ArrayList<TicketServiceField>>(){}.getType();
        Gson gson = new Gson();                        
        this.fields = gson.fromJson(this.camposJsonValues, ticketServiceField);
    }
    
    public boolean permissaoResponderSolicitante(){
        UserSecurity userLogged = FacesUtil.loggedUser();
        return solicitante != null && Objects.equals(userLogged.getId(), this.solicitante.getId()) && this.status == TicketSupportStatus.FECHADO;
    }
    
    public boolean permissaoResponderSolicitanteNotFechado(){
        UserSecurity userLogged = FacesUtil.loggedUser();
        return solicitante != null && Objects.equals(userLogged.getId(), this.solicitante.getId()) && this.status != TicketSupportStatus.FECHADO;
    }
    
    public boolean atendenteStatus(String status){
        UserSecurity userLogged = FacesUtil.loggedUser();
        return this.atendente != null && Objects.equals(userLogged.getId(), this.atendente.getId()) && this.status == TicketSupportStatus.valueOf(status);
    }
    
    public boolean atendenteNotStatus(String status){
        UserSecurity userLogged = FacesUtil.loggedUser();
        return this.atendente != null && Objects.equals(userLogged.getId(), this.atendente.getId()) && this.status != TicketSupportStatus.valueOf(status);
    }
    
    public String ticketIcon(){
        switch(status) {
            case ABERTO:
                return "comments outline";
            case REABERTO:
                return "refresh";
            case PENDENTE_USUARIO:
            case PENDENTE_TERCEIROS:
                return "warning sign";
            case FECHADO:
                return "ban";
        }
        return "";
    }
    
    public String ticketIconTitle(){
         switch(status) {
            case ABERTO:
                return "Chamado aberto";
            case REABERTO:
                return "Chamado reaberto";
            case PENDENTE_USUARIO:
                return "Chamado pendente com o solicitante";
            case PENDENTE_TERCEIROS:
                return "Chamado pendente com terceiros";
            case FECHADO:
                return "Chamado fechado";
        }    
        return "";
    }        
    
    public String ticketColor(){
        switch(status) {
            case ABERTO:
            case REABERTO:            
                return "green";
            case PENDENTE_USUARIO:
            case PENDENTE_TERCEIROS:
                return "orange";
            case FECHADO:
                return "red";            
        }
        return "";
    }
    
    public String markupService() {        
        String[] path = this.servico.getCategoria().split(":");
        String paths = "";
        for(String p : path){
            paths += "<div class='section'>" + p.trim() + "</div>" + "<i class='right chevron icon divider'></i>";
        }
        return paths + this.servico.getDescricao();
    }

    public RequisicaoAcesso getRequisaoAcesso() {
        return requisaoAcesso;
    }

    public void setRequisaoAcesso(RequisicaoAcesso requisaoAcesso) {
        this.requisaoAcesso = requisaoAcesso;
    }        
    
	public List<TicketAttachment> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<TicketAttachment> anexos) {
		this.anexos = anexos;
	}

	public Boolean getWithAttachment() {
		return withAttachment;
	}

	public void setWithAttachment(Boolean withAttachment) {
		this.withAttachment = withAttachment;
	}	
           
    
}
