import { useGameUtilsC4 } from './C4Utils';
import { useGameRoomC4S } from './GameRoomApi';
import GameBoardUIC4 from './C4BoardUI';
import { useEffect } from 'react';
//import { useLocation } from 'react-router-dom';
import { useDisconnectOnLeave } from './LeaveC4';
import '../../../styles/C4Board.css';
import Bar from '../../../components/Bar/Bar';
import BackButton from '../../../components/BackButton/BackButton';
import Timer from '../../../components/Timer';
function GameRoom({ socket, username }) {
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

    const handleTimeOut = () => {
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({ type: 'timeOut', username }));
        }

        //alert("Time's up! You lost your turn.");
    };

    return (
        <div>
            <Bar />
            <BackButton />
            <h1>hello {username}!</h1>
            <p>{statusMessage}</p>
            {gameStarted && (
                <>
                    <h2>
                        {isMyTurn
                            ? 'your turn'
                            : "waiting for your opponent's move"}
                    </h2>
                    <Timer
                        initialTime={30}
                        isMyTurn={isMyTurn}
                        onTimeOut={handleTimeOut}
                    />
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

export default GameRoom;
