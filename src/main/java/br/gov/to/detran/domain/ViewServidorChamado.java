/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "vw_servidor_chamado")
public class ViewServidorChamado implements java.io.Serializable {

    @Id
    @Column(name = "cpf", nullable = false, length = 20)
    private String cpf;

    @Column(name = "matricula", nullable = false, length = 15)
    private String matricula;

    @Column(name = "nome", nullable = false, length = 70)
    private String nome;

    @Column(name = "nome_mae", nullable = false, length = 70)
    private String nomeMae;

    @Column(name = "sexo", nullable = false, length = 1)
    private String sexo;

    @Column(name = "rg", nullable = false, length = 20)
    private String rg;

    @Column(name = "rgOrgao", nullable = false, length = 20)
    private String rgOrgao;
    
    @Column(name = "rg_uf", nullable = false, length = 20)
    private String rgUf;
    
    @Column(name = "cep", nullable = false, length = 20)
    private String cep;
    
    @Column(name = "end_logradouro", nullable = false)
    private String endLogradouro;
    
    @Column(name = "end_numero", nullable = false)
    private String endNumero;
    
    @Column(name = "end_bairro", nullable = false)
    private String endBairro;
    
    @Column(name = "end_cidade", nullable = false)
    private String endCidade;
    
    @Column(name = "end_estado", nullable = false)
    private String endEstado;
    
    @Column(name = "end_complemento", nullable = false)
    private String endComplemento;

    @Column(name = "orgao", nullable = false, length = 100)
    private String orgao;
    
    @Column(name = "telResidencial", nullable = false, length = 100)
    private String telResidencial;

    @Column(name = "nascimento", nullable = false, length = 100)
    @Temporal(TemporalType.DATE)
    private Date nascimento;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getOrgao() {
        return orgao;
    }

    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getRgOrgao() {
        return rgOrgao;
    }

    public void setRgOrgao(String rgOrgao) {
        this.rgOrgao = rgOrgao;
    }

    public String getRgUf() {
        return rgUf;
    }

    public void setRgUf(String rgUf) {
        this.rgUf = rgUf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndLogradouro() {
        return endLogradouro;
    }

    public void setEndLogradouro(String endLogradouro) {
        this.endLogradouro = endLogradouro;
    }

    public String getEndNumero() {
        return endNumero;
    }

    public void setEndNumero(String endNumero) {
        this.endNumero = endNumero;
    }

    public String getEndBairro() {
        return endBairro;
    }

    public void setEndBairro(String endBairro) {
        this.endBairro = endBairro;
    }

    public String getEndCidade() {
        return endCidade;
    }

    public void setEndCidade(String endCidade) {
        this.endCidade = endCidade;
    }

    public String getEndEstado() {
        return endEstado;
    }

    public void setEndEstado(String endEstado) {
        this.endEstado = endEstado;
    }

    public String getEndComplemento() {
        return endComplemento;
    }

    public void setEndComplemento(String endComplemento) {
        this.endComplemento = endComplemento;
    }     

    public String getTelResidencial() {
        return telResidencial;
    }

    public void setTelResidencial(String telResidencial) {
        this.telResidencial = telResidencial;
    }        

    public String sexo() {
        try {
            if (this.sexo.equalsIgnoreCase("m")) {
                return "MASCULINO";
            } else if (this.sexo.equalsIgnoreCase("f")) {
                return "FEMININO";
            }
        } catch (NullPointerException e) {
        }
        return "INDEFINIDO";
    }
}
