import '../../../styles/C4Board.css';

function GameBoardUIC4({ board, isMyTurn, onColumnClick }) {
    const renderCell = (value, rowIndex, colIndex) => {
        let colorClass = 'cell-white';
        if (value === 1) colorClass = 'cell-red';
        if (value === 2) colorClass = 'cell-yellow';

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
