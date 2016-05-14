/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.to.detran.enumeration.DiaSemana;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_escala_trabalho")
public class EscalaTrabalho extends AbstractEntity{
	@Column(name = "dia_semana")
	@Enumerated(EnumType.STRING)
	private DiaSemana diaSemana;
	
	@Column(name = "hora_inicial")
	@Temporal(TemporalType.TIME)
	private Date horaInicial;
	
	@Column(name = "hora_final")
	@Temporal(TemporalType.TIME)
	private Date horaFinal;
	
	@JoinColumn(name = "fk_user")
    @ManyToOne    
    private UserSecurity userSecurity;       

    public EscalaTrabalho() {
    }

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Date getHoraInicial() {
		return horaInicial;
	}

	public void setHoraInicial(Date horaInicial) {
		this.horaInicial = horaInicial;
	}

	public Date getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Date horaFinal) {
		this.horaFinal = horaFinal;
	}

	public UserSecurity getUserSecurity() {
		return userSecurity;
	}

	public void setUserSecurity(UserSecurity userSecurity) {
		this.userSecurity = userSecurity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((diaSemana == null) ? 0 : diaSemana.hashCode());
		result = prime * result + ((horaFinal == null) ? 0 : horaFinal.hashCode());
		result = prime * result + ((horaInicial == null) ? 0 : horaInicial.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EscalaTrabalho other = (EscalaTrabalho) obj;
		if (diaSemana != other.diaSemana)
			return false;
		if (horaFinal == null) {
			if (other.horaFinal != null)
				return false;
		} else if (!horaFinal.equals(other.horaFinal))
			return false;
		if (horaInicial == null) {
			if (other.horaInicial != null)
				return false;
		} else if (!horaInicial.equals(other.horaInicial))
			return false;
		return true;
	}        	
       
}
