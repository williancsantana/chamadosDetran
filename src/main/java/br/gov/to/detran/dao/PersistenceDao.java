/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.dao;

import br.gov.to.detran.domain.AbstractEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * Dao de Acesso ao banco de dados Generico. Serve para todas os dominios do
 * Sistema
 *
 * @author Maycon
 * @param <T>
 */
public class PersistenceDao<T extends AbstractEntity> implements java.io.Serializable {

    private final EntityManager em;

    protected final Class<T> persistentClass;

    public PersistenceDao(Class<T> persistentClass, EntityManager em) {
        this.persistentClass = persistentClass;
        this.em = em;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    /**
     * Pesquisa por um id
     *
     * @param id
     * @return
     */
    public T findById(Long id) {
        return em.find(this.getPersistentClass(), id);
    }

    /**
     * Pesquisa todos
     *
     * @return
     */
    public List<T> findAll() {
        CriteriaQuery<T> createQuery = em.getCriteriaBuilder().createQuery(persistentClass);
        createQuery.select(createQuery.from(persistentClass));
        return em.createQuery(createQuery).getResultList();
    }

    /**
     * Executa uma criteria do JPA
     *
     * @return
     */
    public Criteria executeCriteria() {
        Session session = ((Session) em.getDelegate());
        Criteria criteria = session.createCriteria(persistentClass);
        return criteria;
    }

    /**
     * Insere um dominio
     *
     * @param entity
     */
    public void insert(T entity) {
        em.persist(entity);
    }

    /**
     * Atualiza um dominio
     *
     * @param entity
     */
    public void update(T entity) {
        em.merge(entity);
    }

    /**
     * Remove um dominio
     *
     * @param entity
     * @throws Exception
     */
    public void delete(T entity) throws Exception {
        try {
            if (em.contains(entity)) {
                em.remove(entity);
            } else {
                em.remove(em.find(persistentClass, entity.getId()));
            }
        } catch (PersistenceException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                throw new Exception("Não é possivel deletar um registro que esteja vinculado.");
            }
            throw e;
        }
    }

    /**
     * Remove uma referencia do banco
     *
     * @param entity
     * @throws Exception
     */
    public void softDelete(T entity) throws Exception {
        entity.setRemoved(Boolean.TRUE);
        entity.setDeletedAt(new Date());
        this.update(entity);
    }

    /**
     * Executa uma query nativa
     *
     * @param stringQuery
     * @param parameters
     */
    @Transactional
    public void executeRawQuery(String stringQuery, HashMap<String, Object> parameters) {
        Query query = em.createQuery(stringQuery);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        query.executeUpdate();
    }

    @Transactional
    public void refresh(T instance) {
        this.em.refresh(instance);
    }

    /**
     * Salva um lista
     *
     * @param entities
     */
    public void saveList(List<T> entities) {
        for (T entity : entities) {
            this.insert(entity);
        }
    }

    /**
     * Cria uma instancia de Query JPA
     *
     * @return
     */
    public JPAQuery query() {
        JPAQuery query = new JPAQuery(this.em);
        return query;
    }

    @Transactional
    public JPAUpdateClause updateQuery(EntityPath path) {
        JPAUpdateClause update = new JPAUpdateClause(em, path);
        return update;
    }

    /**
     * Pega um campo a partir de um string
     *
     * @param property
     * @return
     */
    public Field getFieldFrom(String property) {
        String[] properties = property.split("\\.");
        Class clazz = persistentClass;
        Field field = null;
        try {
            for (String prop : properties) {
                if (field != null) {
                    clazz = field.getType();
                }
                field = clazz.getDeclaredField(prop);
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(PersistenceDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return field;
    }

    /**
     * Logica para realizar a pesquisa, ordenação e paginação de resultados para
     * um datatable
     *
     * @param entity
     * @param first
     * @param pageSize
     * @param sortField
     * @param order
     * @param values
     * @return
     */
    public LazyResult<T> lazyLoad(EntityPathBase<T> entity, int first, int pageSize, String sortField, String order, Map<String, Object> values) {
        BooleanBuilder where = new BooleanBuilder();
        return lazyLoad(null, entity, first, pageSize, sortField, order, values, where, false);
    }

    public LazyResult<T> lazyLoad(EntityPathBase<T> entity, int first, int pageSize, String sortField, String order, Map<String, Object> values, BooleanBuilder where, Boolean or) {
        return lazyLoad(null, entity, first, pageSize, sortField, order, values, where, or);
    }

    public LazyResult<T> lazyLoad(Map<String, Object> fkEntities, EntityPathBase<T> entity, int first, int pageSize, String sortField, String order, Map<String, Object> values, BooleanBuilder where, Boolean or) {
        this.applyFilter(entity, fkEntities, where, false);
        this.applyFilter(entity, values, where, or);
        JPAQueryBase queryCount = (JPAQueryBase) this.query().from(entity).where(where);
        Long count = queryCount.fetchCount();
        JPAQueryBase query = (JPAQueryBase) this.query().from(entity).where(where).offset(first).limit(pageSize);
        if (sortField != null && !sortField.isEmpty()) {
            query.orderBy(new OrderSpecifier(order.equalsIgnoreCase("ASCENDING") ? Order.ASC : Order.DESC, Expressions.stringPath(entity, sortField)));
        }
        List<T> result = query.fetch();
        return new LazyResult<>(result, count);
    }

    private void applyFilter(EntityPathBase<T> entity, Map<String, Object> attrs, BooleanBuilder where, Boolean or) {
        if (attrs != null && !attrs.isEmpty()) {
            for (String filterProperty : attrs.keySet()) {
                Field field = this.getFieldFrom(filterProperty);
                if (field != null) {
                    String filterValue = String.valueOf(attrs.get(filterProperty));
                    SimplePath path = Expressions.path(field.getType(), entity, filterProperty);
                    StringExpression template = Expressions.stringTemplate("cast({0} as text)", path);
                    if (or) {
                        where.or(template.containsIgnoreCase(filterValue));
                    } else {
                        where.and(template.containsIgnoreCase(filterValue));
                    }
                }
            }
        }
    }

    public T queryFromField(EntityPathBase<T> entity, String fieldProperty, String filterValue) {
        BooleanBuilder where = new BooleanBuilder();
        Field field = this.getFieldFrom(fieldProperty);
        if (field != null) {
            SimplePath path = Expressions.path(field.getType(), entity, fieldProperty);
            where.and(path.eq(filterValue));
        }
        JPAQueryBase query = this.query();
        query = (JPAQueryBase) query.from(entity).where(where);
        return (T) query.fetchFirst();        
    }

    public EntityManager getEntityManager() {
        return this.em;
    }

    public void flush() {
        this.em.flush();
    }

}
