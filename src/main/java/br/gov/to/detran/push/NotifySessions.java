/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.to.detran.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 *
 * @author Maycon
 */
@ApplicationScoped
public class NotifySessions implements java.io.Serializable{
    HashMap<Integer, List<Session>> sessions = new HashMap<>();
    
    public void addSession(Session session, Integer user){
        if(this.sessions.containsKey(user)){                       
           this.sessions.get(user).add(session);
        }else{
            List<Session> userSessions = new ArrayList<>();
            userSessions.add(session);
            this.sessions.put(user, userSessions);
        }
    }
    
    public void removeSession(Session session, Integer user){
        if(this.sessions.containsKey(user)){  
            this.sessions.get(user).remove(session);
        }
    }
    
    public void sendMensagem(Integer user, NotifyMessage msg) throws IOException{
        List<Session> forRemove = new ArrayList<>();
        if(this.sessions.containsKey(user)){  
            List<Session> userSessions =  this.sessions.get(user);
            for(Session s : userSessions){
                if(s.isOpen()){
                    s.getBasicRemote().sendText(msg.toJson());
                }else{                    
                    forRemove.add(s);
                }
            }
            userSessions.removeAll(forRemove);
        }
        
    }
}
