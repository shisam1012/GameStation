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
 * Manages WebSocket sessions for the Connect 4 game.
 * This class is thread-safe and supports concurrent WebSocket connections from multiple players.
 */
@Component
public class SocketsManager implements DisposableBean {
    
    // Using ConcurrentHashMap for thread safety since WebSocket events are handled in multiple threads.
    // Map of username -> WebSocketSession. 
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    // Reverse mapping from WebSocketSession -> username for quick lookup.
    private final Map<WebSocketSession, String> sessionToUsername = new ConcurrentHashMap<>();

   
    /**
     * Adds a new session for a player.
     * If the player already has an active session, the old one is closed first.
     */
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

    //Retrieves the username for a given WebSocketSession
    public String getUsernameBySession(WebSocketSession session) {
        return sessionToUsername.get(session);
    }

    //Retrieves the WebSocketSession for a given username
    public WebSocketSession getSession(String username) {
        return sessions.get(username);
    }

    /**
     * Removes a session from both mappings
     * Called when a player leaves or disconnects
     */
    public void removeSession(String username) {
        WebSocketSession session = sessions.remove(username);
        if (session != null) {
            sessionToUsername.remove(session);
        }
    }

   // Checks if the WebSocket for a given player is still open
    public boolean isSocketOpen(String username) {
        WebSocketSession session = getSession(username);  
        return session != null && session.isOpen();
    }

    /**
     * Sends a message to a specified player - identified by the username.
     * If the connection is closed or fails, the session is removed.
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
                    System.out.println("Failed to send message to " + username + ": " + e.getMessage());
                    removeSession(username);
                }
            } 
            else {
                System.out.println("Session for " + username + " is closed, can not send the message.");
            }
        } 
        else {
            System.out.println("Session for " + username + " not found, can not send the message.");
        }
    }

    /**
     * Closes all active WebSocket sessions when the bean is destroyed
     * Triggered automatically on application shutdown
     * Implements DisposableBean
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
