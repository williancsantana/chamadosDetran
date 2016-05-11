/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.controller;

import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Maycon
 */
@ManagedBean(name = "languageController", eager = true)
@SessionScoped
public class LanguageController implements java.io.Serializable{
    
    private Locale locale = new Locale("pt", "BR");   
    
    public void changeLanguage(String language, String country){        
        this.locale = new Locale(language, country);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(this.locale);
    }

    public Locale getLocale() {                
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }        

}
