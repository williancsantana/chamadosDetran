/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.messages.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.UINotificationRenderer;

/**
 * Classe refeita para alterar o estilo das mensagens gerados pelo primefaces;
 * @author Maycon
 */
public class MyMessageRenderer extends UINotificationRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {                
        Messages uiMessages = (Messages) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = uiMessages.getClientId(context);
        Map<String, List<FacesMessage>> messagesMap = new HashMap<>();
        boolean globalOnly = uiMessages.isGlobalOnly();
        String containerClass = uiMessages.isShowIcon() ? Messages.CONTAINER_CLASS : Messages.ICONLESS_CONTAINER_CLASS;
        String style = uiMessages.getStyle();
        String styleClass = uiMessages.getStyleClass();
        styleClass = (styleClass == null) ? containerClass : containerClass + " " + styleClass;

        String _for = uiMessages.getFor();
        Iterator<FacesMessage> messages;
        if (_for != null) {
            // key case
            messages = context.getMessages(_for);

            // clientId / SearchExpression case
            UIComponent forComponent = SearchExpressionFacade.resolveComponent(
                    context, uiMessages, _for);
            if (forComponent != null) {
                messages = context.getMessages(forComponent.getClientId(context));
            }
        } else {
            messages = uiMessages.isGlobalOnly() ? context.getMessages(null) : context.getMessages();
        }

        while (messages.hasNext()) {
            FacesMessage message = messages.next();
            FacesMessage.Severity severity = message.getSeverity();

            if (severity.equals(FacesMessage.SEVERITY_INFO)) {
                addMessage(uiMessages, message, messagesMap, "info");
            } else if (severity.equals(FacesMessage.SEVERITY_WARN)) {
                addMessage(uiMessages, message, messagesMap, "warn");
            } else if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
                addMessage(uiMessages, message, messagesMap, "error");
            } else if (severity.equals(FacesMessage.SEVERITY_FATAL)) {
                addMessage(uiMessages, message, messagesMap, "fatal");
            }            
        }                        

        writer.startElement("div", uiMessages);
        writer.writeAttribute("id", clientId, "id");
        //writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }
        writer.writeAttribute("aria-live", "polite", null);

        if (RequestContext.getCurrentInstance().getApplicationContext().getConfig().isClientSideValidationEnabled()) {
            writer.writeAttribute("data-global", String.valueOf(globalOnly), null);
            writer.writeAttribute("data-summary", uiMessages.isShowSummary(), null);
            writer.writeAttribute("data-detail", uiMessages.isShowDetail(), null);
            writer.writeAttribute("data-severity", getClientSideSeverity(uiMessages.getSeverity()), null);
            writer.writeAttribute("data-redisplay", String.valueOf(uiMessages.isRedisplay()), null);
        }

        for (String severity : messagesMap.keySet()) {
            List<FacesMessage> severityMessages = messagesMap.get(severity);

            if (severityMessages.size() > 0) {
                encodeSeverityMessages(context, uiMessages, severity, severityMessages);
            }
        }

        writer.endElement("div");
    }

    protected void addMessage(Messages uiMessages, FacesMessage message, Map<String, List<FacesMessage>> messagesMap, String severity) {
        if (shouldRender(uiMessages, message, severity)) {
            List<FacesMessage> severityMessages = messagesMap.get(severity);

            if (severityMessages == null) {
                severityMessages = new ArrayList<FacesMessage>();
                messagesMap.put(severity, severityMessages);
            }

            severityMessages.add(message);
        }
    }

    protected void encodeSeverityMessages(FacesContext context, Messages uiMessages, String severity, List<FacesMessage> messages) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String styleClassPrefix = Messages.SEVERITY_PREFIX_CLASS + severity;
        boolean escape = uiMessages.isEscape();
        
        String styleClass = "note-success";
        String title = "Sucesso";
        switch(severity){
            case "info" : styleClass = "note-info"; title = "Informação"; break;
            case "warn" : styleClass = "note-warning"; title = "Atenção"; break;
            case "error" : styleClass = "note-danger"; title = "Error"; break;            
        }
        
        writer.startElement("div", null);
        writer.writeAttribute("class", "note card " + styleClass, null);
        writer.writeAttribute("id", "myMensagem", null);

        if (uiMessages.isClosable()) {
            encodeCloseIcon(context, uiMessages);
        }
        
        writer.startElement("strong", null);
        writer.writeText(title, null);
        writer.endElement("strong");

        writer.startElement("ul", null);

        for (FacesMessage msg : messages) {
            writer.startElement("li", null);

            String summary = msg.getSummary() != null ? msg.getSummary() : "";
            String detail = msg.getDetail() != null ? msg.getDetail() : summary;

            if (uiMessages.isShowSummary()) {
                writer.startElement("span", null);
                writer.writeAttribute("class", styleClassPrefix + "-summary", null);

                if (escape) {
                    writer.writeText(summary + " - ", null);
                } else {
                    writer.write(summary + " - ");
                }

                writer.endElement("span");
            }

            if (uiMessages.isShowDetail()) {
                writer.startElement("span", null);
                writer.writeAttribute("class", styleClassPrefix + "-detail", null);

                if (escape) {
                    writer.writeText(detail, null);
                } else {
                    writer.write(detail);
                }

                writer.endElement("span");
            }

            writer.endElement("li");

            msg.rendered();
        }

        writer.endElement("ul");

        writer.endElement("div");
    }

    protected void encodeCloseIcon(FacesContext context, Messages uiMessages) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("class", Messages.CLOSE_LINK_CLASS, null);
        writer.writeAttribute("onclick", "$(this).parent().slideUp();return false;", null);

        writer.startElement("span", null);
        writer.writeAttribute("class", Messages.CLOSE_ICON_CLASS, null);
        writer.endElement("span");

        writer.endElement("a");
    }
}
