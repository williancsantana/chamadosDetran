package br.gov.to.detran.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user_grupo")
public class UserSecurityGroup extends AbstractEntity{
    @JoinColumn(name = "fk_user")
    @ManyToOne    
    private UserSecurity userSecurity;
    
    @JoinColumn(name = "fk_grupo")
    @ManyToOne
    private TicketGroup ticketGroup;

    public UserSecurityGroup() {
    }        

    public UserSecurityGroup(UserSecurity userSecurity, TicketGroup ticketGroup) {
        this.userSecurity = userSecurity;
        this.ticketGroup = ticketGroup;
    }        

    public UserSecurity getUserSecurity() {
        return userSecurity;
    }

    public void setUserSecurity(UserSecurity userSecurity) {
        this.userSecurity = userSecurity;
    }

    public TicketGroup getTicketGroup() {
        return ticketGroup;
    }

    public void setTicketGroup(TicketGroup ticketGroup) {
        this.ticketGroup = ticketGroup;
    }           
    
}