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
public class ProjectCount {
    private Long todos;
    private Long progresso;
    private Long homologacao;
    private Long abertos;
    private Long producao;
    private Long finalizado;
    private Long cancelados;
    private Long pausados;

    public ProjectCount() {
    }
    
	public ProjectCount(Long todos, Long progresso, Long homologacao, Long abertos, Long producao, Long finalizado,
			Long cancelados, Long pausados) {	
		this.todos = todos;
		this.progresso = progresso;
		this.homologacao = homologacao;
		this.abertos = abertos;
		this.producao = producao;
		this.finalizado = finalizado;
		this.cancelados = cancelados;
		this.pausados = pausados;
	}
	
	public Long getCancelados() {
		return cancelados;
	}

	public void setCancelados(Long cancelados) {
		this.cancelados = cancelados;
	}

	public Long getPausados() {
		return pausados;
	}

	public void setPausados(Long pausados) {
		this.pausados = pausados;
	}



	public Long getTodos() {
		return todos;
	}

	public void setTodos(Long todos) {
		this.todos = todos;
	}

	public Long getProgresso() {
		return progresso;
	}

	public void setProgresso(Long progresso) {
		this.progresso = progresso;
	}

	public Long getHomologacao() {
		return homologacao;
	}

	public void setHomologacao(Long homologacao) {
		this.homologacao = homologacao;
	}

	public Long getAbertos() {
		return abertos;
	}

	public void setAbertos(Long abertos) {
		this.abertos = abertos;
	}

	public Long getProducao() {
		return producao;
	}

	public void setProducao(Long producao) {
		this.producao = producao;
	}

	public Long getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Long finalizado) {
		this.finalizado = finalizado;
	}          
        
}
