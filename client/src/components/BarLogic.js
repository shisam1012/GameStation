import { useNavigate } from 'react-router-dom';

export function onClickSignUp(navigate) {
    console.log('sign up clicked');
    navigate('/SignUp');
}

export function onClickRegister(navigate) {
    console.log('register clicked');
    navigate('/Register');
}
