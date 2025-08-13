import { useLocation, useNavigate } from 'react-router-dom';
import { goBack } from './BackButtonUtils';
import './BackButton.css';
function BackButton() {
    const location = useLocation();
    const navigate = useNavigate();

    return (
        <div className='back-button-container'>
            <button
                className='back-button'
                onClick={() => goBack(navigate, location)}
            >
                🔙
            </button>
        </div>
    );
}

export default BackButton;
