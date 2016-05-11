/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_requisicao_acessos")
public class RequisicaoAcesso extends AbstractEntity{
   // @JoinColumn(name = "fk_chamado")    
    //@OneToOne
    @OneToOne
    @JoinColumn(name = "fk_chamado")    
    private TicketSupport chamado;
    
    @Column(name = "json", nullable = false, columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "cpf", nullable = false, length = 255)
    private String cpf;
    
    @Column(name = "setor", nullable = false, length = 255)
    private String setor;
    
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    
    @Column(name = "funcao", nullable = false, length = 255)
    private String funcao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public TicketSupport getChamado() {
        return chamado;
    }

    public void setChamado(TicketSupport chamado) {
        this.chamado = chamado;
    }        
  
}
