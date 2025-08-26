export function onClickConnect4(connect4SetImageSrc, navigate, userLoggedIn) {
    //console.log('Connect4 clicked');
    if (userLoggedIn) navigate('/Connect4Difficulty');
    else {
        alert('You must log in to play the game :)  ->');
        connect4SetImageSrc('./images/connect4.png');
    }
}

export function onMouseDownConnect4(connect4SetImageSrc) {
    //console.log('Connect4 mousedown');
    connect4SetImageSrc('./images/connect4pressed.png');
}
