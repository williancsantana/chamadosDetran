/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author maycon
 */
@Entity
@Table(name = "tb_grupo")
public class TicketGroup extends AbstractEntity{
    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;
    
    @Column(name = "permissoes", nullable = false, length = 4000)
    private String permissoes;  
    
    @Transient
    private List<String> permissions;
    
    @Transient
    private HashMap<String, Boolean> mapPermissoes;
        
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {        
        this.descricao = descricao;
    }

    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }        
    
    public List<String> permissions(){
        if(this.permissions == null){
            this.permissions = Arrays.asList(this.permissoes.split(";"));
        }
        return this.permissions;
    }
    
    public boolean containPermission(String permission){
        System.out.println(permission);
        System.out.println(Arrays.deepToString(this.permissions().toArray()));
        return this.permissions().contains(permission);
    }       
    
}
