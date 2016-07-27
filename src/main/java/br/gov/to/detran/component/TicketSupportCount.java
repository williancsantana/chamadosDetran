/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.component;

/**
 *
 * @author maycon
 */
public class TicketSupportCount {
    private Long todos;
    private Long pendentes;
    private Long fechados;
    private Long abertos;
    private Long naoDefinido;
    private Long comLembretes;

    public TicketSupportCount() {
    }

    public TicketSupportCount(Long todos, Long pendentes, Long fechados, Long abertos, Long naoDefinido, Long comLembretes) {
        this.todos = todos;
        this.pendentes = pendentes;
        this.fechados = fechados;
        this.abertos = abertos;
        this.naoDefinido = naoDefinido;
        this.comLembretes = comLembretes;
    }
        
    public Long getComLembretes(){
    	return this.comLembretes;
    }
    public void setComLembretes(Long comLembretes){
    	this.comLembretes = comLembretes;
    }
    public Long getTodos() {
        return todos;
    }

    public void setTodos(Long todos) {
        this.todos = todos;
    }

    public Long getPendentes() {
        return pendentes;
    }

    public void setPendentes(Long pendentes) {
        this.pendentes = pendentes;
    }

    public Long getFechados() {
        return fechados;
    }

    public void setFechados(Long fechados) {
        this.fechados = fechados;
    }

    public Long getAbertos() {
        return abertos;
    }

    public void setAbertos(Long abertos) {
        this.abertos = abertos;
    }

	public Long getNaoDefinido() {
		return naoDefinido;
	}

	public void setNaoDefinido(Long naoDefinido) {
		this.naoDefinido = naoDefinido;
	}       
        
}
