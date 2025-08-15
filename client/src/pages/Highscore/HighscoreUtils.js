import { useEffect, useState, useContext } from 'react';
import Highscore from './HighscoreUI';
import { getTopScores, getUserInfo } from './HighscoreApi';
import { LoginContext } from '../../context/LoginContext';
function HighscoreContainer() {
    const [topScores, setTopScores] = useState([]);
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { userLoggedIn } = useContext(LoginContext);

    const username = userLoggedIn?.username;

    useEffect(() => {
        if (!username) {
            setLoading(false);
            setTopScores([]);
            setUserInfo(null);
            return;
        }

        async function fetchData() {
            try {
                setLoading(true);
                const [scores, info] = await Promise.all([
                    getTopScores(),
                    getUserInfo(username),
                ]);
                setTopScores(scores);
                setUserInfo(info);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        fetchData();
    }, [username]);

    if (!userLoggedIn) {
        return <p>Please login to see your info.</p>;
    }

    if (loading) return <p>Loading...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return <Highscore topScores={topScores} userInfo={userInfo} />;
}

export default HighscoreContainer;
