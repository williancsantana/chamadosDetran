/*
 * To change this license header, choose License Headers in ProjectDocument Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.project.domain.ProjectDocument;
import br.gov.to.detran.project.domain.QProjectDocument;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ProjectDocumentRepository extends AbstractRepository<ProjectDocument> implements java.io.Serializable {

    @Override
    public EntityPathBase<ProjectDocument> getEntityPath() {
        return QProjectDocument.projectDocument;
    }

	public List<ProjectDocument> getAllDocuments(Long id) {
		// TODO Auto-generated method stub
		return null;
	}    

}
