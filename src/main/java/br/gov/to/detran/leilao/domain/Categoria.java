package br.gov.to.detran.leilao.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.gov.to.detran.domain.AbstractEntity;

@Entity
@Table(name="tb_item_categoria", schema = "leilao")
public class Categoria extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

}
