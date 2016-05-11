/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_pagina_sistema")
public class PaginaSistema extends AbstractEntity{
    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;
    
    @Column(name = "arquivo", nullable = false, length = 255)
    private String arquivo;
    
    @Column(name = "ativado", nullable = false, length = 4000)
    private Boolean ativado;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public Boolean getAtivado() {
        return ativado;
    }

    public void setAtivado(Boolean ativado) {
        this.ativado = ativado;
    }        
  
}
