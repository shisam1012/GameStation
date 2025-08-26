import '../../styles/Bar.css';
import { useNavigate } from 'react-router-dom';
import {
    onClickSignUp,
    onClickLogin,
    onClickSignOut,
    onClickLogo,
    onClickHighscore,
} from './BarLogic.js';
import { UserLogin } from '../../context/LoginContext.js';

function Bar() {
    const navigate = useNavigate();
    const { userLoggedIn } = UserLogin();

    return (
        <div className='bar'>
            {/*the logo on the left is also a button - navigates back to the lobby*/}
            <img
                src='/images/logo2.png'
                alt='Logo'
                className='logo-image'
                onClick={() => onClickLogo(navigate)}
            />

            <div>
                {/*if the user is logged in - show highscore and log out buttons, otherwise show login and signup buttons */}
                {userLoggedIn ? (
                    <>
                        <button
                            className='button'
                            onClick={() => onClickHighscore(navigate)}
                        >
                            highscore
                        </button>
                        <button
                            className='button'
                            onClick={() => onClickSignOut(navigate)}
                        >
                            sign out
                        </button>
                    </>
                ) : (
                    <>
                        <button
                            className='button'
                            onClick={() => onClickLogin(navigate)}
                        >
                            login
                        </button>
                        <button
                            className='button'
                            onClick={() => onClickSignUp(navigate)}
                        >
                            sign up
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}

export default Bar;
