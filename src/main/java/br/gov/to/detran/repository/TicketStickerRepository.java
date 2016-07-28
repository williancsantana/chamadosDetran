package br.gov.to.detran.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import br.gov.to.detran.domain.QTicketGroup;
import br.gov.to.detran.domain.QTicketStickerSupport;
import br.gov.to.detran.domain.TicketGroup;
import br.gov.to.detran.domain.TicketStickerSupport;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;

import com.google.common.base.Ticker;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAQueryBase;

@Transactional
public class TicketStickerRepository extends AbstractRepository<TicketStickerSupport> implements java.io.Serializable{

	@Override
	public EntityPathBase<TicketStickerSupport> getEntityPath() {
		return QTicketStickerSupport.ticketStickerSupport;
	}
	
	public List<TicketStickerSupport> getLembretesAtendente(UserSecurity atendente){
		 BooleanBuilder where = new BooleanBuilder();
         where.andNot(QTicketStickerSupport.ticketStickerSupport.ticketSupport.status.eq(TicketSupportStatus.FECHADO))
                	.and(QTicketStickerSupport.ticketStickerSupport.horaLembrete.before(new Date()))
                	.and(QTicketStickerSupport.ticketStickerSupport.ticketSupport.atendente.id.eq(atendente.getId()));                                
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketStickerSupport.ticketStickerSupport)
                .where(where);
        System.out.println("Essa query:"+query);
        return (List<TicketStickerSupport>) query.fetch();
	}
	
	public TicketStickerSupport getLembreteChamado(TicketSupport chamado){
		BooleanBuilder where = new BooleanBuilder();
		where.and(QTicketStickerSupport.ticketStickerSupport.ticketSupport.id.eq(chamado.getId()));
		JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
		query.from(QTicketStickerSupport.ticketStickerSupport).where(where);
		return (TicketStickerSupport)query.fetchFirst();
	}
}
