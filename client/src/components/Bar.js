import './Bar.css';
import { useNavigate } from 'react-router-dom';
import { onClickSignUp } from './BarLogic';
function Bar() {
    const navigate = useNavigate();
    return (
        <div className='bar'>
            <p>logo place</p>
            <button>register</button>
            <button onClick={() => onClickSignUp(navigate)}>sign up</button>
        </div>
    );
}

export default Bar;
