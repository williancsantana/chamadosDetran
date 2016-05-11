/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_grupo_servico")
public class TicketGroupService extends AbstractEntity{
    @JoinColumn(name = "fk_servico", nullable = false)
    @ManyToOne
    private TicketService servico;
    
    @JoinColumn(name = "fk_grupo", nullable = false)
    @ManyToOne
    private TicketGroup grupo;
    
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TicketGroupServiceType tipo;     

    public TicketGroupService() {
    }

    public TicketGroupService(TicketService servico, TicketGroup grupo, TicketGroupServiceType tipo) {
        this.servico = servico;
        this.grupo = grupo;
        this.tipo = tipo;
    }        

    public TicketService getServico() {
        return servico;
    }

    public void setServico(TicketService servico) {
        this.servico = servico;
    }

    public TicketGroup getGrupo() {
        return grupo;
    }

    public void setGrupo(TicketGroup grupo) {
        this.grupo = grupo;
    }

    public TicketGroupServiceType getTipo() {
        return tipo;
    }

    public void setTipo(TicketGroupServiceType tipo) {
        this.tipo = tipo;
    }     
    
    public boolean isSolicitante(){
        return tipo == TicketGroupServiceType.SOLICITANTE;
    }
    
    public boolean isAtendente(){
        return tipo == TicketGroupServiceType.ATENDENTE;
    }
    
}
