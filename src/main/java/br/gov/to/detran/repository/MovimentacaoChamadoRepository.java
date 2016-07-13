/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QMovimentacaoChamado;
import br.gov.to.detran.domain.MovimentacaoChamado;
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
public class MovimentacaoChamadoRepository extends AbstractRepository<MovimentacaoChamado> implements java.io.Serializable {

    @Override
    public EntityPathBase<MovimentacaoChamado> getEntityPath() {
        return QMovimentacaoChamado.movimentacaoChamado;
    }    
   
}