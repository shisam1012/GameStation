import './Bar.css';
import { useNavigate } from 'react-router-dom';
import { onClickSignUp, onClickLogin } from './BarLogic';
function Bar() {
    const navigate = useNavigate();
    return (
        <div className='bar'>
            <p>logo place</p>
            <button onClick={() => onClickLogin(navigate)}>login</button>
            <button onClick={() => onClickSignUp(navigate)}>sign up</button>
        </div>
    );
}

export default Bar;
