import { useState } from 'react';

export function popMessage(message) {
    alert(message);
}

export function useGameUtilsC4(initialBoard) {
    const [board, setBoard] = useState(initialBoard);
    const [isMyTurn, setIsMyTurn] = useState(false);
    const [statusMessage, setStatusMessage] = useState(
        'waiting for another player...'
    );
    const [gameStarted, setGameStarted] = useState(false);

    const startGame = (board, yourTurn, message) => {
        setBoard(board);
        setIsMyTurn(yourTurn);
        setStatusMessage(message || 'Game is starting :)');
        setGameStarted(true);
    };

    const updateBoard = (newBoard, yourTurn) => {
        setBoard(newBoard);
        setIsMyTurn(yourTurn);
    };

    const endGame = (message) => {
        setStatusMessage(message || 'Game over');
        setIsMyTurn(false);
        setGameStarted(false);
        //!!!!!!!!add score!!!!!!
    };

    const invalidMove = (message) => {
        alert(message || 'Illegael move');
    };

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
