/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QTicketGroupService;
import br.gov.to.detran.domain.QTicketService;
import br.gov.to.detran.domain.QUserSecurityGroup;
import br.gov.to.detran.domain.TicketGroupServiceType;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.util.FacesUtil;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPAQueryBase;
import java.util.List;
import javax.transaction.Transactional;

/**
 *
 * @author maycon
 */
@Transactional
public class TicketServiceRepository extends AbstractRepository<TicketService> implements java.io.Serializable {

    @Override
    public EntityPathBase<TicketService> getEntityPath() {
        return QTicketService.ticketService;
    }
    
    public List<String> getCategories(Long userId){     
    	BooleanBuilder where = new BooleanBuilder();
    	where.and(QTicketGroupService.ticketGroupService.grupo.id.in(
                JPAExpressions.select(QUserSecurityGroup.userSecurityGroup.ticketGroup.id)
                        .from(QUserSecurityGroup.userSecurityGroup)
                        .where(QUserSecurityGroup.userSecurityGroup.userSecurity.id.eq(userId))
                )).or(QTicketGroupService.ticketGroupService.servico.padrao.isTrue());
        where.and(QTicketGroupService.ticketGroupService.tipo.eq(TicketGroupServiceType.SOLICITANTE));
        where.and(QTicketGroupService.ticketGroupService.servico.removed.isFalse());
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketGroupService.ticketGroupService)
        		.where(where);
        query.select(QTicketGroupService.ticketGroupService.servico.categoria)
        	 .distinct();
        return (List<String>) query.fetch();
        
    }

    public List<TicketService> getServicesFilter(String categoriePath, Long userId) {    	
        BooleanBuilder where = new BooleanBuilder();
        if(categoriePath != null && !categoriePath.isEmpty()){
            where.and(QTicketGroupService.ticketGroupService.servico.categoria.startsWith(categoriePath));
        }
        where.and(QTicketGroupService.ticketGroupService.grupo.id.in(
                JPAExpressions.select(QUserSecurityGroup.userSecurityGroup.ticketGroup.id)
                        .from(QUserSecurityGroup.userSecurityGroup)
                        .where(QUserSecurityGroup.userSecurityGroup.userSecurity.id.eq(userId))
                ).or(QTicketGroupService.ticketGroupService.servico.padrao.isTrue()));
        where.and(QTicketGroupService.ticketGroupService.tipo.eq(TicketGroupServiceType.SOLICITANTE));
        where.and(QTicketGroupService.ticketGroupService.servico.removed.isFalse());
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketGroupService.ticketGroupService)
        		.where(where).orderBy(QTicketGroupService.ticketGroupService.servico.descricao.asc());
        query.select(QTicketGroupService.ticketGroupService.servico);   
        query.distinct();
        return (List<TicketService>) query.fetch();
    	
    	
        /*BooleanBuilder where = new BooleanBuilder();        
        if(categoriePath != null && !categoriePath.isEmpty()){
            where.and(QTicketService.ticketService.categoria.startsWith(categoriePath));
        }
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketService.ticketService)
                .where(where).orderBy(QTicketService.ticketService.descricao.asc());
        return (List<TicketService>) query.fetch();*/
    }

	public List<UserSecurity> getPossibleAttendants(TicketService servico) {
		BooleanBuilder where = new BooleanBuilder();        
        where.and(QUserSecurityGroup.userSecurityGroup.ticketGroup.id.in(        		
                JPAExpressions.select(QTicketGroupService.ticketGroupService.grupo.id)
                        .from(QTicketGroupService.ticketGroupService)
                        .where(QTicketGroupService.ticketGroupService.servico.id.eq(servico.getId())
                        		.and(QTicketGroupService.ticketGroupService.tipo.eq(TicketGroupServiceType.ATENDENTE)))
                ));                               
        where.and(QUserSecurityGroup.userSecurityGroup.ticketGroup.removed.isFalse());        
        where.and(QUserSecurityGroup.userSecurityGroup.userSecurity.removed.isFalse());
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();        
        query.from(QUserSecurityGroup.userSecurityGroup)
        		.where(where).orderBy(QUserSecurityGroup.userSecurityGroup.userSecurity.name.asc());
        query.select(QUserSecurityGroup.userSecurityGroup.userSecurity);   
        query.distinct();
        return (List<UserSecurity>) query.fetch();
	}

}
