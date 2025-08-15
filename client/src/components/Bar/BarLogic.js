//import { useNavigate } from 'react-router-dom';
import { AccessLoginContextProvider } from '../../context/LoginContext.js';

export function onClickSignUp(navigate) {
    //console.log('sign up clicked');
    navigate('/SignUp');
}

export function onClickLogin(navigate) {
    //console.log('login clicked');
    navigate('/Login');
}

export function onClickHighscore(navigate) {
    //console.log('login clicked');
    navigate('/Highscore');
}

export function onClickLogo(navigate) {
    //console.log('login clicked');
    navigate('/');
}

export function onClickSignOut(navigate) {
    //console.log('SignOut clicked');
    AccessLoginContextProvider.setUserLoggedIn();
    navigate('/');
}
