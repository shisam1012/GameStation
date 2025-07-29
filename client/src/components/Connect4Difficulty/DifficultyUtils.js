import { playConnect4, onInit } from './DifficultyS';
import { setSocket } from '../../WebsocketStorage';
//import { AccessLoginContextProvider } from '../../context/LoginContext.js';

export function createOnSubmit(difficulty, navigate, username) {
    return function (e) {
        e.preventDefault();
        console.log('submitting difficulty', { difficulty });

        const socket = playConnect4({ username, difficulty });
        console.log('READY STATE:', socket.readyState);
        socket.onopen = () => {
            setSocket(socket);
            console.log('READY STATE:', socket.readyState);
            onInit({ username, difficulty });
            navigate('/GameRoomC4', { state: { username } });
        };

        socket.onerror = (err) => {
            console.error('WebSocket error:', err.message);
        };
    };
}

export function createOnSubmit1(difficulty, navigate, username) {
    return function (e) {
        //without this the function will be called when the Connect4Difficulty would render
        e.preventDefault();
        console.log('submitting difficulty', { difficulty });
        //navigate('/');
        playConnect4({ username, difficulty })
            .then((socket) => {
                setSocket(socket);
                navigate('/GameRoomC4', { state: { username } });
            })
            /*.then((data) => {
                console.log('✅', data);
                //AccessLoginContextProvider.setUserLoggedIn({ username });
                console.log('.....');
                navigate('/');
            })*/
            .catch((err) => {
                console.error('❌ Error submitting data:', err.message);
            });
    };
}
