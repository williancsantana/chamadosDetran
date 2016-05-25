package br.gov.to.detran.domain;

public class AtendenteEscalonamento {
	private Long id;
	private Long quantidade;
	public AtendenteEscalonamento(Long id, Long quantidade) {
		super();
		this.id = id;
		this.quantidade = quantidade;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}	
}
