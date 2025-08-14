package com.example.server.connect4.sockets;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.server.connect4.JmsListenerConnect4;
import com.example.server.connect4.game.GameHandlerC4;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Map;

/**
 * WebSocketHandler handles all WebSocket interactions for the Connect 4 game.
 * Responsibilities:
 *  - Receiving and processing messages from clients
 *  - Managing player sessions via SocketsManager
 *  - Communicating with the game logic (GameHandlerC4)
 *  - Forwarding player requests to the JMS queues for matchmaking
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    // Manages active WebSocket sessions (username and session mapping)
    private final SocketsManager sessionManager;
    //Used to send messages to JMS queues (for matchmaking)
    private final JmsTemplate jmsTemplate;
    /// JSON serializer/deserializer for parsing incoming messages and creating responses 
    private final ObjectMapper mapper = new ObjectMapper();
    //Listens to JMS messages for game-related events (opponent found, game updates)
    private final JmsListenerConnect4 jmsListenerConnect4;
    //Core game logic handler for processing moves and managing game state.
    private final GameHandlerC4 gameHandler;

    /**
     * Constructor — Spring injects all required beans automatically here.
     * @param sessionManager manages WebSocket sessions
     * @param jmsTemplate handles JMS messaging
     * @param gameHandler core game logic for Connect 4
     * @param jmsListenerConnect4 listener for incoming JMS events
     */
    public WebSocketHandler(SocketsManager sessionManager, JmsTemplate jmsTemplate, GameHandlerC4 gameHandler,JmsListenerConnect4 jmsListenerConnect4 ) {
        this.sessionManager = sessionManager; //spring will inject this
        this.jmsTemplate = jmsTemplate;
        this.gameHandler = gameHandler;//spring will inject this
        this.jmsListenerConnect4 = jmsListenerConnect4;//spring will inject this
    }

    /**
     * Handle an error from the underlying WebSocket message transport.
     * Closes the session with SERVER_ERROR status to prevent dangling connections.
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Transport error in session " + session.getId() + ": " + exception.getMessage());
        exception.printStackTrace();
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    /**
     * Handles incoming text messages from the client.
     * Supported message types:
     *  - "init": player joins with username + difficulty - add session & send to matchmaking queue.
     *  -"leave": player leaves → close session, clean up game state, remove from matchmaking.
     *  - "move": player makes a move in a column - forward to game handler.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //System.out.println("... In handleTextMessage ...");
        String payload = message.getPayload();
        //System.out.println("Received message: " + payload);
        Map<String, Object> json = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        String type = (String) json.get("type");
        String username = (String) json.get("username");
        if (username == null) {
            System.err.println("The message is missing a username.");
        }
        else {
            switch (type) {
                case "init": // Player requests to play a game
                    System.out.println("User sent init message: " + username);
                    String difficulty = (String) json.get("difficulty");
                    sessionManager.addSession(username, session);
                    jmsTemplate.convertAndSend("connect4" + difficulty.toLowerCase() + ".queue", username);
                    break;
                
                case "leave": // Player wants to leave the game or matchmaking
                    System.out.println("User left the game: " + username);
                    WebSocketSession userSession = sessionManager.getSession(username);
                    if (userSession != null && userSession.isOpen()) {
                        try {
                            userSession.close(CloseStatus.NORMAL);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // Remove from game state and matchmaking queues
                    gameHandler.removeGame(username);
                    sessionManager.removeSession(username);
                    jmsListenerConnect4.removeFromAllQueues(username);
                    break;

                case "move": // player makes a move in the game 
                    Integer column = (Integer) json.get("column");
                    if (column != null) {
                        gameHandler.handleMove(username, column);
                    } else {
                        System.err.println("Invalid move message received: missing a column");
                    }
                    break;
            }
        }
    }

    /**
     * Called when a WebSocket connection is closed.
     * Cleans up session data and notifies the game handler that the player disconnected.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed. Status: " + status);
        String username = sessionManager.getUsernameBySession(session);
        if (username != null) {
            gameHandler.handlePlayerDisconnected(username);  
            sessionManager.removeSession(username);
        }
    }

}
