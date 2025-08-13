package com.example.server.Connect4Game;

public class Connect4Logic {
	
	private final int NUM_ROWS = 6;
	private final int NUM_COLS = 7;
	//int [][] gameBoard = new int[NUM_ROWS][NUM_COLS];
	
	public boolean updateBoard(int [][] gameBoard,int col, int numPlayer) {
		
		if (gameBoard[0][col] == 0) {
			for (int i = NUM_ROWS-1 ; i >=0 ; i--) {
				if (gameBoard[i][col] == 0) {
					gameBoard[i][col] = numPlayer;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean winningBoard(int [][] gameBoard,int numPlayer) {
		
		return (rowWin(gameBoard, numPlayer) || colWin(gameBoard, numPlayer) || 
				rightDiagWin(gameBoard, numPlayer) || leftDiagWin(gameBoard, numPlayer));
	}
	
	private boolean leftDiagWin(int [][] gameBoard,int numPlayer) {
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0 ; j < 4  ; j++) {
				if (gameBoard[i][j] == numPlayer && 
					gameBoard[i+1][j+1] == numPlayer && 
					gameBoard[i+2][j+2] == numPlayer && 
					gameBoard[i+3][j+3] == numPlayer)
					return true;
			}
		}
		return false;
	}

	
	private boolean rightDiagWin(int [][] gameBoard,int numPlayer) {
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 3 ; j < 7  ; j++) {
				if (gameBoard[i][j] == numPlayer && 
					gameBoard[i+1][j-1] == numPlayer && 
					gameBoard[i+2][j-2] == numPlayer && 
					gameBoard[i+3][j-3] == numPlayer)
					return true;
			}
		}
		return false;
	}
	
	private boolean rowWin(int [][] gameBoard,int numPlayer) {
		for (int i = 0 ; i < NUM_ROWS ; i++) {
			for (int j = 0 ; j < NUM_COLS -3 ; j++) {
				if (gameBoard[i][j] == numPlayer && 
					gameBoard[i][j+1] == numPlayer && 
					gameBoard[i][j+2] == numPlayer && 
					gameBoard[i][j+3] == numPlayer)
					return true;
			}
		}
		return false;
	}
	
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
	
	public void startOver(int [][] gameBoard) {
		for (int i = 0 ; i < NUM_ROWS ; i++) {
			for (int j = 0 ; j < NUM_COLS ; j++) {
				gameBoard[i][j] = 0 ;
			}
		}
	}
	
	public void printArr(int [][] gameBoard) {
		System.out.println();
		for (int i = 0 ; i < NUM_ROWS ; i++) {
			for (int j = 0 ; j < NUM_COLS ; j++) {
				System.out.print(gameBoard[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	
}