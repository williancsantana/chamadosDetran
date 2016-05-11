/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QTokenReset;
import br.gov.to.detran.domain.TokenReset;
import com.querydsl.core.types.dsl.EntityPathBase;
import javax.transaction.Transactional;

/**
 *
 * @author maycon
 */
@Transactional
public class TokenResetRepository extends AbstractRepository<TokenReset> implements java.io.Serializable {

    @Override
    public EntityPathBase<TokenReset> getEntityPath() {
        return QTokenReset.tokenReset;
    }

}
