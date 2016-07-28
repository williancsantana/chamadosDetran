package br.gov.to.detran.domain.view;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.util.FacesUtil;

/**
 * 
 * @author William
 * Modificado por Maycon Costa
 *
 */
@Entity
@Table(name = "vw_estante_chamados")
public class ViewTicketSupport extends AbstractEntity{
			
	private static final long serialVersionUID = 1L;

	@Column(name="chamadonumero")
	private String numero;
	
	@Column(name="chamadoassunto")
	private String assunto;	
	
	@Column(name="chamadoidsolicitante")
	private Long idSolicitante;
	
	@Column(name="chamadonomesolicitante")
	private String solicitante;
	
	@Column(name="chamadoidatendente")
	private Long idAtendente;
	
	@Column(name="chamadonomeatendente")
	private String atendente;
	
	@Column(name="chamadoresposta")
	private Date ultimaResposta;
	
	@Column(name="statuschamado")
	@Enumerated(EnumType.STRING)	
	private TicketSupportStatus status;
	
	@Column(name="chamadoidservico")
	private Long idServico;
	
	@Column(name="chamadodescricaoservico")
	private String servico;
	
	@Column(name="chamadotemanexo")
	private Boolean existeAnexo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sol_viewat")
	private Date solViewat;
	    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "at_viewat")
	private Date atViewat;

	@Column(name = "lembreteid")
	private Long stickerID;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "horalembrete")
	private Date stickerDate;
	
	public void setStickerDate(Date stickerDate){
		this.stickerDate = stickerDate;
	}
	
	public Date getStickerDate(){
		return this.stickerDate;
	}
	public void setStickerID(Long stickerID){
		this.stickerID = stickerID;
	}
	
	public Long getStickerID(){
		return this.stickerID;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public boolean atrasado(){
		if(this.getStickerID()!=null && new Date().compareTo(this.getStickerDate())>=0){
			return true;
		}
		return false;
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

	public Long getIdSolicitante() {
		return idSolicitante;
	}

	public void setIdSolicitante(Long idSolicitante) {
		this.idSolicitante = idSolicitante;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public Long getIdAtendente() {
		return idAtendente;
	}

	public void setIdAtendente(Long idAtendente) {
		this.idAtendente = idAtendente;
	}

	public String getAtendente() {
		return atendente;
	}

	public void setAtendente(String atendente) {
		this.atendente = atendente;
	}

	public Date getUltimaResposta() {
		return ultimaResposta;
	}

	public void setUltimaResposta(Date ultimaResposta) {
		this.ultimaResposta = ultimaResposta;
	}

	public TicketSupportStatus getStatus() {
		return status;
	}

	public void setStatus(TicketSupportStatus status) {
		this.status = status;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public Boolean getExisteAnexo() {
		return existeAnexo;
	}

	public void setExisteAnexo(Boolean existeAnexo) {
		this.existeAnexo = existeAnexo;
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
	
	public boolean isUnseen(){        
	       UserSecurity onlineUser = FacesUtil.loggedUser();
	       return (this.solicitante != null && Objects.equals(this.idSolicitante, onlineUser.getId()) && (this.solViewat == null || this.solViewat.before(ultimaResposta))) ||
	               (this.atendente != null && Objects.equals(this.idAtendente, onlineUser.getId()) && (this.atViewat == null || this.atViewat.before(ultimaResposta)));
	    }
	    
	    public String printUnseenBadge(){
	        UserSecurity onlineUser = FacesUtil.loggedUser();                
	        if(this.solicitante != null && Objects.equals(this.idSolicitante, onlineUser.getId())) {
	            if(this.solViewat == null){
	                return "<div class='ui green horizontal tiny label'>Novo</div>";
	            }else if(this.solViewat.before(ultimaResposta)){
	                return "<div class='ui blue horizontal tiny label'>Atualizado</div>";
	            }else{
	                return "";
	            }
	        }
	        else if(this.atendente != null && Objects.equals(this.idAtendente, onlineUser.getId())) {
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
}
