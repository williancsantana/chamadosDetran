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

import br.gov.to.detran.project.domain.ProjectUser;
import br.gov.to.detran.project.domain.QProjectComment;
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
		 JPAQueryBase query = this.getPersistenceDao().query();
	        BooleanBuilder where = new BooleanBuilder();
	        where.and(QProjectUser.projectUser.project.id.eq(id));
	        query.from(QProjectUser.projectUser).where(where);
	        query.orderBy(QProjectUser.projectUser.created.asc());
	        return query.fetch();
	}	  

}
