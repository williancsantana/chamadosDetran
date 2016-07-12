/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.util.FacesUtil;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class UserLogged implements java.io.Serializable{
    UserSecurity user;
    
    @PostConstruct
    public void postConstruct(){
        this.user = FacesUtil.loggedUser();
    }
    
    public boolean canReply(TicketSupport ticketSupport){
        return true;
    }

    public UserSecurity getUser() {
        return user;
    }

    public void setUser(UserSecurity user) {
        this.user = user;
    }        
    
   
    
}
