import { useEffect } from 'react';
import { popMessage } from './C4Utils';
export function useGameRoomC4S(socket, username, callbacks) {
    const { startGame, updateBoard, endGame, invalidMove } = callbacks;

    useEffect(() => {
        if (!socket) return;

        const onMessage = (event) => {
            try {
                const data = JSON.parse(event.data);

                switch (data.type) {
                    case 'match':
                        startGame(
                            data.board ||
                                Array(6)
                                    .fill(null)
                                    .map(() => Array(7).fill(0)),
                            data.yourTurn,
                            data.message
                        );
                        break;
                    case 'boardUpdate':
                        updateBoard(data.board, data.yourTurn);
                        break;
                    case 'gameOver':
                        endGame(data.message);
                        break;
                    case 'invalidMove':
                        invalidMove(data.message);
                        break;
                    case 'duplicateError':
                        popMessage(data.message);
                        break;
                    case 'exception':
                        popMessage(data.message);
                        break;
                    default:
                        console.log('Unhandled message:', data);
                }
            } catch (err) {
                console.error('Error parsing WebSocket message:', err);
            }
        };

        socket.addEventListener('message', onMessage);
        return () => socket.removeEventListener('message', onMessage);
    }, [socket, startGame, updateBoard, endGame, invalidMove]);

    const sendMove = (colIndex) => {
        if (!socket) return;
        socket.send(
            JSON.stringify({
                type: 'move',
                column: colIndex,
                username,
            })
        );
    };

    return { sendMove };
}
