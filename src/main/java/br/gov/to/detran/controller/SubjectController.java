/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.UserSecurityLogins;
import br.gov.to.detran.repository.UserSecurityLoginsRepository;
import br.gov.to.detran.util.LDAP;
import br.gov.to.detran.security.LdapRepository;
import br.gov.to.detran.security.SecurityPersistenceRepository;
import br.gov.to.detran.security.UserTokenAuthentication;
import br.gov.to.detran.util.FacesUtil;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author Maycon
 */
@Named
@ViewScoped
public class SubjectController implements java.io.Serializable {

    private @Inject
    SecurityPersistenceRepository securityPersistenceRepository;
    private @Inject
    UserSecurityLoginsRepository userSecurityLoginsRepository;
    private @Inject
    LdapRepository ldapRepository;
    
    private UserSecurity usuario;

    private String username;
    private String password;

    private String pwd1;
    private String pwd2;
    private String token;
    private boolean haveToken = false;

    public void prepararOpcao() {
        usuario = FacesUtil.loggedUser();
    }        

    public String userHome() {
        try {
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/login.jsf");
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/index.jsf");
        } catch (IOException ex) {            
            Logger.getLogger(SubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "login";
    }   
    
    public String signIn(){        
        try {            
            LDAP.User userLdap = ldapRepository.signLdap(username, password);
            UserTokenAuthentication userToken = userLdap != null ? 
                    new UserTokenAuthentication(securityPersistenceRepository, userLdap) : 
                    new UserTokenAuthentication(securityPersistenceRepository, username, password);                        
            SecurityUtils.getSubject().login(userToken);
            UserSecurityLogins login = new UserSecurityLogins();
            login.setCreated(new Date());
            login.setUpdated(new Date());
            login.setIp();
            login.setUserSecurity(FacesUtil.loggedUser());
            userSecurityLoginsRepository.insert(login);
            return this.userHome();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException | ExcessiveAttemptsException ex) {                        
            FacesUtil.addMessage(ex.getMessage(), FacesUtil.ERROR);
        } catch (AuthenticationException ex) {                        
            FacesUtil.addMessage(null, "Login", ex.getMessage(), FacesUtil.ERROR, true);
        } catch (Exception ex) {             
            Logger.getLogger(SubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "login";
    }

    public String signOut() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            try {
                SecurityUtils.getSubject().logout();
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/login.jsf");

            } catch (IOException ex) {
                Logger.getLogger(SubjectController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "login";
    }

    public String getPrincipals() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return (String) SecurityUtils.getSubject().getPrincipal();
        }
        return "";
    }

    public void resetPassword() {
        if (!username.isEmpty()) {
            try {
                securityPersistenceRepository.resetPassword(username);
                FacesUtil.addMessage("Foi gerado um token de resete, entre em seu e-mail e confirme.", FacesUtil.INFO);
            } catch (Exception ex) {
                ex.printStackTrace();
                FacesUtil.addMessage("Houve um problema ao gerar seu token de alteração de senha.", FacesUtil.ERROR);
            }
        }
    }

    public void updatePassword() {
        if (token != null && !token.isEmpty()) {
            try {
                securityPersistenceRepository.updatePassword(pwd1, token);
                this.token = "";
                this.pwd1 = "";
                this.pwd2 = "";
                FacesUtil.addMessage("Senha atualizada com sucesso.", FacesUtil.INFO);
            } catch (Exception ex) {
                ex.printStackTrace();
                FacesUtil.addMessage(ex.getMessage(), FacesUtil.ERROR);
            }
        }
    }

    public void confirmerReset() {
        System.out.println("resetando: " + this.token);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isHaveToken() {
        return haveToken;
    }

    public void setHaveToken(boolean haveToken) {
        this.haveToken = haveToken;
    }

    public String getPwd1() {
        return pwd1;
    }

    public void setPwd1(String pwd1) {
        this.pwd1 = pwd1;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }

    public UserSecurity getUsuario() {
        return usuario;
    }

    public void setUsuario(UserSecurity usuario) {
        this.usuario = usuario;
    }

}
