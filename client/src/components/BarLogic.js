import { useNavigate } from 'react-router-dom';

export function onClickSignUp(navigate) {
    console.log('sign up clicked');
    navigate('/SignUp');
}
