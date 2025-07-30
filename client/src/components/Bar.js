import './Bar.css';
import { useNavigate } from 'react-router-dom';
import { onClickSignUp, onClickLogin, onClickSignOut } from './BarLogic';
import { UserLogin } from '../context/LoginContext';

function Bar() {
    const navigate = useNavigate();
    const { userLoggedIn } = UserLogin();

    return (
        <div className='bar'>
            <p>logo place</p>
            {userLoggedIn ? (
                <>
                    <button onClick={() => onClickLogin(navigate)}>
                        highscore
                    </button>
                    <button onClick={() => onClickSignOut()}>sign out</button>
                </>
            ) : (
                <>
                    <button onClick={() => onClickLogin(navigate)}>
                        login
                    </button>
                    <button onClick={() => onClickSignUp(navigate)}>
                        sign up
                    </button>
                </>
            )}
        </div>
    );
}

export default Bar;
