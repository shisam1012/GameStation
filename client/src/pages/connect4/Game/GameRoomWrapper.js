import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import GameRoom from './GameRoom';
import { getSocket } from '../../../WebsocketStorage';

function GameRoomC4Wrapper() {
    const location = useLocation();
    const navigate = useNavigate();

    const socket = getSocket();
    const { username } = location.state || {};

    useEffect(() => {
        if (!socket || !username) {
            navigate('/');
        }
    }, [socket, username, navigate]);

    if (!socket || !username) return null;

    return <GameRoom key={username} socket={socket} username={username} />;
}
export default GameRoomC4Wrapper;
