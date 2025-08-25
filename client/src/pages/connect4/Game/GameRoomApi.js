import { useEffect } from 'react';
import { popMessage } from './C4Utils';

export function useGameRoomC4Api(socket, username, callbacks) {
    // Destructure callback functions for handling game state updates
    const { startGame, updateBoard, endGame, invalidMove } = callbacks;

    useEffect(() => {
        if (!socket) return; //if the socket is not available, do nothing

        //Define handler for incoming WebSocket messages
        const onMessage = (event) => {
            try {
                const data = JSON.parse(event.data); //parse the JSON data from the server

                //Handle different message types sent by the server
                switch (data.type) {
                    case 'match': //game start - initialize the board
                        startGame(
                            data.board ||
                                Array(6)
                                    .fill(null)
                                    .map(() => Array(7).fill(0)),
                            data.yourTurn,
                            data.message
                        );
                        break;

                    case 'boardUpdate': //update the board and turn status after a move
                        updateBoard(data.board, data.yourTurn);
                        break;

                    case 'gameOver': //notify the user the game ended
                        endGame(data.message);
                        break;

                    case 'invalidMove': //notify the user of an invalid move attempt
                        invalidMove(data.message);
                        break;

                    case 'duplicateError':
                        popMessage(data.message);
                        break;

                    case 'exception': //general server-side exception message
                        popMessage(data.message);
                        break;

                    case 'disconnectSocket':
                        endGame(data.message);
                        break;

                    default:
                        console.log('Unhandled message:', data);
                }
            } catch (err) {
                console.error('Error parsing WebSocket message:', err);
            }
        };

        socket.addEventListener('message', onMessage); //onMessage is in DifficultyApi where the socket is initialize
        return () => socket.removeEventListener('message', onMessage);
    }, [socket, startGame, updateBoard, endGame, invalidMove]);

    // Function to send a move to the server
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
