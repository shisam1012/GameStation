package com.example.server.connect4.game;

import com.example.server.connect4.sockets.SocketsManager;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * This class handles the clients' messages and responds to them.
 * It manages active games, processes moves, handles disconnections and timeouts and updates scores accordingly.
 */

@Component
public class GameHandlerC4 {
    // Keeps track of active games (map from username to game controller)
    private final Map<String, C4Controller> activeGames = new ConcurrentHashMap<>();
    private final SocketsManager sessionManager;
    private final C4Logic logic = new C4Logic();
    private final Gson gson = new Gson();

    public GameHandlerC4(SocketsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //Register a new game for both players
    public void registerGame(String player1, String player2, C4Controller game) {
        activeGames.put(player1, game);
        activeGames.put(player2, game);
    }

    //Handles a move request from a player
    public void handleMove(String username, int column) {
        C4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver()) //shouldnt happen...
            return;

        String currentPlayerName = game.getCurrentPlayerName();
        if (!currentPlayerName.equals(username)) { //validate that it is actually the player's turn
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Not your turn\"}");
            return;
        }

        int playerNumber = game.getCurrentPlayer();
        int[][] board = game.getBoard();
        String opponent = getOpponent(username, game);

        boolean success = logic.updateBoard(board, column, playerNumber); //try to place a disc in the given column
        String boardJson = gson.toJson(board);
        if (!success) { //the given column is a,ready full
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
        if (logic.winningBoard(board, playerNumber)) { //the last move finished the game with a win
            game.setGameOver(true);
            updateScore(game, username, opponent);
            sendToPlayer(username, String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You won!\"}"));
            sendToPlayer(opponent, String.format(
                    "{\"type\": \"gameOver\", \"message\": \"You lost!\"}"));
            removeGame(username);
            return;
        }
        if (logic.fullBoard(board)) { //there are no more possible moves and no one won the game already
            game.setGameOver(true);
            sendToPlayer(username, String.format(
                    "{\"type\": \"gameOver\", \"message\": \"It's a tie! This game will not count\"}"));
            sendToPlayer(opponent, String.format(
                    "{\"type\": \"gameOver\", \"message\": \"It's a tie! This game will not count\"}"));
            removeGame(username);
            return;
        }
        //otherwise - switch the turns and update the board for both players
        game.switchTurn();
        sendToPlayer(username, String.format(
                "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": false}", boardJson));

        sendToPlayer(opponent, String.format(
                "{\"type\": \"boardUpdate\", \"board\": %s, \"yourTurn\": true}", boardJson));
    }
    
    // Called when a player's timer runs out
    public void handleTimeOut(String username) {
        C4Controller game = activeGames.get(username);
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

    // Called when a player disconnects during a game
    public void handlePlayerDisconnected(String username) {
        C4Controller game = activeGames.get(username);
        if (game == null || game.isGameOver())
            return;

        String opponent = getOpponent(username, game);
        game.setGameOver(true);
        updateScore(game, opponent, username);

        String boardJson = gson.toJson(game.getBoard());
        sendToPlayer(opponent, String.format(
                "{\"type\": \"gameOver\", \"message\": \"You won! Your opponent disconnected.\", \"board\": %s}",
                boardJson));

        removeGame(username);
    }

    // Update player scores in the database based on the difficulty
    private void updateScore(C4Controller game, String username, String opponent) {
        try {
            switch (game.getDifficulty()) {
                case "easy":
                    C4DAO.updateInfo(username, 1, 1);
                    break;
                case "medium":
                    C4DAO.updateInfo(username, 3, 1);
                    break;
                case "hard":
                    C4DAO.updateInfo(username, 5, 1);
                    break;
            }
            C4DAO.updateInfo(opponent, 0, 0);

        } catch (SQLException e) {
            sendToPlayer(username, String
                    .format("{\"type\":\"exception\", \"message\": \"error while updating the score\"}"));
            sendToPlayer(opponent, String
                    .format("{\"type\":\"exception\", \"message\": \"error while updating the score\"}"));
        }

    }
   
    // Send a message to a specific player if their session is still open
    private void sendToPlayer(String username, String message) {
        if (sessionManager.isSocketOpen(username)) {
            sessionManager.sendMessageToPlayer(username, message);
        } else {
            System.out.println("Cannot send message, " + username + " is disconnected.");
        }
    }

    //Returns the name of the opponent 
    private String getOpponent(String username, C4Controller game) {
        return username.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();    
    }


    //Remove the game of the given user from the hashmap - also remove the player's opponent
    public void removeGame(String username) {
        C4Controller game = activeGames.remove(username);
        if (game != null) {
            activeGames.remove(game.getPlayer1());
            activeGames.remove(game.getPlayer2());
        }
    }

    //Check if a player is currently in a game
    public boolean isInGame(String username) {
        return activeGames.containsKey(username);
    }
}
