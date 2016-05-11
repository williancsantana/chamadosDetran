/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Leice
 */
public class TicketServiceField implements java.io.Serializable{
    private String descricao;
    private String tipo;
    private String itens;
    private boolean obrigatorio;
    private String value;    
    private List<String> selectedOptions;

    public TicketServiceField() {
    }

    public TicketServiceField(String descricao, String tipo, boolean obrigatorio) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.obrigatorio = obrigatorio;
    }        

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }        

    public String getItens() {
        return itens;
    }

    public void setItens(String itens) {
        this.itens = itens;
    }        
    
    public String[] getItensArray(){
        return this.itens.split(":");
    }

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }
    
    public String respostas(){
        if("checkbox".equals(tipo)){
            return Arrays.deepToString(selectedOptions.toArray()).replace("[", "").replace("]", "");
        }else{
            return value;
        }
    }
       
    
}
