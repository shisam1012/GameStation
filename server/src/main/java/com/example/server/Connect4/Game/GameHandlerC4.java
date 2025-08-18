package com.example.server.connect4.game;

import com.example.server.connect4.sockets.SocketsManager;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class GameHandlerC4 {

    private final Map<String, Connect4Controller> activeGames = new ConcurrentHashMap<>();
    private final SocketsManager sessionManager;
    private final Connect4Logic logic = new Connect4Logic();
    private final Gson gson = new Gson();

    public GameHandlerC4(SocketsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void registerGame(String player1, String player2, Connect4Controller game) {
        activeGames.put(player1, game);
        activeGames.put(player2, game);
    }

    public void handleMove(String username, int column) {
        Connect4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver())
            return;

        String currentPlayerName = game.getCurrentPlayerName();
        if (!currentPlayerName.equals(username)) {
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Not your turn\"}");
            return;
        }

        int playerNumber = game.getCurrentPlayer();
        int[][] board = game.getBoard();
        String opponent = getOpponent(username, game);
        boolean success = logic.updateBoard(board, column, playerNumber);
        if (!success) {
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Invalid move\"}");
            return;
        }

        if (!sessionManager.isSocketOpen(opponent)) {
            game.setGameOver(true);
            updateScore(game, username, opponent);
            String boardJson = gson.toJson(board);
            String winnerMessage = String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You win! your opponent disconnected...\", \"board\": %s}",
                    boardJson);
            sendToPlayer(username, winnerMessage);
            System.out.println("sending winning message after the opponent disconnected");
            removeGame(username);
            return;
        }
        if (logic.winningBoard(board, playerNumber)) {
            game.setGameOver(true);

            String boardJson = gson.toJson(board);
            String winnerMessage = String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You win!\", \"board\": %s}", boardJson);

            String loserMessage = String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You lose!\", \"board\": %s}", boardJson);

            updateScore(game, username, opponent);
            /*try {
                switch (game.getDifficulty()) {
                    case "easy":
                        C4DBController.updateInfo(username, 1, 1);
                        break;
                    case "medium":
                        C4DBController.updateInfo(username, 3, 1);
                        break;
                    case "hard":
                        C4DBController.updateInfo(username, 5, 1);
                        break;
                }
                C4DBController.updateInfo(opponent, 0, 0);
            
            } catch (SQLException e) {
                String exception = String
                        .format("{\"type\":\"exception\", \"message\": \"error while updating the score\"}");
                sendToPlayer(username, exception);
                sendToPlayer(opponent, exception);
            }*/

            sendToPlayer(username, winnerMessage);
            sendToPlayer(opponent, loserMessage);
            return;
        }

        game.switchTurn();
        String boardJson = gson.toJson(board);

        sendToPlayer(username, String.format(
                "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": false}", boardJson));

        sendToPlayer(getOpponent(username, game), String.format(
                "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": true}", boardJson));
    }
        
    public void handlePlayerDisconnected(String username) {
        Connect4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver()) return;

        String opponent = getOpponent(username, game);
        game.setGameOver(true);
        updateScore(game, opponent, username); 

        String boardJson = gson.toJson(game.getBoard());
        sendToPlayer(opponent, String.format("{\"type\": \"gameOver\", \"message\": \"You win! Your opponent disconnected.\", \"board\": %s}", boardJson));
        //sendToPlayer(username, String.format("{\"type\": \"gameOver\", \"message\": \"You disconnected.\", \"board\": %s}", boardJson));

        removeGame(username);
    }

    private void updateScore(Connect4Controller game, String username, String opponent) {
        try {
            switch (game.getDifficulty()) {
                case "easy":
                    C4DBController.updateInfo(username, 1, 1);
                    break;
                case "medium":
                    C4DBController.updateInfo(username, 3, 1);
                    break;
                case "hard":
                    C4DBController.updateInfo(username, 5, 1);
                    break;
            }
            C4DBController.updateInfo(opponent, 0, 0);

        } catch (SQLException e) {
            String exception = String
                    .format("{\"type\":\"exception\", \"message\": \"error while updating the score\"}");
            sendToPlayer(username, exception);
            sendToPlayer(opponent, exception);
        }

    }
   
    private void sendToPlayer(String username, String message) {
        if (sessionManager.isSocketOpen(username)) {
            sessionManager.sendMessageToPlayer(username, message);
        } else {
            System.out.println("Cannot send message, " + username + " is disconnected.");
        }
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
