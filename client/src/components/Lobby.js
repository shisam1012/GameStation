import Bar from './Bar';
import './Lobby.css';
import { useNavigate } from 'react-router-dom';
import { /*useEffect,*/ useState } from 'react';
//import { UserLogin } from '../context/LoginContext';
import { onClickConnect4, onMouseDownConnect4 } from './LobbyUtils';
function Lobby() {
    //const { userLoggedIn } = UserLogin();
    const navigate = useNavigate();
    /*useEffect(() => {
        console.log('userLoggedIn updated:', userLoggedIn);
    }, [userLoggedIn]);*/
    const [connect4ImageSrc, connect4SetImageSrc] = useState(
        './images/connect4.png'
    );

    /*<div className='lobby-background'>*/

    return (
        <div>
            <Bar />

            <img
                className='image-button'
                src={connect4ImageSrc}
                alt=''
                onMouseDown={() => onMouseDownConnect4(connect4SetImageSrc)}
                onClick={() => onClickConnect4(navigate)}
            />
            {/*<div>
                {userLoggedIn
                    ? `Welcome, ${userLoggedIn.username}`
                    : 'Please log in'}
            </div>*/}
        </div>
    );
}

export default Lobby;
