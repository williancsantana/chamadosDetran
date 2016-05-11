/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.push;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author maycon
 */
@ServerEndpoint("/support/{user}")
@ApplicationScoped
public class NotifyPush {
    @Inject NotifySessions notifySessions;

    @OnOpen
    public void open(final Session session, @PathParam("user") final String user) {
        System.out.println("usuário logou: " + user);        
        notifySessions.addSession(session, Integer.parseInt(user));
    }
    
    @OnClose
    public void close(Session session, @PathParam("user") final String user) {
        notifySessions.removeSession(session, Integer.parseInt(user));
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(final Session session, final String mensagem) {
        String usr = (String) session.getUserProperties().get("user");                
    }   
}
