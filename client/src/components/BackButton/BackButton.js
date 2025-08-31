import { useLocation, useNavigate } from 'react-router-dom';
import { goBack } from './BackButtonUtils';
import '../../styles/BackButton.css';
function BackButton() {
    const location = useLocation();
    const navigate = useNavigate();
    //returns a button that is used to go back to the last page
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
