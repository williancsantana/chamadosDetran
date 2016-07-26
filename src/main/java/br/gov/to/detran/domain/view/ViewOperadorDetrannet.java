package br.gov.to.detran.domain.view;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "vw_sys_operadorchamado")
public class ViewOperadorDetrannet implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private String nome;
	private String cpf;
	private String tipo;
	private Boolean bloqueado;
	private String ciretran;
	private String obs;
	private String perfil;
	
	@Transient
	private List<String> perfis;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Boolean getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getCiretran() {
		return ciretran;
	}
	public void setCiretran(String ciretran) {
		this.ciretran = ciretran;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public List<String> getPerfis() {
		return perfis;
	}
	public void setPerfis(List<String> perfis) {
		this.perfis = perfis;
	}		
}
