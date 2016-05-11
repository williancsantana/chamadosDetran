/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.security;

import br.gov.to.detran.util.LDAP;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *
 * @author Maycon Costa
 */
public class UserTokenAuthentication extends UsernamePasswordToken{
    private SecurityPersistenceRepository securityPersistenceRepository;
    private AuthenticationType authenticationType; 
    private LDAP.User userLdap;

    public UserTokenAuthentication(SecurityPersistenceRepository securityPersistenceRepository, 
            String username, String password) {
        super(username, password);
        this.securityPersistenceRepository = securityPersistenceRepository;
        this.authenticationType = AuthenticationType.LOCAL;
    }    
    
    public UserTokenAuthentication(SecurityPersistenceRepository securityPersistenceRepository, LDAP.User user) {
        super(user.getUserPrincipal(), "ldap");
        this.securityPersistenceRepository = securityPersistenceRepository;
        this.authenticationType = AuthenticationType.LDAP;
        this.userLdap = user;
    }    
               
    public SecurityPersistenceRepository getSecurityPersistenceRepository() {
        return securityPersistenceRepository;
    }

    public void setSecurityPersistenceRepository(SecurityPersistenceRepository securityPersistenceRepository) {
        this.securityPersistenceRepository = securityPersistenceRepository;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }        

    public LDAP.User getUserLdap() {
        return userLdap;
    }

    public void setUserLdap(LDAP.User userLdap) {
        this.userLdap = userLdap;
    }        
    
}
