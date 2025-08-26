import '../../../styles/C4Board.css';

// GameBoardUIC4: renders the Connect 4 board
// Props:
// - board: 2D array representing the game state (0 = empty, 1 = red, 2 = yellow)
// - isMyTurn: boolean, true if the current player can make a move
// - onColumnClick: function to call when a column is clicked
function GameBoardUIC4({ board, isMyTurn, onColumnClick }) {
    // Render a single cell in the board
    const renderCell = (value, rowIndex, colIndex) => {
        // Determine the cell color based on value
        let colorClass = 'cell-white';
        if (value === 1) colorClass = 'cell-red';
        if (value === 2) colorClass = 'cell-yellow';

        //only the top row is clickable, and only if it's the player's turn
        const isClickable = rowIndex === 0 && isMyTurn;

        return (
            <td
                key={colIndex}
                onClick={
                    isClickable ? () => onColumnClick(colIndex) : undefined
                }
                className={`game-cell ${colorClass} ${
                    isClickable ? 'cell-clickable' : 'cell-default'
                }`}
            />
        );
    };

    return (
        <div className='board-container'>
            <table className='game-board'>
                <tbody>
                    {board.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            {row.map((cell, colIndex) =>
                                renderCell(cell, rowIndex, colIndex)
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default GameBoardUIC4;
