/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.leilao.repository;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.leilao.domain.Categoria;
import br.gov.to.detran.leilao.domain.QCategoria;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class CategoriaRepository extends AbstractRepository<Categoria> implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;

	@Override
    public EntityPathBase<Categoria> getEntityPath() {
        return QCategoria.categoria;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Categoria getCategoria(long id) {
		BooleanBuilder where = new BooleanBuilder();
		where.and(QCategoria.categoria.id.eq(id));
		where.and(QCategoria.categoria.removed.isFalse());		
		JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
		query.from(QCategoria.categoria);
		query.where(where);
		return (Categoria) query.fetchFirst();
	}

}
