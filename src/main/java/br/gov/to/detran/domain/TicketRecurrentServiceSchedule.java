package br.gov.to.detran.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * @author willian
 *
 */

@Entity
@Table(name = "tb_servico_recorrente_horario")
public class TicketRecurrentServiceSchedule extends AbstractEntity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="dia_semana")
	private String diaSemana;
	
	@JoinColumn(name = "fk_servico_recorrente")
	private TicketRecurrentService servicoRecorrente;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_agendada")
	private Date dataAgendada;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hora_agendada")
	private Date horaAgendada;
	
	@Column(name = "descricao")
	private String descricao;

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public TicketRecurrentService getServicoRecorrente() {
		return servicoRecorrente;
	}

	public void setServicoRecorrente(TicketRecurrentService servicoRecorrente) {
		this.servicoRecorrente = servicoRecorrente;
	}

	public Date getDataAgendada() {
		return dataAgendada;
	}

	public void setDataAgendada(Date dataAgendada) {
		this.dataAgendada = dataAgendada;
	}

	public Date getHoraAgendada() {
		return horaAgendada;
	}

	public void setHoraAgendada(Date horaAgendada) {
		this.horaAgendada = horaAgendada;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
