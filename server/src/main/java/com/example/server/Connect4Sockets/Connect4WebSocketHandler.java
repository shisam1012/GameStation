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
            jmsTemplate.convertAndSend("connect4" + difficulty.toLowerCase() + ".queue", username);// + ":" + difficulty);



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
