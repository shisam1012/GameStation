package com.example.server.Connect4.Sockets;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.server.JmsListenerConnect4;
import com.example.server.Connect4.Game.GameHandlerC4;
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
    private final JmsListenerConnect4 jmsListenerConnect4;
    private final GameHandlerC4 gameHandler;

    public Connect4WebSocketHandler(SocketsManagerC4 sessionManager, JmsTemplate jmsTemplate, GameHandlerC4 gameHandler,JmsListenerConnect4 jmsListenerConnect4 ) {
        this.sessionManager = sessionManager;
        this.jmsTemplate = jmsTemplate;
        this.gameHandler = gameHandler;
        this.jmsListenerConnect4 = jmsListenerConnect4;
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
            System.out.println("User sent init message: " + username);
            jmsTemplate.convertAndSend("connect4" + difficulty.toLowerCase() + ".queue", username);// + ":" + difficulty);

        }
        if ("leave".equals(type)) {
            String username = (String) json.get("username");
            WebSocketSession userSession = sessionManager.getSession(username);

            if (username != null) {
                System.out.println("User left the game: " + username);

                if (userSession != null && userSession.isOpen()) {
                    try {
                        userSession.close(CloseStatus.NORMAL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                gameHandler.removeGame(username);
                sessionManager.removeSession(username);

            
                jmsListenerConnect4.removeFromAllQueues(username);

            } else {
                System.err.println("Leave message missing username.");
            }
        }


        
        if ("move".equals(type)) {
            String username = (String) json.get("username"); // וודא ששם המשתמש נשלח גם בהודעת ה-move
            Integer column = (Integer) json.get("column");

            if (username != null && column != null) {
                gameHandler.handleMove(username, column);
            } else {
                System.err.println("Invalid move message received: missing username or column");
            }
        }

    }

   
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed. Status: " + status);

        String username = sessionManager.getUsernameBySession(session);
        if (username != null) {
            gameHandler.handlePlayerDisconnected(username);  
            sessionManager.removeSession(username);
        }
    }



    public void sendToClient(WebSocketSession session, String msg) throws IOException {
        session.sendMessage(new TextMessage(msg));
    }
}
