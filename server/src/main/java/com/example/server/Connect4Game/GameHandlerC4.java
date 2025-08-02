package com.example.server.Connect4Game;

import com.example.server.Connect4Sockets.SocketsManagerC4;
import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class GameHandlerC4 {

    private final Map<String, Connect4Controller> activeGames = new ConcurrentHashMap<>();
    private final SocketsManagerC4 sessionManager;
    private final Connect4Logic logic = new Connect4Logic();
    private final Gson gson = new Gson();

    public GameHandlerC4(SocketsManagerC4 sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void registerGame(String player1, String player2, Connect4Controller game) {
        activeGames.put(player1, game);
        activeGames.put(player2, game);
    }

    public void handleMove(String username, int column) {
        Connect4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver()) return;

        String currentPlayerName = game.getCurrentPlayerName();
        if (!currentPlayerName.equals(username)) {
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Not your turn\"}");
            return;
        }

        int playerNumber = game.getCurrentPlayer();
        int[][] board = game.getBoard();

        boolean success = logic.updateBoard(board, column, playerNumber);
        if (!success) {
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Invalid move\"}");
            return;
        }

       
        if (logic.winningBoard(board, playerNumber)) {
            game.setGameOver(true);

            String boardJson = gson.toJson(board);
            String winnerMessage = String.format(
                "{\"type\": \"gameOver\", \"message\": \"You win!\", \"board\": %s}", boardJson);
            String loserMessage = String.format(
                "{\"type\": \"gameOver\", \"message\": \"You lose!\", \"board\": %s}", boardJson);

            sendToPlayer(username, winnerMessage);
            sendToPlayer(getOpponent(username, game), loserMessage);
            return;
        }

       
        game.switchTurn();
        String boardJson = gson.toJson(board);

        sendToPlayer(username, String.format(
            "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": false}", boardJson));

        sendToPlayer(getOpponent(username, game), String.format(
            "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": true}", boardJson));
    }

    private void sendToPlayer(String username, String message) {
        sessionManager.sendMessageToPlayer(username, message);
    }

    private String getOpponent(String username, Connect4Controller game) {
        return username.equals(game.getCurrentPlayerName()) ?
                (game.getCurrentPlayer() == 1 ? game.getPlayer2() : game.getPlayer1()) :
                username.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
    }

    public void removeGame(String username) {
        Connect4Controller game = activeGames.remove(username);
        if (game != null) {
            activeGames.remove(game.getPlayer1());
            activeGames.remove(game.getPlayer2());
        }
    }

    public boolean isInGame(String username) {
        return activeGames.containsKey(username);
    }
}
