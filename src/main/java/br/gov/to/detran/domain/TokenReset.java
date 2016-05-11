/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_token_reset")
public class TokenReset extends AbstractEntity{
    
    @OneToOne
    @JoinColumn(name = "fk_user_security", nullable = true)    
    private UserSecurity userSecurity;

    public UserSecurity getUserSecurity() {
        return userSecurity;
    }

    public void setUserSecurity(UserSecurity userSecurity) {
        this.userSecurity = userSecurity;
    }
    
}
