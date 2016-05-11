/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.security;

import br.gov.to.detran.repository.UserSecurityRepository;
import br.gov.to.detran.util.LDAP;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.ldap.LdapContext;

/**
 *
 * @author Leice
 */
@RequestScoped
public class LdapRepository implements Serializable{
    private final String DOMAIN = "detran.to.gov.br";
    private final String SERVER = "srv-ad";
    private final String ADMIN_USERNAME = "99999999999";
    private final String ADMIN_PASSWORD = "detran##linkcon##";
    
    @Inject
    private UserSecurityRepository userSecurityRepository;    
    
    private LdapContext getConnection(String username, String password) throws Exception{
        return LDAP.getConnection(username, password, DOMAIN, SERVER);
    }
    
    public LDAP.User signLdap(String username, String password) {
        try {            
            LdapContext ctx = this.getConnection(username, password);
            LDAP.User user = LDAP.getUser(username, ctx);            
            ctx.close();
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    
    public LdapRepository.LDAPResult search(String cpf) {        
        try {            
            LdapContext ctx = this.getConnection(ADMIN_USERNAME, ADMIN_PASSWORD);
            LDAP.User user = LDAP.getUser(cpf, ctx);            
            ctx.close();
            if(userSecurityRepository.existsCPF(cpf)){
                return new LDAPResult(3, null);
            }
            if(user.getUserAccountControl().equals("2") || user.getUserAccountControl().equals("16")){
                return new LDAPResult(2, user);
            }
            return new LDAPResult(1, user);
        } catch (Exception e) {
            e.printStackTrace();            
            return new LDAPResult(0, null);
        }
    }
    
    public static class LDAPResult{
        private final int status;
        private LDAP.User userResult;

        public LDAPResult(int status, LDAP.User userResult) {
            this.status = status;
            this.userResult = userResult;
        }

        public LDAP.User getUserResult() {
            return userResult;
        }

        public void setUserResult(LDAP.User userResult) {
            this.userResult = userResult;
        }

        public int getStatus() {
            return status;
        }                
        
        public String getMensagemResult(){
            switch(this.status){
                case 0: return "O Usúario não foi encontrado";
                case 1: return "O Usúario foi encontrado";
                case 2: return "Está conta está desabilitada";
                case 3: return "O usuário foi encontrado, mas já está cadastrado nesse sistema";
                default: return "";
            }            
        }
        
        public String getMensagemIcon(){
             switch(this.status){
                case 0: return "remove user";
                case 1: return "thumbs up";
                case 2: return "ban";
                case 3: return "info circle";
                default: return "";
            }
        }
        
        public String getMensagemClass(){
             switch(this.status){
                case 0: return "error";
                case 1: return "success";
                case 2: return "warning";
                case 3: return "info";
                default: return "";
            }
        }
        
    }
}
