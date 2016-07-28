

package br.gov.to.detran.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "tb_lembrete_chamado")
public class TicketStickerSupport extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "fk_chamado", nullable= true)
	@OneToOne
	private TicketSupport ticketSupport;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hora_lembrete", nullable = false)
    private Date horaLembrete;
    
	
	public void setHoraLembrete(Date horaLembrete){
		this.horaLembrete = horaLembrete;
	}
	
	public Date getHoraLembrete(){
		return this.horaLembrete;
	}
	
	
	public TicketSupport getTicketSupport() {
		return ticketSupport;
	}
	public void setTicketSupport(TicketSupport ticketSupport) {
		this.ticketSupport = ticketSupport;
	}
		
}