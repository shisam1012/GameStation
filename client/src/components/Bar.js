import './Bar.css';
import { useNavigate } from 'react-router-dom';
import {
    onClickSignUp,
    onClickLogin,
    onClickSignOut,
    onClickLogo,
} from './BarLogic';
import { UserLogin } from '../context/LoginContext';
//import logo from '../images/logo2.png';

function Bar() {
    const navigate = useNavigate();
    const { userLoggedIn } = UserLogin();

    return (
        <div className='bar'>
            <img
                src='/images/logo2.png'
                alt='Logo'
                className='logo-image'
                onClick={() => onClickLogo(navigate)}
            />

            {/*<p>logo place</p>*/}
            <div>
                {userLoggedIn ? (
                    <>
                        <button
                            className='button'
                            onClick={() => onClickLogin(navigate)}
                        >
                            highscore
                        </button>
                        <button
                            className='button'
                            onClick={() => onClickSignOut()}
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
