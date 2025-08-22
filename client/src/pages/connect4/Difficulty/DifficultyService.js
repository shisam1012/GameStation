import { playConnect4, onInit } from './DifficultyApi';
import { setSocket } from '../../../WebsocketStorage';
//import { LoginContext } from '../../context/LoginContext';

export function createOnSubmit(difficulty, navigate, username, location) {
    //const { userLoggedIn } = useContext(LoginContext);

    // const username = userLoggedIn?.username;

    return function (e) {
        e.preventDefault();
        console.log('submitting difficulty', { difficulty });

        const socket = playConnect4({ username, difficulty });
        console.log('READY STATE:', socket.readyState);
        socket.onopen = () => {
            setSocket(socket);
            console.log('READY STATE:', socket.readyState);
            onInit({ username, difficulty });

            navigate('/GameRoom', {
                state: { from: location.pathname },
            });
        };

        socket.onerror = (err) => {
            console.log('... IN onerror in DifficultyUtils ...');
            console.error('WebSocket error:', err.message);
        };
    };
}
