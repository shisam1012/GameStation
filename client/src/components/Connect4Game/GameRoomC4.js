import { useGameUtilsC4 } from './C4Utils';
import { useGameRoomC4S } from './GameRoomC4S';
import GameBoardUIC4 from './C4BoardUI';
import { useEffect } from 'react';
//import { useLocation } from 'react-router-dom';
import { useDisconnectOnLeave } from './LeaveC4';

function GameRoomC4({ socket, username }) {
    //const location = useLocation();
    console.log('[MOUNT] GameRoomC4 mounted');
    useEffect(() => {
        return () => {
            console.log('[UNMOUNT] GameRoomC4 unmounted');
        };
    }, []);

    useDisconnectOnLeave(socket, username);
    const {
        board,
        isMyTurn,
        statusMessage,
        gameStarted,
        startGame,
        updateBoard,
        endGame,
        invalidMove,
    } = useGameUtilsC4(
        Array(6)
            .fill(null)
            .map(() => Array(7).fill(0))
    );

    const { sendMove } = useGameRoomC4S(socket, username, {
        startGame,
        updateBoard,
        endGame,
        invalidMove,
    });

    const handleColumnClick = (colIndex) => {
        if (!isMyTurn) return;
        sendMove(colIndex);
    };

    useEffect(() => {
        return () => {
            if (window.location.pathname === '/GameRoomC4') {
                return;
            }
            if (socket && socket.readyState === WebSocket.OPEN) {
                console.log('[UNMOUNT] sending leave');
                socket.send(JSON.stringify({ type: 'leave', username }));
            }
        };
    }, [socket, username]);

    return (
        <div>
            <h1>hello {username}!</h1>
            <p>{statusMessage}</p>
            {gameStarted && (
                <>
                    <h2>
                        {isMyTurn
                            ? 'your turn'
                            : "waiting for your opponent's move"}
                    </h2>
                    <GameBoardUIC4
                        board={board}
                        isMyTurn={isMyTurn}
                        onColumnClick={handleColumnClick}
                    />
                </>
            )}
        </div>
    );
}

export default GameRoomC4;
