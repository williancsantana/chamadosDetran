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

import br.gov.to.detran.domain.QTicketReply;
import br.gov.to.detran.project.domain.ProjectComment;
import br.gov.to.detran.project.domain.QProjectComment;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ProjectCommentRepository extends AbstractRepository<ProjectComment> implements java.io.Serializable {

    @Override
    public EntityPathBase<ProjectComment> getEntityPath() {
        return QProjectComment.projectComment;
    }

	public List<ProjectComment> getAllComments(Long id) {
		 JPAQueryBase query = this.getPersistenceDao().query();
	        BooleanBuilder where = new BooleanBuilder();
	        where.and(QProjectComment.projectComment.project.id.eq(id));
	        where.and(QProjectComment.projectComment.removed.isFalse());
	        query.from(QProjectComment.projectComment).where(where);
	        query.orderBy(QProjectComment.projectComment.created.desc());
	        return query.fetch();
	}    

}
