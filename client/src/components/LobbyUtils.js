export function onClickConnect4(navigate) {
    console.log('Connect4 clicked');
    navigate('/Connect4Difficulty');
}

export function onMouseDownConnect4(connect4SetImageSrc) {
    console.log('Connect4 mousedown');
    connect4SetImageSrc('./images/connect4pressed.png');
}
