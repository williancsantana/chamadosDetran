/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QTicketGroup;
import br.gov.to.detran.domain.TicketGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAQueryBase;
import java.util.List;
import javax.transaction.Transactional;

/**
 *
 * @author maycon
 */
@Transactional
public class TicketGroupRepository extends AbstractRepository<TicketGroup> implements java.io.Serializable {

    @Override
    public EntityPathBase<TicketGroup> getEntityPath() {
        return QTicketGroup.ticketGroup;
    }

    public List<TicketGroup> getGroupsAtendenteAndSolicitantes() {
         BooleanBuilder where = new BooleanBuilder();
         StringExpression template = Expressions.stringTemplate("cast({0} as text)", QTicketGroup.ticketGroup.permissoes);
                where.and(template.containsIgnoreCase("chamado:abrir")).or(template.containsIgnoreCase("chamado:atender"));                                
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketGroup.ticketGroup)
                .where(where).orderBy(QTicketGroup.ticketGroup.descricao.asc());
        return (List<TicketGroup>) query.fetch();
    }       

}
