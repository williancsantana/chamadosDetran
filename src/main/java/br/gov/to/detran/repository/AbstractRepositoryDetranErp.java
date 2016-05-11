/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.managerfactory.DetranERP;
import br.gov.to.detran.util.LanguageResource;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

/**
 *
 * @author maycon
 */
public abstract class AbstractRepositoryDetranErp<T>{

    private @Inject
    LanguageResource lanRes;
    private @Inject @DetranERP
    EntityManager em;
    
    public abstract EntityPathBase<T> getEntityPath();      
    
    public JPAQuery query() {
        JPAQuery query = new JPAQuery(this.em);
        return query;
    }
    
    public void print(){
        for (EntityType<?> entity : em.getMetamodel().getEntities()) {
            final String className = entity.getName();
            System.out.println(className);
        }
    }
}
