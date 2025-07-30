package com.example.server;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.example.server.Connect4Sockets.SocketsManagerC4;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Listens to the Connect 4 game queue and matches players.
 */

@Component
public class JmsListenerConnect4 {

    private final Queue<String> waitingPlayersEasy = new LinkedList<>();
    private final Queue<String> waitingPlayersMedium = new LinkedList<>();
    private final Queue<String> waitingPlayersHard = new LinkedList<>();
    
    private final SocketsManagerC4 sessionManager;

    public JmsListenerConnect4(SocketsManagerC4 sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    /*Add a new player's username to the queue of players who want to play connect 4 in easy mode */
    @JmsListener(destination = "connect4easy.queue")
    public void receivePlayerEasy(String username) {
       System.out.println("... In receivePlayerEasy ...");
       System.out.println("recieved: " + username);
       waitingPlayersEasy.add(username);
       handleReceivePlayer(waitingPlayersEasy);
    }
    
   @JmsListener(destination = "connect4medium.queue")
    public void receivePlayerMedium(String username) {
       System.out.println("... In receivePlayerMedium ...");
       System.out.println("recieved: " + username);
       waitingPlayersMedium.add(username);
       handleReceivePlayer(waitingPlayersMedium);
    }
    
   @JmsListener(destination = "connect4hard.queue")
    public void receivePlayerHard(String username) {
       System.out.println("... In receivePlayerHard ...");
       System.out.println("recieved: " + username);
       waitingPlayersHard.add(username);
       handleReceivePlayer(waitingPlayersHard);
    }
    
   private void handleReceivePlayer(Queue<String> waitingPlayers) {
        if (waitingPlayers.size() == 1) {
           String playerWaiting = waitingPlayers.peek();
           if (playerWaiting != null && sessionManager.getSession(playerWaiting) == null) {
               String waitMessage = "{\"type\": \"waiting\", \"message\": \"waiting for another player\"}";
               sessionManager.sendMessageToPlayer(playerWaiting, waitMessage);
               System.out.println("Sent waiting message to " + playerWaiting);
           }
       }

       if (waitingPlayers.size() >= 2) { //we can start a game between 2 of them
           String username1 = waitingPlayers.poll();
           String username2 = waitingPlayers.poll();
           System.out.println("Can start a game between " + username1 + " and " + username2);
            //if the socket is not open yet - return the players back to the queue
           if (sessionManager.getSession(username1) == null || sessionManager.getSession(username2) == null) {
               waitingPlayers.add(username1);
               waitingPlayers.add(username2);
               return;
           }

           String jsonMessage1 = String.format(
                   "{\"type\": \"match\", \"message\": \"Found another player! You will play against %s\"}", username2);
           String jsonMessage2 = String.format(
                   "{\"type\": \"match\", \"message\": \"Found another player! You will play against %s\"}", username1);
           //sending to the players that a match is found via the socket
           sessionManager.sendMessageToPlayer(username1, jsonMessage1);
           System.out.println("Sent match message to " + username1);
           sessionManager.sendMessageToPlayer(username2, jsonMessage2);
           System.out.println("Sent match message to " + username2);
       }
    
   }
    
  
}
