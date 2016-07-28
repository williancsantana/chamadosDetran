/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.leilao.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.leilao.domain.ItemLeilao;
import br.gov.to.detran.leilao.domain.QItemLeilao;
import br.gov.to.detran.repository.AbstractRepository;

/**
 *
 * @author maycon
 */
@Transactional
public class ItemLeilaoRepository extends AbstractRepository<ItemLeilao> implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;

	@Override
    public EntityPathBase<ItemLeilao> getEntityPath() {
        return QItemLeilao.itemLeilao;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ItemLeilao> getVeiculosDeCategoria(Long id) {				
		JPAQueryBase query = this.getPersistenceDao().query();
	    BooleanBuilder where = new BooleanBuilder();	    
	    where.and(QItemLeilao.itemLeilao.removed.isFalse());
	    where.and(QItemLeilao.itemLeilao.categoria.id.eq(id));
	    query.from(QItemLeilao.itemLeilao).where(where);
	    query.orderBy(QItemLeilao.itemLeilao.placa.asc());
	    return query.fetch();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean existePlacaCadastrada(String placa) {
		BooleanBuilder where = new BooleanBuilder();
		where.and(QItemLeilao.itemLeilao.placa.equalsIgnoreCase(placa));
		where.and(QItemLeilao.itemLeilao.removed.isFalse());				
		JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
		query = (JPAQueryBase) query.from(QItemLeilao.itemLeilao).where(where);
		Long count = query.fetchCount();
		return count != 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean existeChassiCadastrado(String chassi) {
		BooleanBuilder where = new BooleanBuilder();
		where.and(QItemLeilao.itemLeilao.chassi.equalsIgnoreCase(chassi));
		where.and(QItemLeilao.itemLeilao.removed.isFalse());		
		JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
		query = (JPAQueryBase) query.from(QItemLeilao.itemLeilao).where(where);
		Long count = query.fetchCount();
		return count != 0;
	}	

}
