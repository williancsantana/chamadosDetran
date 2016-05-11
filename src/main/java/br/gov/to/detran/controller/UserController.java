/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import br.gov.to.detran.domain.TicketGroup;
import br.gov.to.detran.domain.UserSecurity;
import br.gov.to.detran.domain.UserSecurityGroup;
import br.gov.to.detran.domain.UserStatus;
import br.gov.to.detran.repository.Repository;
import br.gov.to.detran.repository.TicketGroupRepository;
import br.gov.to.detran.repository.UserSecurityLoginsRepository;
import br.gov.to.detran.repository.UserSecurityRepository;
import br.gov.to.detran.security.AuthenticationType;
import br.gov.to.detran.security.LdapRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class UserController extends BaseController<UserSecurity> implements java.io.Serializable {

    private @Inject
    UserSecurityRepository repository;
    private @Inject
    UserSecurityLoginsRepository userLoginsrepository;
    private @Inject
    TicketGroupRepository ticketGroupRepository;
    private @Inject
    LdapRepository ldapRepository;

    private List<TicketGroup> listGrupos;
    private TicketGroup ticketGroup;
    private String ldapCpf;
    private LdapRepository.LDAPResult ldapUserResult;

    @PostConstruct
    public void postConstruct() {
        super.postContruct();
        UserSecurity user = this.getFlash("usuarioInstance");
        if (user != null) {
            this.instance = user;
            user.setLastLogin(userLoginsrepository.findLastLogin(user));
        }
    }

    public void insert() {
        try {
            this.instance.setUserStatus(UserStatus.ACTIVE);
            this.instance.setAusente(false);
            this.repository.insert(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Cadastro", "Registro Adicionado!");
            this.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        try {
            this.instance.setUpdated(new Date());
            this.repository.update(this.instance);
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Usuário Atualizado", "Atualização");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.addMenssage(FacesMessage.SEVERITY_ERROR, "Error ao Atualizar o usuário", "Atualização");
        }
    }

    public void remove(UserSecurity instance) {
        try {
            this.repository.remove(instance);
            this.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void searchLdapUser() {
        try {
            this.ldapUserResult = ldapRepository.search(this.ldapCpf);
        } catch (NullPointerException ex) {
        }
    }

    public void insertLdapUser() {
        try {
            UserSecurity user = new UserSecurity();
            user.setEmail(ldapUserResult.getUserResult().getUserPrincipal());
            user.setName(ldapUserResult.getUserResult().getDisplayName());
            user.setCpf(ldapUserResult.getUserResult().getUserPrincipal().split("@")[0]);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            user.setUserStatus(UserStatus.ACTIVE);
            user.setAuthenticationType(AuthenticationType.LDAP);
            user.setPassword(new Sha256Hash("ldap").toHex());
            user.setAusente(false);
            this.repository.insert(user);
            addMenssage(FacesMessage.SEVERITY_ERROR, "Usuário do LDAP importando com sucesso!", "Importar");
            this.ldapUserResult = null;
            this.ldapCpf = "";
        } catch (NullPointerException ex) {
            addMenssage(FacesMessage.SEVERITY_ERROR, "Usuário do LDAP não encontrado!", "Importar");
        } catch (Exception ex) {            
        }
    }

    public void removeGrupo(UserSecurityGroup grupo) {
        this.instance.getGrupos().remove(grupo);
    }

    public void validar() {

    }

    @Override
    public Repository getRepository() {
        return this.repository;
    }

    public List<TicketGroup> getListGrupos() {
        if (this.listGrupos == null) {
            this.listGrupos = ticketGroupRepository.getAll();
        }
        List<TicketGroup> temporario = new ArrayList<>(this.listGrupos);
        temporario.removeAll(this.instance.getAllGrupos());
        return temporario;
    }

    public void setListGrupos(List<TicketGroup> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public void adicionarTicketGroup() {
        if (this.ticketGroup != null) {            
            this.instance.getGrupos().add(new UserSecurityGroup(this.instance, ticketGroup));
            this.addMenssage(FacesMessage.SEVERITY_INFO, "Grupo adicionado", "Adicionar");
            this.ticketGroup = null;
            return;
        }
        this.addMenssage(FacesMessage.SEVERITY_ERROR, "Nenhum grupo selecionado", "Adicionar");
    }

    public void addTicketGroup(TicketGroup g) {        
        ticketGroup = g;
    }

    public String getLdapCpf() {
        return ldapCpf;
    }

    public void setLdapCpf(String ldapCpf) {
        this.ldapCpf = ldapCpf;
    }

    public LdapRepository.LDAPResult getLdapUserResult() {
        return ldapUserResult;
    }
    
    public boolean isShowImportButton(){
        if(ldapUserResult == null){
            return true;
        }
        return ldapUserResult.getStatus() != 1;
    }

}
