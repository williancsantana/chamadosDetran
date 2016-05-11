/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QTicketService;
import br.gov.to.detran.domain.TicketService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
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
    
    public List<String> getCategories(){        
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();        
        query.from(QTicketService.ticketService)
                .select(QTicketService.ticketService.categoria)
                .distinct();
        return (List<String>) query.fetch();
    }

    public List<TicketService> getServicesFilter(String categoriePath) {
        BooleanBuilder where = new BooleanBuilder();        
        if(categoriePath != null && !categoriePath.isEmpty()){
            where.and(QTicketService.ticketService.categoria.startsWith(categoriePath));
        }
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketService.ticketService)
                .where(where).orderBy(QTicketService.ticketService.descricao.asc());
        return (List<TicketService>) query.fetch();
    }

}
