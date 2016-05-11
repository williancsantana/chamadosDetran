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
@Table(name = "tb_chamado_resposta")
public class TicketReply extends AbstractEntity{
    @Column(name = "mensagem", nullable = false, columnDefinition = "TEXT")
    private String mensagem;
    
    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketReplyType tipo;
    
    @JoinColumn(name = "fk_autor")
    @ManyToOne
    private UserSecurity autor;
    
    @JoinColumn(name = "fk_chamado")
    @ManyToOne
    private TicketSupport chamado;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TicketReplyType getTipo() {
        return tipo;
    }

    public void setTipo(TicketReplyType tipo) {
        this.tipo = tipo;
    }

    public UserSecurity getAutor() {
        return autor;
    }

    public void setAutor(UserSecurity autor) {
        this.autor = autor;
    }

    public TicketSupport getChamado() {
        return chamado;
    }

    public void setChamado(TicketSupport chamado) {
        this.chamado = chamado;
    }        
    
    public String titleMensagem(){
         switch(mensagem) {
            case "ABERTO":
                return "Chamado aberto";
            case "REABERTO":
                return "Chamado reaberto";
            case "PENDENTE_USUARIO":
                return "Chamado pendente com o solicitante";
            case "PENDENTE_TERCEIROS":
                return "Chamado pendente com terceiros";
            case "PENDENCIA TERCEIROS RESOLVIDA":
                return "Pendência com terceiros resolvida";
            case "PENDENCIA USUARIO REMOVIDA":
                return "Pendência com solicitante removida";
            case "FECHADO":
                return "Chamado fechado";
            case "ESCALONAMENTO":
                return "Chamado escalonado para outro atendente";
            case "APROPRIACAO":
                return "Chamado apropriado por outro atendente";
        }
         return "";
    }
    
     public String iconMensagem() {
        switch(mensagem) {
            case "ABERTO":
                return "comments outline";
            case "REABERTO":
                return "refresh";
            case "PENDENTE_USUARIO":
            case "PENDENTE_TERCEIROS":
                return "warning sign";
            case "PENDENCIA TERCEIROS RESOLVIDA":
                return "checkmark";
            case "PENDENCIA USUARIO REMOVIDA":
                return "comments outline";
            case "FECHADO":
                return "ban";
            case "ESCALONAMENTO":
                return "level up";
            case "APROPRIACAO":
                return "level down";
        }
        return "";
    }
     
    public String colorMensagem() {
        switch(mensagem) {
            case "ABERTO":
            case "REABERTO":
            case "PENDENCIA TERCEIROS RESOLVIDA":
            case "PENDENCIA USUARIO REMOVIDA":
                return "green";
            case "PENDENTE_USUARIO":
            case "PENDENTE_TERCEIROS":
                return "orange";
            case "FECHADO":
                return "red";
            case "ESCALONAMENTO":
            case "APROPRIACAO":
                return "blue";
        }
        return "";
    }
    
}
