function GameBoardUIC4({ board, isMyTurn, onColumnClick }) {
    const renderCell = (value, rowIndex, colIndex) => {
        let color = 'white';
        if (value === 1) color = 'red';
        if (value === 2) color = 'yellow';

        const isClickable = rowIndex === 0 && isMyTurn;

        return (
            <td
                key={colIndex}
                onClick={
                    isClickable ? () => onColumnClick(colIndex) : undefined
                }
                style={{
                    width: '50px',
                    height: '50px',
                    backgroundColor: color,
                    border: '1px solid black',
                    cursor: isClickable ? 'pointer' : 'default',
                }}
            />
        );
    };

    return (
        <table style={{ borderCollapse: 'collapse' }}>
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
    );
}

export default GameBoardUIC4;
