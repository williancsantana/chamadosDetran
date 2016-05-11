/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.util;

import br.gov.to.detran.domain.UserSecurity;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;

/**
 * Classes com funções uteis.
 *
 * @author Maycon
 */
public class FacesUtil {

    public static FacesMessage.Severity INFO = FacesMessage.SEVERITY_INFO;
    public static FacesMessage.Severity ERROR = FacesMessage.SEVERITY_ERROR;
    public static FacesMessage.Severity WARN = FacesMessage.SEVERITY_WARN;
    public static FacesMessage.Severity SUCESSO = FacesMessage.SEVERITY_FATAL;

    public static void addMessage(String mensagem, FacesMessage.Severity severity) {
        FacesUtil.addMessage(null, null, mensagem, severity, false);
    }

    public static void addMessage(String id, String mensagem, FacesMessage.Severity severity) {
        FacesUtil.addMessage(id, null, mensagem, severity, false);
    }

    public static void addMessage(String id, String sumario, String mensagem, FacesMessage.Severity severity, boolean limpar) {
        if (limpar) {
            FacesUtil.clearAllMessages();
        }
        FacesContext.getCurrentInstance().addMessage(
                id, new FacesMessage(severity, sumario, mensagem));
    }

    public static void clearAllMessages() {
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = context.getMessages();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public static UserSecurity loggedUser() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            UserSecurity userSecurity = (UserSecurity) SecurityUtils.getSubject().getPrincipals().asList().get(1);
            return userSecurity;
        }
        return null;
    }

    public Object getFlashAndRemove(String name) {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext()
                .getFlash();
        if (flash.containsKey(name)) {
            return flash.get(name);
        }
        return null;
    }

    public void setFlashObject(String name, Object value) {
        FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().put(name, value);
    }

    public static String getIpAddrs() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return httpServletRequest.getRemoteAddr();
    }

    public static Object getRequestParameter(String param) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParameter(param);
    }

}
