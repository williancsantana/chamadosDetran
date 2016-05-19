/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.project.domain.ProjectUser;
import br.gov.to.detran.project.domain.QProjectUser;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ProjectUserRepository extends AbstractRepository<ProjectUser> implements java.io.Serializable {

    @Override
    public EntityPathBase<ProjectUser> getEntityPath() {
        return QProjectUser.projectUser;
    }

	public List<ProjectUser> getAllUsers(Long id) {
		// TODO Auto-generated method stub
		return null;
	}	  

}
