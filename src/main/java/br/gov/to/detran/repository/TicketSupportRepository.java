/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.Group;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPAQueryBase;

import br.gov.to.detran.domain.AtendenteEscalonamento;
import br.gov.to.detran.domain.QTicketGroupService;
import br.gov.to.detran.domain.QTicketReply;
import br.gov.to.detran.domain.QTicketSupport;
import br.gov.to.detran.domain.QUserSecurityGroup;
import br.gov.to.detran.domain.TicketReply;
import br.gov.to.detran.domain.TicketReplyType;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.TicketSupportStatus;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.util.FacesUtil;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

/**
 *
 * @author maycon
 */
@Transactional
public class TicketSupportRepository extends AbstractRepository<TicketSupport> implements java.io.Serializable {
    
    public static String jsonTransacoes = "{\n" +
"            \"ABERTO\": {\n" +
"                \"AUTO\": \"ABERTO\",\n" +
"                \"FECHAR\": \"FECHADO\",\n" +
"                \"PENDENTE_USUARIO\": \"PENDENTE_USUARIO\",\n" +
"                \"PENDENTE_TERCEIROS\": \"PENDENTE_TERCEIROS\"\n" +
"            },\n" +
"            \"REABERTO\": {\n" +
"                \"AUTO\": \"REABERTO\",\n" +
"                \"FECHAR\": \"FECHADO\",\n" +
"                \"PENDENTE_USUARIO\": \"PENDENTE_USUARIO\",\n" +
"                \"PENDENTE_TERCEIROS\": \"PENDENTE_TERCEIROS\"\n" +
"            },\n" +
"            \"FECHADO\": {\n" +
"                \"AUTO\": \"REABERTO\"\n" +
"            },\n" +
"            \"PENDENTE_TERCEIROS\": {\n" +
"                \"AUTO\": \"PENDENTE_TERCEIROS\",\n" +
"                \"RETIRAR_PENDENCIA\": \"REABERTO\"\n" +
"            },\n" +
"            \"PENDENTE_USUARIO\": {\n" +
"                \"AUTO\": \"REABERTO\",\n" +
"                \"FECHAR\": \"FECHADO\"\n" +
"            }\n" +
"        }";
    
    public HashMap<String, HashMap<String, Object>> getTransacoes(){
        Type stringStringMap = new TypeToken<HashMap<String, HashMap<String, Object>>>(){}.getType();
        HashMap<String, HashMap<String, Object>> fromJson = new Gson().fromJson(jsonTransacoes, stringStringMap);        
        return fromJson;
    }

    
    @Override
    public EntityPathBase<TicketSupport> getEntityPath() {
        return QTicketSupport.ticketSupport;
    }

    public Map<Long, Object> contarChamados(List<Long> users) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(QTicketSupport.ticketSupport.atendente.id.in(users));
        where.and(QTicketSupport.ticketSupport.status.ne(TicketSupportStatus.FECHADO));
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketSupport.ticketSupport)
                .leftJoin(QTicketSupport.ticketSupport.atendente)
                //.select(QTicketSupport.ticketSupport.atendente.id, QTicketSupport.ticketSupport.id.count())
                .where(where);
        query.groupBy(QTicketSupport.ticketSupport.atendente.id);

        Map<Long, Object> result = (Map<Long, Object>) query.transform(groupBy(QTicketSupport.ticketSupport.atendente.id)
                .as(set(QTicketSupport.ticketSupport.id.count())));
        
        /*return query.list(Projections.constructor(AtendenteEscalonamento.class,
        		QTicketSupport.ticketSupport.atendente.id,
        		QTicketSupport.ticketSupport.id.count()));*/     
        
        return result;/*(List<AtendenteEscalonamento>) query.select(Projections.bean(AtendenteEscalonamento.class, QTicketSupport.ticketSupport.atendente.id,
        		QTicketSupport.ticketSupport.id.count())).fetch();*/
    }        
    
    public List<TicketReply> getAllReplys(Long id){
        JPAQueryBase query = this.getPersistenceDao().query();
        BooleanBuilder where = new BooleanBuilder();
        where.and(QTicketReply.ticketReply.chamado.id.eq(id));
        query.from(QTicketReply.ticketReply).where(where);
        query.orderBy(QTicketReply.ticketReply.created.asc());
        return query.fetch();
    }

    public Long getNextSequence() {
        JPAQueryBase query = this.getPersistenceDao().query();
        Long sequence = (Long) query.select(QTicketSupport.ticketSupport.id.max()).from(QTicketSupport.ticketSupport).fetchFirst();
        return (sequence == null ? 1L : (sequence + 1));
    }
    
    private BooleanBuilder getDefaultWhere(){
        UserSecurity userOnline = FacesUtil.loggedUser();
        BooleanBuilder where = new BooleanBuilder();
        where.and(QTicketSupport.ticketSupport.solicitante.id.eq(userOnline.getId()).or(QTicketSupport.ticketSupport.atendente.id.eq(userOnline.getId())));
        where.and(QTicketSupport.ticketSupport.solicitante.id.eq(userOnline.getId())
                .and(QTicketSupport.ticketSupport.solViewat.isNull().or(QTicketSupport.ticketSupport.solViewat.before(QTicketSupport.ticketSupport.ultimaResposta)))
                .or(QTicketSupport.ticketSupport.atendente.id.eq(userOnline.getId())
                        .and(QTicketSupport.ticketSupport.atViewat.isNull().or(QTicketSupport.ticketSupport.atViewat.before(QTicketSupport.ticketSupport.ultimaResposta)))));
        return where;
    }

    public Long countTodos(UserSecurity onlineUser) {
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        query.select(QTicketSupport.ticketSupport.id.count()).from(QTicketSupport.ticketSupport).where(where);
        return (Long) query.fetchFirst();
    }

    public Long countAbertos(UserSecurity onlineUser) {
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.ABERTO).or(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.REABERTO)));
        query.select(QTicketSupport.ticketSupport.id.count()).from(QTicketSupport.ticketSupport).where(where);
        return (Long) query.fetchFirst();
    }

    public Long countFechados(UserSecurity onlineUser) {
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.FECHADO));
        query.select(QTicketSupport.ticketSupport.id.count()).from(QTicketSupport.ticketSupport).where(where);
        return (Long) query.fetchFirst();
    }

    public Long countPendentes(UserSecurity onlineUser) {     
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.PENDENTE_USUARIO).or(QTicketSupport.ticketSupport.status.eq(TicketSupportStatus.PENDENTE_TERCEIROS)));
        query.select(QTicketSupport.ticketSupport.id.count()).from(QTicketSupport.ticketSupport).where(where);
        return (Long) query.fetchFirst();
    }
    
    public Long countNaoDefinido(UserSecurity onlineUser) {     
        BooleanBuilder where = this.getDefaultWhere();
        JPAQueryBase query = this.getPersistenceDao().query();        
        where.and(QTicketSupport.ticketSupport.atendente.isNull());
        query.select(QTicketSupport.ticketSupport.id.count()).from(QTicketSupport.ticketSupport).where(where);
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
        //where.and(QTicketGroupService.ticketGroupService.tipo.eq(TicketGroupServiceType.ATENDENTE));                
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketGroupService.ticketGroupService)
                .select(QTicketGroupService.ticketGroupService.servico.id)
                .where(where);        
        return query.fetch();
    }

    public TicketSupport getTicketSupport(Long id) {
        System.out.println("id: "  + id);
        BooleanBuilder where = new BooleanBuilder();
        where.and(QTicketSupport.ticketSupport.id.eq(id));
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketSupport.ticketSupport)
        .leftJoin(QTicketSupport.ticketSupport.respostas).where(where);
        return (TicketSupport) query.fetchFirst();
    }
    
    public TicketReply lastClosing(Long id){
        BooleanBuilder where = new BooleanBuilder();
        where.and(QTicketReply.ticketReply.chamado.id.eq(id));
        where.and(QTicketReply.ticketReply.tipo.eq(TicketReplyType.NOTIFICATION));
        where.and(QTicketReply.ticketReply.mensagem.eq("FECHADO"));
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QTicketReply.ticketReply)
        .where(where).limit(1).orderBy(QTicketReply.ticketReply.created.desc());
        return (TicketReply) query.fetchFirst();
    }
    
    public void updateViewat(TicketSupport instance) {        
        UserSecurity user = FacesUtil.loggedUser();
        if (instance.getSolicitante().getId().equals(user.getId())) {            
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("solViewat", new Date());
            parameters.put("id", instance.getId());
            this.getPersistenceDao().executeRawQuery("update TicketSupport set solViewat =:solViewat where id =:id", parameters);
        } else if (instance.getAtendente() != null && instance.getAtendente().getId().equals(user.getId())) {         
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("atViewat", new Date());
            parameters.put("id", instance.getId());
            this.getPersistenceDao().executeRawQuery("update TicketSupport set atViewat =:atViewat where id =:id", parameters);
        }
    }    

}
