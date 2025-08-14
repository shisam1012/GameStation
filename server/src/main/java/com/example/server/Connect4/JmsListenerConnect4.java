package com.example.server.connect4;

import com.example.server.connect4.game.Connect4Controller;
import com.example.server.connect4.game.GameHandlerC4;
import com.example.server.connect4.sockets.SocketsManager;
import com.google.gson.Gson;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listens to the Connect 4 game queue and matches players.
 */

@Component
public class JmsListenerConnect4 {

    private final Map<String, Connect4Controller> activeGames = new ConcurrentHashMap<>();

    private final Queue<String> waitingPlayersEasy = new LinkedList<>();
    private final Queue<String> waitingPlayersMedium = new LinkedList<>();
    private final Queue<String> waitingPlayersHard = new LinkedList<>();

    private final SocketsManager sessionManager;
    private final Gson gson = new Gson();
    private final GameHandlerC4 gameHandler;

    /*public JmsListenerConnect4(SocketsManagerC4 sessionManager) {
        this.sessionManager = sessionManager;
    }*/
    public JmsListenerConnect4(SocketsManager sessionManager, GameHandlerC4 gameHandler) {
    this.sessionManager = sessionManager;
    this.gameHandler = gameHandler;
}

    @JmsListener(destination = "connect4easy.queue")
    public void receivePlayerEasy(String username) {
        System.out.println("... In receivePlayerEasy ...");
        System.out.println("received: " + username);
        waitingPlayersEasy.add(username);
        handleReceivePlayer(waitingPlayersEasy,"easy");
    }

    @JmsListener(destination = "connect4medium.queue")
    public void receivePlayerMedium(String username) {
        System.out.println("... In receivePlayerMedium ...");
        System.out.println("received: " + username);
        waitingPlayersMedium.add(username);
        handleReceivePlayer(waitingPlayersMedium, "medium");
    }

    @JmsListener(destination = "connect4hard.queue")
    public void receivePlayerHard(String username) {
        System.out.println("... In receivePlayerHard ...");
        System.out.println("received: " + username);
        waitingPlayersHard.add(username);
        handleReceivePlayer(waitingPlayersHard, "hard");
    }

    private void handleReceivePlayer(Queue<String> waitingPlayers, String difficulty) {
        if (waitingPlayers.size() == 1) {
            String playerWaiting = waitingPlayers.peek();
            if (playerWaiting != null && sessionManager.getSession(playerWaiting) != null) {
                String waitMessage = "{\"type\": \"waiting\", \"message\": \"waiting for another player\"}";
                sessionManager.sendMessageToPlayer(playerWaiting, waitMessage);
                System.out.println("Sent waiting message to " + playerWaiting);
            }
        }

        if (waitingPlayers.size() >= 2) {
            String username1 = waitingPlayers.poll();
            String username2 = waitingPlayers.poll();
            System.out.println("Can start a game between " + username1 + " and " + username2);

            // if one of the sessions is not open, return them to the queue
            if (sessionManager.getSession(username1) == null || sessionManager.getSession(username2) == null) {
                if (username1 != null)
                    waitingPlayers.add(username1);
                if (username2 != null)
                    waitingPlayers.add(username2);
                return;
            }
            if (username1.equals(username2)) {
                String jsonMessage = String.format(
                "{\"type\": \"duplicateError\", \"message\": \"You can\'t play against yourself...\"}"
            );
            sessionManager.sendMessageToPlayer(username1, jsonMessage);
            return;
            }

            // Create the game
            Connect4Controller game = new Connect4Controller(username1, username2,difficulty);
            gameHandler.registerGame(username1, username2, game);

            // Convert board to JSON
            String boardJson = gson.toJson(game.getBoard());

            // Build messages
            String jsonMessage1 = String.format(
                "{\"type\": \"match\", \"message\": \"Found another player! You will play against %s\", \"board\": %s, \"yourTurn\": true}",
                username2, boardJson
            );

            String jsonMessage2 = String.format(
                "{\"type\": \"match\", \"message\": \"Found another player! You will play against %s\", \"board\": %s, \"yourTurn\": false}",
                username1, boardJson
            );

            // Send to players
            sessionManager.sendMessageToPlayer(username1, jsonMessage1);
            System.out.println("Sent match message to " + username1);
            sessionManager.sendMessageToPlayer(username2, jsonMessage2);
            System.out.println("Sent match message to " + username2);
        }
    }

    public void removeFromAllQueues(String username) {
        waitingPlayersEasy.remove(username);
        waitingPlayersMedium.remove(username);
        waitingPlayersHard.remove(username);
        System.out.println("Removed " + username + " from all queues");
    }

    public Connect4Controller getGameForPlayer(String username) {
        return activeGames.get(username);
    }
}