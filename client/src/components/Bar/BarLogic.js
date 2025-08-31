import { AccessLoginContextProvider } from '../../context/LoginContext.js';

//Functions for handeling click events on the bar's buttons
// All the buttons are used for navigation in the website

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
    navigate('/'); //navigate to the lobby
}

export function onClickSignOut(navigate) {
    //console.log('SignOut clicked');
    AccessLoginContextProvider.setUserLoggedIn();
    navigate('/');
}
