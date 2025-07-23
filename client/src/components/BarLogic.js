//import { useNavigate } from 'react-router-dom';
import { AccessLoginContextProvider } from '../context/LoginContext.js';

export function onClickSignUp(navigate) {
    console.log('sign up clicked');
    navigate('/SignUp');
}

export function onClickLogin(navigate) {
    console.log('login clicked');
    navigate('/Login');
}

export function onClickSignOut() {
    console.log('SignOut clicked');
    AccessLoginContextProvider.setUserLoggedIn();
}
