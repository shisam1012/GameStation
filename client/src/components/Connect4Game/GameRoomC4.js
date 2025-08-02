import { useGameUtilsC4 } from './C4Utils';
import { useGameRoomC4S } from './GameRoomC4S';
import GameBoardUIC4 from './C4BoardUI';

function GameRoomC4({ socket, username }) {
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

/*import { useEffect, useState } from 'react';

function GameRoomC4({ socket, username }) {
    const [statusMessage, setStatusMessage] = useState('ממתין לשחקן נוסף...');
    const [gameStarted, setGameStarted] = useState(false);
    const [board, setBoard] = useState(
        Array(6)
            .fill(null)
            .map(() => Array(7).fill(0))
    );
    const [isMyTurn, setIsMyTurn] = useState(false);

    const handleColumnClick = (colIndex) => {
        if (!isMyTurn || !socket) return;

        socket.send(
            JSON.stringify({
                type: 'move',
                column: colIndex,
                username: username,
            })
        );
    };

    useEffect(() => {
        if (!socket) return;

        const onMessage = (event) => {
            try {
                console.log('... IN onMessage in GameRoomC4 ...');
                const data = JSON.parse(event.data);

                if (data.type === 'match') {
                    setStatusMessage(data.message || 'נמצא שחקן!');
                    setGameStarted(true);
                    if (data.board) setBoard(data.board);
                    if (data.yourTurn !== undefined) setIsMyTurn(data.yourTurn);
                } else if (data.type === 'boardUpdate') {
                    if (data.board) setBoard(data.board);
                    if (data.yourTurn !== undefined) setIsMyTurn(data.yourTurn);
                } else if (data.type === 'gameOver') {
                    setStatusMessage(data.message || 'המשחק הסתיים');
                    setIsMyTurn(false);
                } else if (data.type === 'invalidMove') {
                    alert(data.message || 'מהלך לא חוקי');
                } else {
                    console.log('Unhandled message:', data);
                }
            } catch (err) {
                console.error('Error parsing WebSocket message:', err);
            }
        };

        socket.addEventListener('message', onMessage);

        return () => {
            socket.removeEventListener('message', onMessage);
        };
    }, [socket]);

    const renderCell = (value, rowIndex, colIndex) => {
        let color = 'white';
        if (value === 1) color = 'red';
        if (value === 2) color = 'yellow';

        const isClickable = rowIndex === 0 && isMyTurn;

        return (
            <td
                key={colIndex}
                onClick={
                    isClickable ? () => handleColumnClick(colIndex) : undefined
                }
                style={{
                    width: '50px',
                    height: '50px',
                    backgroundColor: color,
                    border: '1px solid black',
                    cursor: isClickable ? 'pointer' : 'default',
                }}
            ></td>
        );
    };

    const renderBoard = () => {
        return (
            <table style={{ borderCollapse: 'collapse' }}>
                <tbody>
                    {board.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            {row.map((cell, colIndex) =>
                                renderCell(cell, rowIndex, colIndex)
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };

    return (
        <div>
            <h1>hello {username}!</h1>
            <p>{statusMessage}</p>
            {gameStarted && (
                <div>
                    <h2>
                        {isMyTurn
                            ? 'your turn'
                            : "waiting for your opponent's move"}
                    </h2>
                    {renderBoard()}
                </div>
            )}
        </div>
    );
}

export default GameRoomC4;
*/
