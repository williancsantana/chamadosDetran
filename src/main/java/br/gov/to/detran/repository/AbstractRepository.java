/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.dao.LazyResult;
import br.gov.to.detran.dao.PersistenceDao;
import br.gov.to.detran.domain.AbstractEntity;
import br.gov.to.detran.leilao.domain.ItemLeilao;
import br.gov.to.detran.util.LanguageResource;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 *
 * @author maycon
 */
public abstract class AbstractRepository<T extends AbstractEntity> implements Repository<T> {

    private @Inject
    LanguageResource lanRes;
    private @Inject
    PersistenceDao<T> persistenceDao;

    public abstract EntityPathBase<T> getEntityPath();

    public void insert(T entity) throws Exception {
        try {
            this.persistenceDao.insert(entity);
        } catch (NullPointerException e) {
            throw new Exception("Campos Obrigatorios não foram preenchidos!");
        }
    }

    public void update(T entity) throws Exception {
        try {
            this.persistenceDao.update(entity);
        } catch (NullPointerException e) {
            throw new Exception("Campos Obrigatorios não foram preenchidos!");
        }
    }
    
    public void remove(T entity) throws Exception {
        this.remove(entity, false);
    }

    public void remove(T entity, boolean softDelete) throws Exception {
        try {
            if (entity.getId() > 0) {
            	if(!softDelete){
            		this.persistenceDao.delete(this.persistenceDao.findById(entity.getId()));
            	}else{
            		this.persistenceDao.softDelete(entity);
            	}            	            	
            }
        } catch (NullPointerException e) {
            throw new Exception("Campos Obrigatorios não foram preenchidos!");
        }
    }
    
    public JPAUpdateClause updateQuery(){
        return this.persistenceDao.updateQuery(this.getEntityPath());
    }
    
    public void flush(){
        this.persistenceDao.flush();
    }
    
    public void refresh(T instance){
        this.persistenceDao.refresh(instance);
    }

    public List<T> getAll() {
        return persistenceDao.findAll();
    }

    public T searchByField(String field, String value) {
        return this.persistenceDao.queryFromField(this.getEntityPath(), field, value);
    }

    @Override
    public LazyResult<T> lazyLoad(int first, int pageSize, String sortField, String order, Map<String, Object> values) {
        return this.persistenceDao.lazyLoad(this.getEntityPath(), first, pageSize, sortField, order, values);
    }
    
    @Override
    public LazyResult<T> lazyLoad(Map<String, Object> fkEntities, int first, int pageSize, String sortField, String order, Map<String, Object> values) {
        return this.persistenceDao.lazyLoad(fkEntities, this.getEntityPath(), first, pageSize, sortField, order, values, new BooleanBuilder(), false);
    }
    
    @Override
    public LazyResult<T> lazyLoad(int first, int pageSize, String sortField, String order, Map<String, Object> values, BooleanBuilder where, Boolean or) {
        return this.persistenceDao.lazyLoad(this.getEntityPath(), first, pageSize, sortField, order, values, where, or);
    }
    
    @Override
	public LazyResult<T> lazyLoad3(List<Predicate> predicates, int first, int pageSize, String sortField, String order,
			Map<String, Object> values) {
    	return this.persistenceDao.lazyLoad3(predicates, this.getEntityPath(), first, pageSize, sortField, order, values);
	}

    @Override
    public Class<T> getPersitenceClass() {
        return this.persistenceDao.getPersistentClass();
    }

    @Override
    public T getInstancePorId(Long id) throws Exception {
        return this.persistenceDao.findById(id);
    }

    public PersistenceDao<T> getPersistenceDao() {
        return persistenceDao;
    }

    public void setPersistenceDao(PersistenceDao<T> persistenceDao) {
        this.persistenceDao = persistenceDao;
    }        
}
