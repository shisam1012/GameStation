import { useEffect, useState } from 'react';

function GameRoomC4({ socket, username }) {
    const [statusMessage, setStatusMessage] = useState('ממתין לשחקן נוסף...');
    const [gameStarted, setGameStarted] = useState(false);

    useEffect(() => {
        if (!socket) return;

        const onMessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                if (data.type === 'match') {
                    setStatusMessage(data.message || 'נמצא שחקן!');
                    setGameStarted(true);
                    // בהמשך כאן נטען את הלוח למשל
                } else {
                    // הודעות רגילות
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

    return (
        <div>
            <h1>שלום, {username}!</h1>
            <p>{statusMessage}</p>
            {gameStarted && (
                <div>
                    {/* בהמשך נטען כאן את קומפוננט הלוח */}
                    <p>הלוח יוצג כאן בהמשך</p>
                </div>
            )}
        </div>
    );
}

export default GameRoomC4;
