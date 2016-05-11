/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.repository;

import br.gov.to.detran.domain.QUserSecurityLogins;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.UserSecurityLogins;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPAQueryBase;
import javax.transaction.Transactional;

/**
 *
 * @author maycon
 */
@Transactional
public class UserSecurityLoginsRepository extends AbstractRepository<UserSecurityLogins> implements java.io.Serializable {

    @Override
    public EntityPathBase<UserSecurityLogins> getEntityPath() {
        return QUserSecurityLogins.userSecurityLogins;
    }
    
    public UserSecurityLogins findLastLogin(UserSecurity userSecurity){         
        BooleanBuilder where = new BooleanBuilder();
        where.and(QUserSecurityLogins.userSecurityLogins.userSecurity.id.eq(userSecurity.getId()));
        JPAQueryBase query = (JPAQueryBase) this.getPersistenceDao().query();
        query.from(QUserSecurityLogins.userSecurityLogins)
                .leftJoin(QUserSecurityLogins.userSecurityLogins.userSecurity)
                .where(where).limit(1).orderBy(QUserSecurityLogins.userSecurityLogins.created.desc());
        return (UserSecurityLogins) query.fetchFirst();
    }

}
