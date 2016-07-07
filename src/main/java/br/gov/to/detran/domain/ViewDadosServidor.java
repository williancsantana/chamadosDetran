package br.gov.to.detran.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="vw_dados_servidor")
public class ViewDadosServidor implements java.io.Serializable {

	@Column(name="nome", length = 30)
	private String nome;
	@Id
	@Column(name="cpf", length = 15)
	private String cpf;
	
	@Column(name="matricula", length = 15)
	private String matricula;
		
	private int codSetor;
	
	private String nomesetor;
	

	private int codsituacaofuncional;
	private String descricaosituacaofuncional;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public int getCodSetor() {
		return codSetor;
	}
	public void setCodSetor(int codSetor) {
		this.codSetor = codSetor;
	}
	public String getNomesetor() {
		return nomesetor;
	}
	public void setNomesetor(String nomesetor) {
		this.nomesetor = nomesetor;
	}
	public int getCodsituacaofuncional() {
		return codsituacaofuncional;
	}
	public void setCodsituacaofuncional(int codsituacaofuncional) {
		this.codsituacaofuncional = codsituacaofuncional;
	}
	public String getDescricaosituacaofuncional() {
		return descricaosituacaofuncional;
	}
	public void setDescricaosituacaofuncional(String descricaosituacaofuncional) {
		this.descricaosituacaofuncional = descricaosituacaofuncional;
	}
	
	
	
	
	
}
