/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.types.dsl.EntityPathBase;

import br.gov.to.detran.project.domain.ProjectActivities;
import br.gov.to.detran.project.domain.QProjectActivities;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ProjectActivitiesRepository extends AbstractRepository<ProjectActivities> implements java.io.Serializable {

    @Override
    public EntityPathBase<ProjectActivities> getEntityPath() {
        return QProjectActivities.projectActivities;
    }

	public List<ProjectActivities> getAllActivities(Long id) {
		return null;
	}
	

}
