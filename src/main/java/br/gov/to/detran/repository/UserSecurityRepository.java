/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.domain.QEscalaTrabalho;
import br.gov.to.detran.domain.QTicketGroupService;
import br.gov.to.detran.domain.QUserSecurity;
import br.gov.to.detran.domain.QUserSecurityGroup;
import br.gov.to.detran.domain.TicketGroupServiceType;
import br.gov.to.detran.domain.TicketService;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.enumeration.DiaSemana;

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
    
    public List<Long> solicitanteIds(UserSecurity solicitante, TicketService service, DiaSemana diaSemana, Date hora){
    	System.out.println("Parametros\n================================");
    	System.out.println("Solicitante: " + solicitante.getId());
    	System.out.println("Service: " + service.getId());
    	System.out.println("Dia Semana: " + diaSemana.getLabel());
    	System.out.println("Data: " + new SimpleDateFormat("dd/MM/yyyy").format(hora));
    	QEscalaTrabalho qEscala = QEscalaTrabalho.escalaTrabalho;
        BooleanBuilder where = new BooleanBuilder();
        where.and(QUserSecurityGroup.userSecurityGroup.userSecurity.id.ne(solicitante.getId()));
        where.and(QUserSecurityGroup.userSecurityGroup
                .ticketGroup.id.in(
                        JPAExpressions.select(QTicketGroupService.ticketGroupService.grupo.id)
                                .from(QTicketGroupService.ticketGroupService)
                                .leftJoin(QTicketGroupService.ticketGroupService.servico)
                                .where(new BooleanBuilder().and(QTicketGroupService.ticketGroupService
                                        .tipo.eq(TicketGroupServiceType.ATENDENTE))
                                        .and(QTicketGroupService.ticketGroupService.servico.id.eq(service.getId())))));
        where.and(QUserSecurityGroup.userSecurityGroup.userSecurity.ausente.eq(false));
        
        BooleanBuilder expressaoHora = new BooleanBuilder(qEscala.diaSemana.eq(diaSemana).and(
        		qEscala.horaInicial.hour().loe(hora.getHours())).and(qEscala.horaFinal.hour().goe(hora.getHours())));
        /*
        BooleanBuilder expressaoHora2 = new BooleanBuilder(qEscala.escalaTrabalho.isNull());
        expressaoHora2.orAllOf(com.mysema.query.types.expr.BooleanExpression.allOf());*/
                
        where.andAnyOf(qEscala.escalaTrabalho.isNull(), expressaoHora);
        /*qEscala.diaSemana.eq(diaSemana)).andAnyOf(qEscala.diaSemana.eq(diaSemana).and(
                		qEscala.horaInicial.hour().goe(hora.getHours())).and(qEscala.horaFinal.hour().loe(hora.getHours())));
        //new BooleanBuilder().orAllOf(qEscala.diaSemana.eq(diaSemana).and(
        //		qEscala.horaInicial.hour().goe(hora.getHours())).and(qEscala.horaFinal.hour().loe(hora.getHours())))));
       /* where.andAnyOf(qEscala.escalaTrabalho.isNull(), new BooleanBuilder().orAllOf(qEscala.diaSemana.eq(diaSemana),
        		qEscala.horaInicial.hour().goe(hora.getHours()), qEscala.horaFinal.hour().loe(hora.getHours()))).getValue();*/
        		/*.orAllOf(qEscala.diaSemana.eq(diaSemana),
                		qEscala.horaInicial.hour().goe(hora.getHours()), qEscala.horaFinal.hour().loe(hora.getHours()));*/
/*        where.andAnyOf(
    	.or(qEscala.diaSemana.eq(diaSemana).andAnyOf((qEscala.diaSemana.eq(diaSemana))
    	    	.and(qEscala.horaInicial.goe(hora)).and(qEscala.horaFinal.loe(hora)))));*/        
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QUserSecurityGroup.userSecurityGroup)
                .select(QUserSecurityGroup.userSecurityGroup.userSecurity.id)                
                .leftJoin(QUserSecurityGroup.userSecurityGroup.userSecurity)
                .leftJoin(QUserSecurityGroup.userSecurityGroup.ticketGroup)
                .leftJoin(QUserSecurityGroup.userSecurityGroup.userSecurity.escalaDeTrabalho, qEscala)
                .where(where);     
        query.distinct();
        System.out.println(query.toString());
        return query.fetch();
    }       

}
