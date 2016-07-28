/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository.view;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.domain.QTicketGroupService;
import br.gov.to.detran.domain.QUserSecurityGroup;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.view.QViewTicketSupport;
import br.gov.to.detran.domain.view.ViewTicketSupport;
import br.gov.to.detran.repository.AbstractRepository;
import br.gov.to.detran.util.FacesUtil;

/**
 *
 * @author maycon
 */
@Transactional
public class ViewTicketSupportRepository extends AbstractRepository<ViewTicketSupport> implements java.io.Serializable {
    	
	private static final long serialVersionUID = 1L;	
    
    @Override
    public EntityPathBase<ViewTicketSupport> getEntityPath() {
        return QViewTicketSupport.viewTicketSupport;
    }
    
    private BooleanBuilder getDefaultWhere(){
        UserSecurity userOnline = FacesUtil.loggedUser();
        BooleanBuilder where = new BooleanBuilder();
        where.and(QViewTicketSupport.viewTicketSupport.idSolicitante.eq(userOnline.getId()).or(QViewTicketSupport.viewTicketSupport.idAtendente.eq(userOnline.getId())));
        where.and(QViewTicketSupport.viewTicketSupport.idSolicitante.eq(userOnline.getId())
                .and(QViewTicketSupport.viewTicketSupport.solViewat.isNull().or(QViewTicketSupport.viewTicketSupport.solViewat.before(QViewTicketSupport.viewTicketSupport.ultimaResposta)))
                .or(QViewTicketSupport.viewTicketSupport.idAtendente.eq(userOnline.getId())
                        .and(QViewTicketSupport.viewTicketSupport.atViewat.isNull().or(QViewTicketSupport.viewTicketSupport.atViewat.before(QViewTicketSupport.viewTicketSupport.ultimaResposta)))));
        return where;
    }
    
    private BooleanBuilder getStickersWhere(){
    	UserSecurity userOnline = FacesUtil.loggedUser();
    	BooleanBuilder where = new BooleanBuilder();
    	where.and(QTicketStickerSupport.ticketStickerSupport.ticketSupport.solicitante.id.eq(userOnline.getId()));
    	return where;
    }
    
    public Long countTodos(UserSecurity onlineUser) {
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }
    
    public Long countLembretesAtrasados(UserSecurity onlineUser){
    	BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();
        where.and(QViewTicketSupport.viewTicketSupport.stickerDate.before(new Date()))
        .andNot(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.FECHADO));
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }

    public Long countAbertos(UserSecurity onlineUser) {
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.ABERTO).or(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.REABERTO)));
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }
    
    /*
     * Metodo para contagem de lembretes
     */
    public Long countLembretes(UserSecurity onlineUser){
    	BooleanBuilder where = new BooleanBuilder();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.andNot(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.FECHADO)).and(QViewTicketSupport.viewTicketSupport.stickerDate.before(new Date()))
        .and(QViewTicketSupport.viewTicketSupport.idAtendente.eq(onlineUser.getId())).and(QViewTicketSupport.viewTicketSupport.stickerID.isNotNull());
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }

    public Long countFechados(UserSecurity onlineUser) {
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.FECHADO));
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }

    public Long countPendentes(UserSecurity onlineUser) {     
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.PENDENTE_USUARIO).or(QViewTicketSupport.viewTicketSupport.status.eq(TicketSupportStatus.PENDENTE_TERCEIROS)));
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }
    
    public Long countNaoDefinido(UserSecurity onlineUser) {     
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QViewTicketSupport.viewTicketSupport.idAtendente.isNull());
        query.select(QViewTicketSupport.viewTicketSupport.id.count()).from(QViewTicketSupport.viewTicketSupport).where(where);
        return (Long) query.fetchFirst();
    }
    
    public List<Long> getUserServices(){
        UserSecurity onlineUser = FacesUtil.loggedUser();                
        BooleanBuilder where = new BooleanBuilder();
        where.and(QTicketGroupService.ticketGroupService.grupo.id.in(
                JPAExpressions.select(QUserSecurityGroup.userSecurityGroup.ticketGroup.id)
                        .from(QUserSecurityGroup.userSecurityGroup)
                        .where(QUserSecurityGroup.userSecurityGroup.userSecurity.id.eq(onlineUser.getId()))
                ));
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketGroupService.ticketGroupService)
                .select(QTicketGroupService.ticketGroupService.servico.id)
                .where(where);        
        return query.fetch();
    }

}
