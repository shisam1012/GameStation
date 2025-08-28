//This function is called when the connect 4 button is clicked
export function onClickConnect4(connect4SetImageSrc, navigate, userLoggedIn) {
    //console.log('Connect4 clicked');
    //if the use is logged in - can navigate to the difficulty selection page, otherwise alert a message
    if (userLoggedIn) navigate('/Connect4Difficulty');
    else {
        alert('You must log in to play the game :)  ->');
        connect4SetImageSrc('./images/connect4.png');
    }
}

//This function is called on mouse down on the connect 4 button - only changes the image of the button
export function onMouseDownConnect4(connect4SetImageSrc) {
    //console.log('Connect4 mousedown');
    connect4SetImageSrc('./images/connect4pressed.png');
}
