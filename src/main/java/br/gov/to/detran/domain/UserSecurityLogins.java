/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import br.gov.to.detran.util.FacesUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_user_logins")
public class UserSecurityLogins extends AbstractEntity{
    @Column(name = "ipaddr", nullable = false, length = 255)
    private String ipaddr;     
    
    @JoinColumn(name = "fk_user_security")
    @ManyToOne
    private UserSecurity userSecurity;    
    
    public void setIp(){
        this.ipaddr = FacesUtil.getIpAddrs();
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public UserSecurity getUserSecurity() {
        return userSecurity;
    }

    public void setUserSecurity(UserSecurity userSecurity) {
        this.userSecurity = userSecurity;
    }        
   
}
