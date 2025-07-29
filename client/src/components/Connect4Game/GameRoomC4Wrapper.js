import { useLocation, useNavigate } from 'react-router-dom';
import GameRoomC4 from './GameRoomC4';
import { getSocket } from '../../WebsocketStorage';

function GameRoomC4Wrapper() {
    const location = useLocation();
    const navigate = useNavigate();

    const socket = getSocket();
    const { username } = location.state || {};

    if (!socket || !username) {
        navigate('/');
        return null;
    }

    return <GameRoomC4 socket={socket} username={username} />;
}

export default GameRoomC4Wrapper;
