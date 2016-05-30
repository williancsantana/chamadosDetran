/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.project.enumeration.TaskType;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_project_task")
public class ProjectTask extends AbstractEntity{
	
	@Column(name = "titulo", nullable = true)
    private String titulo;	
	
	
	@Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;	
	
	@Column(name = "finalizado", nullable = false)
    private Boolean finalizado = false;
	
    @JoinColumn(name = "fk_user_finalized")
    @ManyToOne    
    private UserSecurity userFinalized;       
    
    @JoinColumn(name = "fk_project")
    @ManyToOne
    private Project project;
    
    @Column(name = "tipo_demanda", nullable = true)
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    public ProjectTask() {
    }

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public UserSecurity getUserFinalized() {
		return userFinalized;
	}

	public void setUserFinalized(UserSecurity userFinalized) {
		this.userFinalized = userFinalized;
	}

	public Boolean getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}			
	
}
