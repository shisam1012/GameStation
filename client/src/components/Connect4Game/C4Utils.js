import { useState } from 'react';

export function useGameUtilsC4(initialBoard) {
    const [board, setBoard] = useState(initialBoard);
    const [isMyTurn, setIsMyTurn] = useState(false);
    const [statusMessage, setStatusMessage] = useState('ממתין לשחקן נוסף...');
    const [gameStarted, setGameStarted] = useState(false);

    const startGame = (board, yourTurn, message) => {
        setBoard(board);
        setIsMyTurn(yourTurn);
        setStatusMessage(message || 'המשחק התחיל!');
        setGameStarted(true);
    };

    const updateBoard = (newBoard, yourTurn) => {
        setBoard(newBoard);
        setIsMyTurn(yourTurn);
    };

    const endGame = (message) => {
        setStatusMessage(message || 'המשחק הסתיים');
        setIsMyTurn(false);
        setGameStarted(false);
    };

    const invalidMove = (message) => {
        alert(message || 'מהלך לא חוקי');
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
