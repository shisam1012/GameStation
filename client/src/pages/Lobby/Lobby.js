import { useState } from 'react';
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import Tooltip from 'react-bootstrap/Tooltip';
import { useNavigate } from 'react-router-dom';

import { onClickConnect4, onMouseDownConnect4 } from './LobbyUtils';

import Bar from '../../components/Bar/Bar';
import { UserLogin } from '../../context/LoginContext';

import '../../styles/Lobby.css';

//https://react-bootstrap.netlify.app/docs/components/overlays/#api

/**
 *
 * @returns the lobby page - includes the bar component, button for connect 4 info and the game button
 */
function Lobby() {
    const navigate = useNavigate();

    const [connect4ImageSrc, connect4SetImageSrc] = useState(
        './images/connect4.png'
    );
    const { userLoggedIn } = UserLogin();
    const [show, setShow] = useState(false);

    // The connect 4 info button text
    const renderTooltip = (props) => (
        <Tooltip {...props} className='info-tooltip'>
            <p>Can you connect four discs before your opponent? </p>
            <p>Take turns dropping discs into the board — </p>
            <p>
                the goal is to create a line of four in a row, column, or
                diagonal.
            </p>{' '}
            <p>
                <i>*You must be logged in to play.</i>
            </p>
        </Tooltip>
    );
    return (
        <div>
            <Bar />
            <figure>
                <div className='image-container'>
                    <img
                        className='image-button'
                        src={connect4ImageSrc}
                        alt=''
                        onMouseDown={() =>
                            onMouseDownConnect4(connect4SetImageSrc)
                        }
                        onClick={() =>
                            onClickConnect4(
                                connect4SetImageSrc,
                                navigate,
                                userLoggedIn
                            )
                        }
                    />
                    <OverlayTrigger
                        show={show}
                        overlay={renderTooltip}
                        placement='right'
                    >
                        <button
                            className='info-button'
                            onClick={() => setShow(!show)}
                        >
                            i
                        </button>
                    </OverlayTrigger>
                </div>
            </figure>

            {/*<div>
                {userLoggedIn
                    ? `Welcome, ${userLoggedIn.username}`
                    : 'Please log in'}
            </div>*/}
        </div>
    );
}

export default Lobby;
