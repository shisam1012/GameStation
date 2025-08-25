import Bar from '../../components/Bar/Bar';
import BackButton from '../../components/BackButton/BackButton';
function Highscore({ topScores = [], userInfo = null }) {
    return (
        <div>
            <Bar />
            <BackButton />
            <h2>Top 10 Scores:</h2>
            {topScores.length === 0 ? (
                <p>No scores found.</p>
            ) : (
                <ul>
                    {topScores.map((player, index) => (
                        <li key={index}>
                            {player.username}: {player.totalScore} points
                        </li>
                    ))}
                </ul>
            )}

            <h2>Your Info:</h2>
            {userInfo ? (
                <p>
                    Games: {userInfo.gamesCount} | Wins: {userInfo.winsCount} |
                    Score: {userInfo.totalScore}
                </p>
            ) : (
                <p>No user info available.</p>
            )}
        </div>
    );
}

export default Highscore;
