/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

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
		// TODO Auto-generated method stub
		return null;
	}    

}
