package br.gov.to.detran.repository;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.domain.QSetorAtendimento;
import br.gov.to.detran.domain.SetorAtendimento;

/**
 *
 * @author maycon
 */
@Transactional
public class SetorAtendimentoRepository extends AbstractRepository<SetorAtendimento> implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;

	@Override
    public EntityPathBase<SetorAtendimento> getEntityPath() {
        return QSetorAtendimento.setorAtendimento;
    }   
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean existe(Integer idSetor, Long instanceId) {
		BooleanBuilder where = new BooleanBuilder();
		where.and(QSetorAtendimento.setorAtendimento.idSetor.eq(idSetor));		
		where.and(QSetorAtendimento.setorAtendimento.removed.isFalse());
		if(instanceId != null && instanceId != 0){
			where.and(QSetorAtendimento.setorAtendimento.id.ne(instanceId));
		}		
		JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
		query = (JPAQueryBase) query.from(QSetorAtendimento.setorAtendimento).where(where);
		Long count = query.fetchCount();
		return count != 0;
	}
    
}