/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QViewServidorChamado;
import br.gov.to.detran.domain.ViewServidorChamado;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import javax.transaction.Transactional;

/**
 *
 * @author maycon
 */
@Transactional
public class DetranERPRepository extends AbstractRepositoryDetranErp<ViewServidorChamado> implements java.io.Serializable {

    @Override
    public EntityPathBase<ViewServidorChamado> getEntityPath() {
        return QViewServidorChamado.viewServidorChamado;
    }
    
    public ViewServidorChamado findServidor(String cpf){
        print();
        JPAQuery query = this.query();
        QViewServidorChamado entity = QViewServidorChamado.viewServidorChamado;
        BooleanBuilder where = new BooleanBuilder();
        where.and(entity.cpf.eq(cpf));
        query.from(entity).where(where);
        System.out.println(query);
        return (ViewServidorChamado) query.fetchFirst();
    }
    
}
