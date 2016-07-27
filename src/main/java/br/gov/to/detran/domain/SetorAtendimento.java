package br.gov.to.detran.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_setor_atendimento")
public class SetorAtendimento extends AbstractEntity{	
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "setor", length = 600)
	private String setor;
	
	@Column(name = "id_setor")
	private Integer idSetor;
	
	
	public String getSetor() {
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	public Integer getIdSetor() {
		return idSetor;
	}
	public void setIdSetor(Integer idSetor) {
		this.idSetor = idSetor;
	}
	
	
		
}