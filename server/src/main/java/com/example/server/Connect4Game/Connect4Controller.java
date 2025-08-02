package com.example.server.Connect4Game;

public class Connect4Controller {
    private final int ROWS = 6;
    private final int COLS = 7;
    private int[][] board; //0 = empty, 1 = player1, 2 =player2
    private String player1;
    private String player2;
    private int currentPlayer; // 1 or 2
    private boolean isGameOver;

    public Connect4Controller(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new int[ROWS][COLS];
        this.currentPlayer = 1;
        this.isGameOver = false;
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

