package com.example.server.Connect4Sockets;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Map;

/**
 * Handles incoming WebSocket messages and connections.
 */
@Component
public class Connect4WebSocketHandler extends TextWebSocketHandler {

    private final SocketsManagerC4 sessionManager;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public Connect4WebSocketHandler(SocketsManagerC4 sessionManager, JmsTemplate jmsTemplate) {
        this.sessionManager = sessionManager;
        this.jmsTemplate = jmsTemplate;
    }

    private String extractParam(WebSocketSession session, String key) {
        String query = session.getUri().getQuery();
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return null;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("... In afterConnectionEstablished ...");
        //String username = extractParam(session, "username");
        //String difficulty = extractParam(session, "difficulty");

        /*if (username == null || difficulty == null) {
            System.out.println("Missing username or difficulty in connection URL");
            session.close(); 
            return;
        }*/

        //sessionManager.addSession(username, session);

        //jmsTemplate.convertAndSend("connect4" + difficulty.toLowerCase() + ".queue", username + ":" + difficulty);

        //System.out.println("User connected: " + username + " with difficulty: " + difficulty);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Transport error in session " + session.getId() + ": " + exception.getMessage());
        exception.printStackTrace();
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("... In handleTextMessage ...");
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        Map<String, Object> json = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        String type = (String) json.get("type");

        if ("init".equals(type)) {
            String username = (String) json.get("username");
            String difficulty = (String) json.get("difficulty");
            sessionManager.addSession(username, session);
            System.out.println("User connected: " + username);
            jmsTemplate.convertAndSend("connect4" + difficulty.toLowerCase() + ".queue", username + ":" + difficulty);



        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed. Status: " + status);
    }

    public void sendToClient(WebSocketSession session, String msg) throws IOException {
        session.sendMessage(new TextMessage(msg));
    }
}
