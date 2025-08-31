package com.example.server.connect4.game;

/**
 * Controller class for a Connect 4 game.
 * Holds the game state: players, board, current turn, difficulty, and status.
 */
public class GameObject {
    private final int ROWS = 6;
    private final int COLS = 7;
    private int[][] board; // 0 = empty, 1 = player1's disc, 2 = player2's disc
    private String player1;
    private String player2;
    private int currentPlayer; // 1 or 2
    private boolean isGameOver;
    private String difficulty; // difficulty level, affects scoring

    
    /**
     * Constructor: initializes a new game between two players.
     *
     * @param player1   username of player 1
     * @param player2   username of player 2
     * @param difficulty chosen difficulty level
     */
    public GameObject(String player1, String player2, String difficulty) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new int[ROWS][COLS];
        this.currentPlayer = 1;
        this.isGameOver = false;
        this.difficulty = difficulty;
    }

    /*
     * Getters and Setters
     */
    
    public int[][] getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCurrentPlayerName() {
        if (currentPlayer == 1)
            return player1;
        return player2;
    }

    public void switchTurn() {
        currentPlayer = 3 - currentPlayer;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean status) {
        this.isGameOver = status;
    }

    public String getPlayer1() {
    return player1;
    }

    public String getPlayer2() {
        return player2;
    }

   
}

