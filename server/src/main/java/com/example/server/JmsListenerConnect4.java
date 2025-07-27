package com.example.server;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.server.Connect4Sockets.SocketsManagerC4;

//import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class JmsListenerConnect4 {

    private final Queue<String> waitingPlayers = new LinkedList<>();
 private final SocketsManagerC4 sessionManager;

    public JmsListenerConnect4(SocketsManagerC4 sessionManager) {
        this.sessionManager = sessionManager;
    }
   @JmsListener(destination = "connect4easy.queue")
public void receivePlayer(String playerInfo) {
    System.out.println("recieved: " + playerInfo);
    waitingPlayers.add(playerInfo);

    if (waitingPlayers.size() >= 2) {
        String player1 = waitingPlayers.poll();
        String player2 = waitingPlayers.poll();
        System.out.println("can start a game between " + player1 + " and " + player2);

        
        if (player1.contains(":") && player2.contains(":")) {
    String username1 = player1.split(":")[0];  
    String username2 = player2.split(":")[0];

    sessionManager.sendMessageToPlayer(username1, "You are matched with " + username2);
    sessionManager.sendMessageToPlayer(username2, "You are matched with " + username1);
} else {
    System.err.println("Player info format incorrect: " + player1 + ", " + player2);
}



            /*try {
                sessionManager.sendMessageToPlayer(player1, "You are matched with " + player2);
                sessionManager.sendMessageToPlayer(player2, "You are matched with " + player1);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }
    


   
}
