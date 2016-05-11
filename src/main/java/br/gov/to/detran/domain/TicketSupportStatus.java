/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

/**
 *
 * @author maycon
 */
public enum TicketSupportStatus {
    ABERTO("Aberto"), PENDENTE("Pendente"), PENDENTE_USUARIO("Pendente de Usuário"),
    PENDENTE_TERCEIROS("Pendente Terceiros"), FECHADO("Fechados"), REABERTO("Reaberto");

    private TicketSupportStatus(String label) {
        this.label = label;
    }
            
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
       
}
