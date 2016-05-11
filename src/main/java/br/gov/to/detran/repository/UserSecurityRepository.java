/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QTicketGroupService;
import br.gov.to.detran.domain.QUserSecurity;
import br.gov.to.detran.domain.QUserSecurityGroup;
import br.gov.to.detran.domain.TicketGroupServiceType;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.UserSecurity;
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
public class UserSecurityRepository extends AbstractRepository<UserSecurity> implements java.io.Serializable {

    @Override
    public EntityPathBase<UserSecurity> getEntityPath() {
        return QUserSecurity.userSecurity;
    }

    public boolean existsCPF(String cpf) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(QUserSecurity.userSecurity.cpf.eq(cpf));
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query = (JPAQueryBase) query.from(QUserSecurity.userSecurity).where(where);
        Long count = query.fetchCount();
        return count != 0;
        
    }
    
    public List<Long> solicitanteIds(UserSecurity solicitante, TicketService service){        
        BooleanBuilder where = new BooleanBuilder();
        where.and(QUserSecurityGroup.userSecurityGroup.userSecurity.id.ne(solicitante.getId()));
        where.and(QUserSecurityGroup.userSecurityGroup
                .ticketGroup.id.in(
                        JPAExpressions.select(QTicketGroupService.ticketGroupService.servico.id)
                                .from(QTicketGroupService.ticketGroupService)
                                .leftJoin(QTicketGroupService.ticketGroupService.servico)
                                .where(new BooleanBuilder().and(QTicketGroupService.ticketGroupService
                                        .tipo.eq(TicketGroupServiceType.ATENDENTE))
                                        .and(QTicketGroupService.ticketGroupService.servico.id.eq(service.getId())))));
        where.and(QUserSecurityGroup.userSecurityGroup.userSecurity.ausente.eq(false));                                      
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QUserSecurityGroup.userSecurityGroup)
                .select(QUserSecurityGroup.userSecurityGroup.userSecurity.id)
                .leftJoin(QUserSecurityGroup.userSecurityGroup.userSecurity)
                .leftJoin(QUserSecurityGroup.userSecurityGroup.ticketGroup)
                .where(where);        
        System.out.println(query.toString());
        return query.fetch();
    }       

}
