/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.project.enumeration.ProjectStatus;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_project")
public class Project extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
    
    @Column(name = "titulo", nullable = false, length = 4000)
    private String titulo;
    
    @Column(name = "tags", nullable = false, length = 4000)
    private String tags;
    
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;       
       
    @JoinColumn(name = "fk_autor", nullable = false)
    @ManyToOne
    private UserSecurity autor;
    
    @JoinColumn(name = "fk_coordinator", nullable = false)
    @ManyToOne
    private UserSecurity coordinator;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;       
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_estimated", nullable = true)
    private Date timeEstimated;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date", nullable = true)
    private Date startDate;
       
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectUser> peoples = new ArrayList<>();
        
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectComment> comments = new ArrayList<>();                  
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectDocument> documents = new ArrayList<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectTask> tasks = new ArrayList<>();

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UserSecurity getAutor() {
		return autor;
	}

	public void setAutor(UserSecurity autor) {
		this.autor = autor;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public Date getTimeEstimated() {
		return timeEstimated;
	}

	public void setTimeEstimated(Date timeEstimated) {
		this.timeEstimated = timeEstimated;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<ProjectUser> getPeoples() {
		return peoples;
	}

	public void setPeoples(List<ProjectUser> peoples) {
		this.peoples = peoples;
	}

	public List<ProjectComment> getComments() {
		return comments;
	}

	public void setComments(List<ProjectComment> comments) {
		this.comments = comments;
	}

	public List<ProjectDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ProjectDocument> documents) {
		this.documents = documents;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<ProjectTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ProjectTask> projectTask) {
		this.tasks = projectTask;
	}

	public UserSecurity getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(UserSecurity coordinator) {
		this.coordinator = coordinator;
	}	   	
}
