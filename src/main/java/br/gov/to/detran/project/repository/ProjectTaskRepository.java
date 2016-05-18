/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.project.domain.ProjectTask;
import br.gov.to.detran.project.domain.QProjectTask;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ProjectTaskRepository extends AbstractRepository<ProjectTask> implements java.io.Serializable {

    @Override
    public EntityPathBase<ProjectTask> getEntityPath() {
        return QProjectTask.projectTask;
    }

	public List<ProjectTask> getAllTasks(Long id) {
		// TODO Auto-generated method stub
		return null;
	}	  

}
