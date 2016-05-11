/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.dao.LazyResult;
import br.gov.to.detran.domain.AbstractEntity;
import com.querydsl.core.BooleanBuilder;
import java.util.Map;

/**
 * Interface que define um repositorio
 * @author Maycon
 * @param <T>
 */
public interface Repository<T extends AbstractEntity> {                
    /**
     * Retorna o Objeto Class do dominio
     * @return 
     */
    public Class getPersitenceClass();
    /**
     * Retorna uma instancia do dominio pelo seu id
     * @param id
     * @return
     * @throws Exception 
     */
    public T getInstancePorId(Long id) throws Exception;
    /**
     * Função usado para paginação das tabelas do primefaces
     * @param first
     * @param pageSize
     * @param sortField
     * @param order
     * @param values
     * @return 
     */
    public LazyResult<T> lazyLoad(int first, int pageSize, String sortField, String order, Map<String, Object> values);
    
    public LazyResult<T> lazyLoad(Map<String, Object> fkEntities, int first, int pageSize, String sortField, String order, Map<String, Object> values);
        
    public LazyResult<T> lazyLoad(int first, int pageSize, String sortField, String order, Map<String, Object> values, BooleanBuilder where, Boolean or);    
}
