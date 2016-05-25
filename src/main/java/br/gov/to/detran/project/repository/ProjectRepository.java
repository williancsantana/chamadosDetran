/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.domain.QTicketSupport;
import br.gov.to.detran.domain.TicketSupport;
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

	public Project getProject(Long id) {
		 System.out.println("id: "  + id);
	        BooleanBuilder where = new BooleanBuilder();
	        where.and(QProject.project.id.eq(id));
	        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
	        query.from(QProject.project)
	        .leftJoin(QProject.project.peoples)
	        .leftJoin(QProject.project.documents)
	        .leftJoin(QProject.project.tasks)
	        .leftJoin(QProject.project.comments)
	        .where(where);
	        return (Project) query.fetchFirst();
	}    

}
