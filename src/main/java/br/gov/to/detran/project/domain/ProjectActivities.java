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
import br.gov.to.detran.project.enumeration.ProjectActivitiesType;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_project_activities")
public class ProjectActivities extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
    
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ProjectActivitiesType type;   
    
    @JoinColumn(name = "fk_user")
    @ManyToOne    
    private UserSecurity user;       
    
    @JoinColumn(name = "fk_project")
    @ManyToOne
    private Project project;

	public ProjectActivitiesType getType() {
		return type;
	}

	public void setType(ProjectActivitiesType type) {
		this.type = type;
	}

	public UserSecurity getUser() {
		return user;
	}

	public void setUser(UserSecurity user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
    
}
