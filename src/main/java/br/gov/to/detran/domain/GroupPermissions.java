/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.domain;

/**
 *
 * @author Leice
 */
public class GroupPermissions {
    String key;
    Boolean selected;
    

    public GroupPermissions(String key, Boolean selected) {
        this.key = key;
        this.selected = selected;
    }        

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }        
    
}
