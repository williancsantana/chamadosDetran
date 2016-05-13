/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import br.gov.to.detran.component.TicketSupportTable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class TicketSupportTableController implements java.io.Serializable {
    private @Inject TicketSupportTable supportTable;

    @PostConstruct
    public void postConstruct() {
    	try{
    		this.supportTable.update();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}        
    }

    public TicketSupportTable getSupportTable() {
        return supportTable;
    }

    public void setSupportTable(TicketSupportTable supportTable) {
        this.supportTable = supportTable;
    }        
}
