import '../../../styles/C4Board.css';
import Bar from '../../../components/Bar/Bar';
import BackButton from '../../../components/BackButton/BackButton';
import Timer from '../../../components/Timer';
import GameBoardUIC4 from './C4BoardUI';

//GameRoomUI: handles the UI of the Connect 4 game room
// Props:
// - username: current player's username
// - statusMessage: message to display game status
// - isMyTurn: boolean, true if it is the player's turn
// - gameStarted: boolean, true if the game has started
// - board: 2D array representing the current board state
// - onColumnClick: function to call when a column is clicked
// - onTimeOut: function to call when the timer reaches 0
function GameRoomUI({
    username,
    statusMessage,
    isMyTurn,
    gameStarted,
    board,
    onColumnClick,
    onTimeOut,
}) {
    return (
        <div>
            <Bar />
            <BackButton />
            <h1>Hello {username}!</h1>
            <p>{statusMessage}</p>
            {gameStarted && (
                <>
                    {/* Display whose turn it is */}
                    <h2>
                        {isMyTurn
                            ? 'Your turn - click on the upper row to select where to drop your disc'
                            : "Waiting for your opponent's move"}
                    </h2>
                    {/* Timer component handles turn time */}
                    <Timer
                        initialTime={30}
                        isMyTurn={isMyTurn}
                        onTimeOut={onTimeOut}
                    />
                    {/* Game board UI */}
                    <GameBoardUIC4
                        board={board}
                        isMyTurn={isMyTurn}
                        onColumnClick={onColumnClick}
                    />
                </>
            )}
        </div>
    );
}

export default GameRoomUI;
