/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.managerfactory;

import br.gov.to.detran.dao.PersistenceDao;
import br.gov.to.detran.domain.AbstractEntity;
import java.lang.reflect.ParameterizedType;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;

/**
 * Classe reponsavel por produzir objetos DAO para os repositories. Necessário para 
 * realizar a injeção das dependencias.
 * @author Maycon
 */
public class DaoFactory {
  @Produces
  public <T extends AbstractEntity> PersistenceDao<T> persistenceDao(InjectionPoint injectionPoint, EntityManager em) {      
    ParameterizedType type = (ParameterizedType) injectionPoint.getType();
    Class classe = (Class) type.getActualTypeArguments()[0];
    return new PersistenceDao(classe, em);
  }    
}
