package br.gov.to.detran.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Maycon Costa
 *
 */
@Entity
@Table(name = "tb_movimentacao_chamado")
public class MovimentacaoChamado extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "fk_chamado")
	@ManyToOne
	private TicketSupport ticketSupport;
	
	@JoinColumn(name = "fk_setor")
	@ManyToOne
	private SetorAtendimento setor;
	
	@JoinColumn(name = "fk_usuario_atendente", nullable = true)
	@ManyToOne
	private UserSecurity usuarioAtendente;
	
	@JoinColumn(name = "fk_usuario_movimentacao")
	@ManyToOne
	private UserSecurity usuarioMovimentacao;
	
	@JoinColumn(name = "fk_usuario_rejeitado", nullable = true)
	@ManyToOne
	private UserSecurity usuarioRejeitado;
	
	@JoinColumn(name = "fk_servico")
	@ManyToOne
	private TicketService servico;
	
	@JoinColumn(name = "fk_servico_historico", nullable = true)
	@ManyToOne
	private TicketService oldServico;
		
	private Boolean aceito = false;
	
	public TicketSupport getTicketSupport() {
		return ticketSupport;
	}
	public void setTicketSupport(TicketSupport ticketSupport) {
		this.ticketSupport = ticketSupport;
	}
	public SetorAtendimento getSetor() {
		return setor;
	}
	public void setSetor(SetorAtendimento setor) {
		this.setor = setor;
	}
	public UserSecurity getUsuarioAtendente() {
		return usuarioAtendente;
	}
	public void setUsuarioAtendente(UserSecurity usuarioAtendente) {
		this.usuarioAtendente = usuarioAtendente;
	}
	public UserSecurity getUsuarioMovimentacao() {
		return usuarioMovimentacao;
	}
	public void setUsuarioMovimentacao(UserSecurity usuarioMovimentacao) {
		this.usuarioMovimentacao = usuarioMovimentacao;
	}
	public UserSecurity getUsuarioRejeitado() {
		return usuarioRejeitado;
	}
	public void setUsuarioRejeitado(UserSecurity usuarioRejeitado) {
		this.usuarioRejeitado = usuarioRejeitado;
	}
	public Boolean getAceito() {
		return aceito;
	}
	public void setAceito(Boolean aceito) {
		this.aceito = aceito;
	}
	public TicketService getServico() {
		return servico;
	}
	public void setServico(TicketService servico) {
		this.servico = servico;
	}
	public TicketService getOldServico() {
		return oldServico;
	}
	public void setOldServico(TicketService oldServico) {
		this.oldServico = oldServico;
	}			
	
}