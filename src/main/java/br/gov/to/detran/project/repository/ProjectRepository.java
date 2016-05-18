/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.project.domain.Project;
import br.gov.to.detran.project.domain.QProject;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ProjectRepository extends AbstractRepository<Project> implements java.io.Serializable {

    @Override
    public EntityPathBase<Project> getEntityPath() {
        return QProject.project;
    }

	public Project getProject(long parseLong) {
		// TODO Auto-generated method stub
		return null;
	}    

}
