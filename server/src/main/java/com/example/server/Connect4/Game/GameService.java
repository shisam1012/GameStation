package com.example.server.connect4.game;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service


public class GameService {
     // Keeps track of active games (map from username to game object)
    private final Map<String, GameObject> activeGames = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();
    private final C4Logic logic = new C4Logic();

    //This function inserts a new game to the map - one entry for each player
    public void registerGame(String player1, String player2, GameObject game) {
        activeGames.put(player1, game);
        activeGames.put(player2, game);
    }

    //Checks if the user is already in a game
    public boolean isInGame(String username) {
        return activeGames.containsKey(username);
    }

    //Returns the game object of the given username
    public GameObject getGame(String username) {
        return activeGames.get(username);
    }

    //This function removes a username and its game from the map, also remove its opponent
    public void removeGame(String username) {
        GameObject game = activeGames.remove(username);
        if (game != null) {
            activeGames.remove(game.getPlayer1());
            activeGames.remove(game.getPlayer2());
        }
    }

    //Returns the opponent of the given player
    public String getOpponent(String username, GameObject game) {
        return username.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
    }

    //This function updates the game board by calling logic.updateBoard
    //returns true if the move  is valid, otherwise false
    public boolean playMove(GameObject game, String username, int column) {
        int playerNumber = game.getCurrentPlayer();
        return logic.updateBoard(game.getBoard(), column, playerNumber);
    }

    //Calls logic.winningBoard and returns the same boolean answer
    public boolean checkWin(GameObject game, int playerNumber) {
        return logic.winningBoard(game.getBoard(), playerNumber);
    }

    //Calls logic.checkTie
    public boolean checkTie(GameObject game) {
        return logic.fullBoard(game.getBoard());
    }

    //This function gets the usernames of both players and the difficulty level
    //Calls C4DAO to update the info of the players
    public void updateScore(String winner, String loser, String difficulty) throws SQLException {
        switch (difficulty) {
            case "easy":
                C4DAO.updateInfo(winner, 1, 1);
                break;
            case "medium":
                C4DAO.updateInfo(winner, 3, 1);
                break;
            case "hard":
                C4DAO.updateInfo(winner, 5, 1);
                break;
        }
        C4DAO.updateInfo(loser, 0, 0);
    }

    //Converts an array to a json format in order to send it to the client
    public String toJsonBoard(int[][] board) {
        return gson.toJson(board);
    }
}
