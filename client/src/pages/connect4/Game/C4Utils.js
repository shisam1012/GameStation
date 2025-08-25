import { useState } from 'react';

// Utility function to show a simple alert message
export function popMessage(message) {
    alert(message);
}

// Custom hook to manage Connect 4 game state
export function useGameUtilsC4() {
    // Function to create an empty 6x7 board
    const createEmptyBoard = () =>
        Array(6)
            .fill(null)
            .map(() => Array(7).fill(0));

    // State: current game board
    const [board, setBoard] = useState(createEmptyBoard());

    // State: whether it is the player's turn
    const [isMyTurn, setIsMyTurn] = useState(false);

    // State: current status message for the player
    const [statusMessage, setStatusMessage] = useState(
        'waiting for another player...'
    );

    // State: whether the game has started
    const [gameStarted, setGameStarted] = useState(false);

    // Function to start the game
    // - board: initial board state
    // - yourTurn: boolean indicating if this player starts
    // - message: optional status message
    const startGame = (board, yourTurn, message) => {
        setBoard(board);
        setIsMyTurn(yourTurn);
        setStatusMessage(message);
        setGameStarted(true);
    };

    // Function to update the board after a move
    // - newBoard: updated board state
    // - yourTurn: boolean indicating if it is now this player's turn
    const updateBoard = (newBoard, yourTurn) => {
        setBoard(newBoard);
        setIsMyTurn(yourTurn);
    };

    // Function to end the game
    // - message: optional message to show
    const endGame = (message) => {
        setStatusMessage(message || 'Game over');
        setIsMyTurn(false);
        setGameStarted(false);
    };

    // Function to show an invalid move message
    const invalidMove = (message) => {
        alert(message || 'Illegal move');
    };

    // Return the state and the functions to manage the game
    return {
        board,
        isMyTurn,
        statusMessage,
        gameStarted,
        startGame,
        updateBoard,
        endGame,
        invalidMove,
    };
}
