package com.example.server.connect4.sockets;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Manages WebSocket sessions for Connect4.
 */

@Component
public class SocketsManager implements DisposableBean{
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
private final Map<WebSocketSession, String> sessionToUsername = new ConcurrentHashMap<>();

   /*  public void addSession(String username, WebSocketSession session) {
       sessions.put(username, session);
   }*/
    public void addSession(String username, WebSocketSession session) {
    WebSocketSession oldSession = sessions.get(username);
    if (oldSession != null && oldSession.isOpen()) {
        try {
            oldSession.close(CloseStatus.NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    sessions.put(username, session);
    sessionToUsername.put(session, username);
}

public String getUsernameBySession(WebSocketSession session) {
    return sessionToUsername.get(session);
}

    public WebSocketSession getSession(String username) {
        return sessions.get(username);
    }

    /*public void removeSession(String username) {
        sessions.remove(username);
    }*/
    public void removeSession(String username) {
        WebSocketSession session = sessions.remove(username);
        if (session != null) {
            sessionToUsername.remove(session);
        }
    }

    public boolean isSocketOpen(String username) {
    WebSocketSession session = getSession(username);  
    return session != null && session.isOpen();
}

    /**
     * Sends a message to a specified player - identified by the username.
     */
    public void sendMessageToPlayer(String username, String message) {
        System.out.println("... In sendMessageToPlayer ...");
        WebSocketSession session = sessions.get(username);
        if (session != null) {
            System.out.println("Sending message to user " + username + ", session open: " + session.isOpen());
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    System.err.println("Failed to send message to " + username + ": " + e.getMessage());
                    removeSession(username);
                }
            } else {
                System.err.println("Session for " + username + " is closed.");
            }
        } else {
            System.err.println("Session for " + username + " not found.");
        }
    }

    /**
     * Close all active sessions on shutdown.
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("...Closing all WebSocket sessions...");
        
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.close(CloseStatus.GOING_AWAY);
            }
        }
        sessions.clear();
    }
}
