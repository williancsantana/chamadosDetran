package br.gov.to.detran.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author willian
 *
 */

@Entity
@Table(name = "tb_servico_recorrente")
public class TicketRecurrentService extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@Column(name = "descricao")
	private String descricao;
	
	@JoinColumn(name="fk_service")
	@ManyToOne
	private TicketService servico;
	
	@JoinColumn(name = "fk_solicitante")
	@ManyToOne
	private UserSecurity solicitante;
	
	@Column(name = "assunto")
	private String assunto;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
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
	
	
}
