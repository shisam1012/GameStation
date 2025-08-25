import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGameUtilsC4 } from './C4Utils';
import { useGameRoomC4Api } from './GameRoomApi';
import { UserLogin } from '../../../context/LoginContext';
import { getSocket } from '../../../WebsocketStorage';
import GameRoomUI from './GameRoomUI';

function GameRoom() {
    // Get logged-in user from context
    const { userLoggedIn } = UserLogin();
    const username = userLoggedIn?.username;
    // Retrieve existing socket instance from storage
    const socket = getSocket();

    const navigate = useNavigate();

    // Hook that manages game state (board, turn, status, etc.)
    const {
        board,
        isMyTurn,
        statusMessage,
        gameStarted,
        startGame,
        updateBoard,
        endGame,
        invalidMove,
    } = useGameUtilsC4();

    // Hook that sets up WebSocket listeners and provides sendMove function
    const { sendMove } = useGameRoomC4Api(socket, username, {
        startGame,
        updateBoard,
        endGame,
        invalidMove,
    });

    // Handles a column click by sending the move if it's the player's turn
    const handleColumnClick = (colIndex) => {
        if (isMyTurn) sendMove(colIndex);
    };

    // Handles when the player runs out of time - notify the server
    const handleTimeOut = () => {
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({ type: 'timeOut', username }));
        }
    };

    // Redirect to home page if there is no active socket
    useEffect(() => {
        if (!socket) {
            navigate('/');
        }
    }, [socket, navigate]);

    // Cleanup effect: when leaving the page, send a "leave" message if socket is open
    useEffect(() => {
        return () => {
            if (window.location.pathname === '/GameRoom') {
                return;
            }
            if (socket && socket.readyState === WebSocket.OPEN) {
                console.log('[UNMOUNT] sending leave');
                socket.send(JSON.stringify({ type: 'leave', username }));
            }
        };
    }, [socket, username]);

    // If user is not logged in, prompt to log in
    if (!userLoggedIn) {
        return <p>Please login to choose a difficulty.</p>;
    }

    // Render the UI component for the game room
    return (
        <GameRoomUI
            username={username}
            statusMessage={statusMessage}
            isMyTurn={isMyTurn}
            gameStarted={gameStarted}
            board={board}
            onColumnClick={handleColumnClick}
            onTimeOut={handleTimeOut}
        />
    );
}

export default GameRoom;
