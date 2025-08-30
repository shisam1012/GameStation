package com.example.server.connect4.game;

import com.example.server.connect4.sockets.SocketsManager;
import org.springframework.stereotype.Component;

/**
 * This class handles the clients' messages and responds to them.
 */


@Component
public class GameController {

    private final SocketsManager sessionManager;
    private final GameService gameService;

    public GameController(SocketsManager sessionManager, GameService gameService) {
        this.sessionManager = sessionManager;
        this.gameService = gameService;
    }

    public void handleMove(String username, int column) {
        GameObject game = gameService.getGame(username);
        if (game == null || game.isGameOver()) //shouldnt happen...
            return;

        if (!game.getCurrentPlayerName().equals(username)) { //validate that it is actually the player's turn
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Not your turn\"}");
            return;
        }

        String opponent = gameService.getOpponent(username, game);
        boolean success = gameService.playMove(game, username, column);  //try to place a disc in the given column
        String boardJson = gameService.toJsonBoard(game.getBoard());

        if (!success) { //the given column is a,ready full
            sendToPlayer(username, "{\"type\": \"invalidMove\", \"message\": \"Invalid move\"}");
            return;
        }

        if (!sessionManager.isSocketOpen(opponent)) { //the opponent's session is closed - end the game
            game.setGameOver(true);
            try {
                gameService.updateScore(username, opponent, game.getDifficulty());
            } 
            catch (Exception ignored) {
            }
            sendToPlayer(username, "{\"type\": \"gameOver\", \"message\": \"You won! opponent disconnected.\"}");
            gameService.removeGame(username);
            return;
        }

        if (gameService.checkWin(game, game.getCurrentPlayer())) { //the last move finished the game with a win
            game.setGameOver(true);
            try {
                gameService.updateScore(username, opponent, game.getDifficulty());
            } 
            catch (Exception ignored) {
            }
            sendToPlayer(username, "{\"type\": \"gameOver\", \"message\": \"You won!\"}");
            sendToPlayer(opponent, "{\"type\": \"gameOver\", \"message\": \"You lost!\"}");
            gameService.removeGame(username);
            return;
        }

        if (gameService.checkTie(game)) {  //there are no more possible moves and no one won the game already
            game.setGameOver(true);
            sendToPlayer(username, "{\"type\": \"gameOver\", \"message\": \"It's a tie!\"}");
            sendToPlayer(opponent, "{\"type\": \"gameOver\", \"message\": \"It's a tie!\"}");
            gameService.removeGame(username);
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
        GameObject game = gameService.getGame(username);
        if (game == null || game.isGameOver()) return;

        String opponent = gameService.getOpponent(username, game);
        game.setGameOver(true);

        sendToPlayer(username, "{\"type\": \"gameOver\", \"message\": \"You ran out of time!\"}");
        sendToPlayer(opponent, "{\"type\": \"gameOver\", \"message\": \"Your opponent ran out of time - You won!\"}");

        try {
            gameService.updateScore(opponent, username, game.getDifficulty());
        } catch (Exception ignored) {}

        gameService.removeGame(username);
    }

    
    public void handlePlayerDisconnected(String username) {
        GameObject game = gameService.getGame(username);
        if (game == null || game.isGameOver()) return;

        String opponent = gameService.getOpponent(username, game);
        game.setGameOver(true);

        try {
            gameService.updateScore(opponent, username, game.getDifficulty());
        } catch (Exception ignored) {}

        String boardJson = gameService.toJsonBoard(game.getBoard());
        sendToPlayer(opponent, String.format(
                "{\"type\": \"gameOver\", \"message\": \"You won! Your opponent disconnected.\", \"board\": %s}",
                boardJson));

        gameService.removeGame(username);
    }

    private void sendToPlayer(String username, String message) {
        if (sessionManager.isSocketOpen(username)) {
            sessionManager.sendMessageToPlayer(username, message);
        }
    }
}