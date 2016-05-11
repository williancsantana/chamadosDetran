/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.managerfactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe responsavel por produzir entity manager. Necessária para realizar
 * a injeção do entity manager em outras classes.
 * @author Maycon
 */
@RequestScoped
public class ManagerFactory implements java.io.Serializable {
    
    @PersistenceContext(unitName="application")
    EntityManager em;
    
    @PersistenceContext(unitName="detran-erp")
    EntityManager detranErp;

    @Produces @RequestScoped @Default
    public EntityManager getEntityManager() {
        return em;
    }
        
    @Produces @RequestScoped @DetranERP
    public EntityManager getDetranErpEntityManager() {
        return detranErp;
    }
    
           
}
