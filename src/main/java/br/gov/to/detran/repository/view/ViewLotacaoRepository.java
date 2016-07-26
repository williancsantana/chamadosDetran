package br.gov.to.detran.repository.view;

import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;

import br.gov.to.detran.domain.view.QViewLotacao;
import br.gov.to.detran.domain.view.ViewLotacao;
import br.gov.to.detran.repository.AbstractRepositoryDetranErp;



@Transactional
public class ViewLotacaoRepository extends AbstractRepositoryDetranErp<ViewLotacao> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Override
    public EntityPathBase<ViewLotacao> getEntityPath() {
        return QViewLotacao.viewLotacao;
    }

	public List<ViewLotacao> buscarLotacao(String texto) {		
		BooleanBuilder where = new BooleanBuilder();
        where.and(QViewLotacao.viewLotacao.nome.startsWithIgnoreCase(texto));
        where.and(QViewLotacao.viewLotacao.pai.isNotNull());
        
        JPAQuery query = (JPAQuery) this.query();
        query = (JPAQuery) query.from(QViewLotacao.viewLotacao).where(where);               
        return query.fetch();		
	}

	public ViewLotacao buscarLotacaoId(Long setorId) {
		BooleanBuilder where = new BooleanBuilder();
        where.and(QViewLotacao.viewLotacao.id.eq(setorId));        
        
        JPAQuery query = (JPAQuery) this.query();
        query = (JPAQuery) query.from(QViewLotacao.viewLotacao).where(where);               
        return (ViewLotacao) query.fetchFirst();	
	}
    
}
