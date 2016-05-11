/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.push;

import br.gov.to.detran.domain.TicketReply;
import br.gov.to.detran.domain.TicketSupport;
import br.gov.to.detran.domain.UserSecurity;
import com.google.gson.Gson;
import java.io.Serializable;
import javax.faces.context.FacesContext;

/**
 *
 * @author maycon
 */
public class NotifyMessage implements Serializable {
    private String title;
    private String mensagem;
    private String url;
    private String type;
    
    public static NotifyMessage builderNew(TicketSupport t, UserSecurity user){                
        String url = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/chamado.jsf?id=" + t.getId().intValue();
        return new NotifyMessage("Detran TO - Chamados", user.getName() + ", chamado n° " + t.getNumero() + " escalonado para você",
                url, "0");
    }        
    
    public static NotifyMessage builderReply(TicketSupport t, UserSecurity user, TicketReply reply){                
        String url = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/chamado.jsf?id=" + t.getId().intValue();
        url += "#" + reply.getId();
        return new NotifyMessage("Detran TO - Chamados", user.getName() + ", chamado n° " + t.getNumero() + " respondido",
                url, "1");
    }        
       

    public NotifyMessage(String title, String mensagem, String url, String type) {
        this.title = title;
        this.mensagem = mensagem;
        this.url = url;
        this.type = type;
    }
        
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }        
    
    public String toJson(){
        return new Gson().toJson(this);
    }
        
}
