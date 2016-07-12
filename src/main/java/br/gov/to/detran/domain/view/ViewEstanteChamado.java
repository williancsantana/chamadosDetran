package br.gov.to.detran.domain.view;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.UserSecurity;



@Entity
@Table(name = "vw_estante_chamados")
public class ViewEstanteChamado extends AbstractEntity{
	@Column(name="numero",nullable=false)
	private String numero;
	
	@Column(name="assunto", nullable=false, length=4000)
	private String assunto;
	
	@JoinColumn(name="fk_solicitante",nullable=false)
	@ManyToOne
	private UserSecurity solicitante;
	
	@JoinColumn(name="fk_atendente",nullable=false)
	@ManyToOne
	private UserSecurity atendente;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ultima_resposta", nullable = false)
    private Date ultimaResposta;
	
	@Column(name="status",nullable=false)
	private String status;
	
	@JoinColumn(name="fk_service",nullable=false)
	@ManyToOne
	private TicketService servico;
	
	@Column(name="existe_anexo")
	private Boolean existeAnexo;

	public String getNumero() {
		return numero;
	}

	public String getAssunto() {
		return assunto;
	}

	public UserSecurity getSolicitante() {
		return solicitante;
	}

	public UserSecurity getAtendente() {
		return atendente;
	}

	public Date getUltimaResposta() {
		return ultimaResposta;
	}

	public String getStatus() {
		return status;
	}

	public TicketService getServico() {
		return servico;
	}

	public Boolean getExisteAnexo() {
		return existeAnexo;
	}
	
	
	
}
