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
        String boardJson = gson.toJson(board);
        if (!success) {
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Invalid move\"}");
            return;
        }

        if (!sessionManager.isSocketOpen(opponent)) { //the opponent's session is closed - end the game
            game.setGameOver(true);
            updateScore(game, username, opponent);
            String winnerMessage = String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You won! your opponent disconnected...\"}");
            sendToPlayer(username, winnerMessage);
            System.out.println("sending winning message after the opponent disconnected");
            removeGame(username);
            return;
        }
        if (logic.winningBoard(board, playerNumber)) { //the last move finished the game
            game.setGameOver(true);
            updateScore(game, username, opponent);
            sendToPlayer(username, String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You won!\"}"));
            sendToPlayer(opponent, String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You lost!\"}"));
            removeGame(username);
            return;
        }
        game.switchTurn();
        sendToPlayer(username, String.format(
                "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": false}", boardJson));

        sendToPlayer(getOpponent(username, game), String.format(
                "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": true}", boardJson));
    }
       
    public void handleTimeOut(String username) {
        Connect4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver()) return;

        String opponent = getOpponent(username, game);
        game.setGameOver(true);
        
        //send a message to the player who ran out of time
        sendToPlayer(username, String.format(
                "{\"type\": \"gameOver\", \"message\": \"You ran out of time!\"}"));
        
        //semding a winning message to the opponent
        sendToPlayer(opponent, String.format(
                "{\"type\": \"gameOver\", \"message\": \"Your opponent ran out of time - You won!\"}"));

        //update score
        updateScore(game, opponent, username);
        removeGame(username);
    }


    public void handlePlayerDisconnected(String username) {
        Connect4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver()) return;

        String opponent = getOpponent(username, game);
        game.setGameOver(true);
        updateScore(game, opponent, username); 

        String boardJson = gson.toJson(game.getBoard());
        sendToPlayer(opponent, String.format("{\"type\": \"gameOver\", \"message\": \"You win! Your opponent disconnected.\", \"board\": %s}", boardJson));
        
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
            sendToPlayer(username, String
                    .format("{\"type\":\"exception\", \"message\": \"error while updating the score\"}"));
            sendToPlayer(opponent, String
                    .format("{\"type\":\"exception\", \"message\": \"error while updating the score\"}"));
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
        if (username.equals(game.getCurrentPlayerName())) {
            return game.getCurrentPlayer() == 1 ? game.getPlayer2() : game.getPlayer1();
        } else {
            return username.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
        }
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
