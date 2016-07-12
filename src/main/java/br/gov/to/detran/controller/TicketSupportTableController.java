/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.to.detran.component.ViewTicketSupportTable;

/**
 *
 * @author maycon
 */
@Named
@ViewScoped
public class TicketSupportTableController implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private @Inject ViewTicketSupportTable supportTable;

    @PostConstruct
    public void postConstruct() {
    	try{
    		this.supportTable.update();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}        
    }

    public ViewTicketSupportTable getSupportTable() {
        return supportTable;
    }

    public void setSupportTable(ViewTicketSupportTable supportTable) {
        this.supportTable = supportTable;
    }        
}
