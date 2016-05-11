/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.security;

import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.UserStatus;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * Classes responsavel por realizar a autenticação e a autorização do usuario
 *
 * @author Maycon
 */
@RequestScoped
public class Authorization extends AuthorizingRealm {

    public Authorization() {
        this.setName("myRealm");
    }

    /**
     * Gerar os perfils do usuario
     *
     * @param pc
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        SimplePrincipalCollection simplePrincipal = (SimplePrincipalCollection) pc;
        UserSecurity user = (UserSecurity) simplePrincipal.asList().get(1);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("Admin");
        return simpleAuthorizationInfo;
    }

    /**
     * Realiza a autenticação do usuario, verificando os dados de login e senha
     * repassados. Caso o não tenha um usuario, é retornado uma exceção para o
     * controller.
     *
     * @param at
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {        
        UserTokenAuthentication userToken = (UserTokenAuthentication) at;
        SecurityPersistenceRepository userRepository = userToken.getSecurityPersistenceRepository();        
        UserSecurity user = null;
        try {
            user = userRepository.findUser("email", userToken.getUsername());            
        } catch (Exception ex) {            
            throw new AuthenticationException("Usuário ou senhas inválidos");
        }        
        if (userToken.getAuthenticationType() == AuthenticationType.LDAP) {            
            if (userToken.getUserLdap().getUserAccountControl().equalsIgnoreCase("2")
                    || userToken.getUserLdap().getUserAccountControl().equalsIgnoreCase("16")) {
                throw new LockedAccountException("Usuário Desabilitado.");
            }
            if (user == null) {                
                user = new UserSecurity();
                user.setEmail(userToken.getUserLdap().getUserPrincipal());
                user.setName(userToken.getUserLdap().getDisplayName());
                user.setCpf(userToken.getUserLdap().getUserPrincipal().split("@")[0]);
                user.setCreated(new Date());
                user.setUpdated(new Date());
                user.setUserStatus(UserStatus.ACTIVE);
                user.setAuthenticationType(AuthenticationType.LDAP);
                user.setPassword("ldap");
                if (!userRepository.insertUser(user)) {
                    throw new LockedAccountException("Não possivel realizar o login.");
                }
            }
        } else if (userToken.getAuthenticationType() == AuthenticationType.LOCAL && user != null) {
            if (user.getUserStatus() == UserStatus.BLOCKED) {
                throw new LockedAccountException("Usuário Bloqueado.");
            }
        } else if(user == null){
            throw new AuthenticationException("Usuário ou senhas inválidos");
        }        
        SimplePrincipalCollection principal = new SimplePrincipalCollection();
        principal.add(user.getName(), this.getName());
        principal.add(user, this.getName());
        return new SimpleAuthenticationInfo(principal, user.getPassword());        
    }

}
