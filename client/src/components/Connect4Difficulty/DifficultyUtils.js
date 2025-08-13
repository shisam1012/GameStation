import { playConnect4, onInit } from './DifficultyS';
import { setSocket } from '../../WebsocketStorage';

export function createOnSubmit(difficulty, navigate, username, location) {
    return function (e) {
        e.preventDefault();
        console.log('submitting difficulty', { difficulty });

        const socket = playConnect4({ username, difficulty });
        console.log('READY STATE:', socket.readyState);
        socket.onopen = () => {
            setSocket(socket);
            console.log('READY STATE:', socket.readyState);
            onInit({ username, difficulty });
            navigate('/GameRoomC4', {
                state: { username, from: location.pathname },
            });
        };

        socket.onerror = (err) => {
            console.log('... IN onerror in DifficultyUtils ...');
            console.error('WebSocket error:', err.message);
        };
    };
}
