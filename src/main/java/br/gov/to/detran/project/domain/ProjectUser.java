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
import br.gov.to.detran.project.enumeration.ProjectUserType;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_projeto_usuario")
public class ProjectUser extends AbstractEntity{
    @JoinColumn(name = "fk_user")
    @ManyToOne    
    private UserSecurity userSecurity;
    
    @JoinColumn(name = "fk_project")
    @ManyToOne
    private Project project;
        
	public UserSecurity getUserSecurity() {
		return userSecurity;
	}

	public void setUserSecurity(UserSecurity userSecurity) {
		this.userSecurity = userSecurity;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}  	
    
}


