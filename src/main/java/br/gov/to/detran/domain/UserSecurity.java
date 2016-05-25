/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.to.detran.security.AuthenticationType;
import br.gov.to.detran.security.Security;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_user")
public class UserSecurity extends AbstractEntity implements Security{

    @Column(name = "cpf", nullable = false)
    private String cpf;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;
    
    @Column(name = "ausente")
    private Boolean ausente;
            
    @OneToMany(mappedBy = "userSecurity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSecurityGroup> grupos;
    
    @OneToMany(mappedBy = "userSecurity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EscalaTrabalho> escalaDeTrabalho = new ArrayList<>();
    
    //@OneToOne(optional=true, mappedBy="user")
    @Transient
    private UserAvatar userAvatar;
    
    @Transient
    UserSecurityLogins lastLogin;

    public UserSecurity() {
    }        

    @Override
    public String getName() 
    {        
        return name;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }    

    public List<UserSecurityGroup> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<UserSecurityGroup> grupos) {
        this.grupos = grupos;
    }       
    
    public boolean isLdap(){
        return authenticationType == AuthenticationType.LDAP;
    }
    
    public boolean isLocal(){
        return authenticationType == AuthenticationType.LOCAL;
    }

    public UserSecurityLogins getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(UserSecurityLogins lastLogin) {
        this.lastLogin = lastLogin;
    }    

    public Boolean getAusente() {
        if(ausente == null){
            ausente = false;
        }
        return ausente;
    }

    public void setAusente(Boolean ausente) {
        this.ausente = ausente;
    }    

    public List<TicketGroup> getAllGrupos() {
        List<TicketGroup> tempGrupos = new ArrayList<>();
        if(this.grupos != null){
        	for(UserSecurityGroup g : this.grupos){
                tempGrupos.add(g.getTicketGroup());
            }
        }        
        return tempGrupos;
    }

	public List<EscalaTrabalho> getEscalaDeTrabalho() {
		return escalaDeTrabalho;
	}

	public void setEscalaDeTrabalho(List<EscalaTrabalho> escalaDeTrabalho) {
		this.escalaDeTrabalho = escalaDeTrabalho;
	}

	public UserAvatar getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(UserAvatar userAvatar) {
		this.userAvatar = userAvatar;
	}       
	
	public String getSmallName(){
		String[] nameSplit = name.split(" ");
		String tempName = nameSplit[0]; 
		try{
			tempName += " " + nameSplit[1];
		}catch(Exception e){			
		}
		return tempName;		
	}
	
}
