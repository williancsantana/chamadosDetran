/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQuery;

import br.gov.to.detran.managerfactory.DetranNET;

/**
 *
 * @author maycon
 */
public abstract class AbstractRepositoryDetranNET{
    
    private @Inject @DetranNET
    EntityManager em;
             
    
    @SuppressWarnings("rawtypes")
	public JPAQuery query() {
        JPAQuery query = new JPAQuery(this.em);
        return query;
    }


	public EntityManager getEm() {
		return em;
	}


	public void setEm(EntityManager em) {
		this.em = em;
	}       
       
}
