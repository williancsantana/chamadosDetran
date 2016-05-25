/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.project.domain.ProjectTask;
import br.gov.to.detran.project.domain.QProjectComment;
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
		 JPAQueryBase query = this.getPersistenceDao().query();
	        BooleanBuilder where = new BooleanBuilder();
	        where.and(QProjectTask.projectTask.project.id.eq(id));
	        query.from(QProjectTask.projectTask).where(where);
	        query.orderBy(QProjectTask.projectTask.created.asc());
	        return query.fetch();
	}	  

}
