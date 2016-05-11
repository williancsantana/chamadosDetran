/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 * Classe responsavel para acessar as propriedades de mensagem do sistema. Usado
 * também para internacionalização.
 * @author Maycon
 */
public class LanguageResource implements java.io.Serializable {

    private final static String name = "msg";
        
    public String getString(String key) {
        String msg = key;
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, LanguageResource.name);        
        if (bundle.containsKey(key)) {            
            msg = bundle.getString(key);
        }
        return msg;
    }
    
    public String getString(String key, Object... params){
        String msg = this.getString(key);
        return MessageFormat.format(msg, params);
    }

}
