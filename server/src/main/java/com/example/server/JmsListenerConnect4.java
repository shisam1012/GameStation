package com.example.server;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class JmsListenerConnect4 {

    private final Queue<String> waitingPlayers = new LinkedList<>();

    @JmsListener(destination = "connect4easy.queue")
    public void receivePlayer(String playerInfo) {
        System.out.println("recieved: " + playerInfo);
        waitingPlayers.add(playerInfo);

        if (waitingPlayers.size() >= 2) {
            String player1 = waitingPlayers.poll();
            String player2 = waitingPlayers.poll();
            System.out.println("can start a game between " + player1 + " and " + player2);
           
        }
    }
}
