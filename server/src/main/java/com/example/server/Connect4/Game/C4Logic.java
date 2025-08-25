package com.example.server.connect4.game;

/**
 * This class handles the logic of the game connect 4
 * Includes functions for updating a board, check if a board is full and if the board includes a winning sequence
 */

public class C4Logic {
	
	//Connect 4 standard board size: 6 rows × 7 columns
	private final int NUM_ROWS = 6; 
	private final int NUM_COLS = 7;
	
	/* 
	 * Attempts to drop a disc for the given player into the specified column.
	 * The disc falls to the lowest available row in that column.
	 *
	 * @param gameBoard the board (2D array)
	 * @param col the chosen column (0-based)
	 * @param numPlayer the player making the move (1 or 2)
	 * @return true if the move was successful, false if the column is full
	 */
	public boolean updateBoard(int[][] gameBoard, int col, int numPlayer) {
		if (gameBoard[0][col] == 0) {
			for (int i = NUM_ROWS - 1; i >= 0; i--) { //going bottom up to find the place to insert the disc
				if (gameBoard[i][col] == 0) { //empty place in the column
					gameBoard[i][col] = numPlayer;
					return true;
				}
			}
		}
		return false;  //column is full
	}
	
	//Check if the board is full - there are no more available moves
	public boolean fullBoard(int[][] gameBoard) {
		for (int i = 0; i < NUM_COLS; i++) {
			if (gameBoard[0][i] == 0)
				return false;
		}
		return true;
	}

	/*
	 * Checks if the given player has a winning sequence of 4 in a row.
	 * This includes:
	 *   - horizontal (row)
	 *   - vertical (column)
	 *   - diagonal (left-to-right and right-to-left)
	 */
	public boolean winningBoard(int[][] gameBoard, int numPlayer) {
		return (rowWin(gameBoard, numPlayer) || colWin(gameBoard, numPlayer) ||
				rightDiagWin(gameBoard, numPlayer) || leftDiagWin(gameBoard, numPlayer));
	}
	
	//Checks for a diagonal win (top-left to bottom-right)
	private boolean leftDiagWin(int [][] gameBoard,int numPlayer) {
		for (int i = 0 ; i < NUM_ROWS - 3 ; i++) {
			for (int j = 0 ; j < NUM_COLS -3  ; j++) {
				if (gameBoard[i][j] == numPlayer && 
					gameBoard[i+1][j+1] == numPlayer && 
					gameBoard[i+2][j+2] == numPlayer && 
					gameBoard[i+3][j+3] == numPlayer)
					return true;
			}
		}
		return false;
	}

	//Checks for diagonal win (top-right to bottom-left)
	private boolean rightDiagWin(int[][] gameBoard, int numPlayer) {
		for (int i = 0; i < NUM_ROWS - 3; i++) {
			for (int j = 3; j < NUM_COLS; j++) {
				if (gameBoard[i][j] == numPlayer &&
						gameBoard[i + 1][j - 1] == numPlayer &&
						gameBoard[i + 2][j - 2] == numPlayer &&
						gameBoard[i + 3][j - 3] == numPlayer)
					return true;
			}
		}
		return false;
	}
	
	// Checks for horizontal win 
	private boolean rowWin(int[][] gameBoard, int numPlayer) {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS - 3; j++) {
				if (gameBoard[i][j] == numPlayer &&
						gameBoard[i][j + 1] == numPlayer &&
						gameBoard[i][j + 2] == numPlayer &&
						gameBoard[i][j + 3] == numPlayer)
					return true;
			}
		}
		return false;
	}
	
	// Checks for vertical win 
	private boolean colWin(int [][] gameBoard,int numPlayer) {
		for (int i = 0 ; i < NUM_COLS ; i++) {
			for (int j = 0 ; j < NUM_ROWS -3 ; j++) {
				if (gameBoard[j][i] == numPlayer && 
					gameBoard[j+1][i] == numPlayer && 
					gameBoard[j+2][i] == numPlayer && 
					gameBoard[j+3][i] == numPlayer)
					return true;
			}
		}
		return false;
	}
	
	
}