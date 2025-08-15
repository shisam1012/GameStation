export async function getTopScores() {
    const res = await fetch('http://localhost:8080/api/scores/top10'); // GET
    if (!res.ok) throw new Error('Failed to fetch top scores');
    return res.json();
}

export async function getUserInfo(username) {
    const params = new URLSearchParams({ username });
    const res = await fetch(
        `http://localhost:8080/api/scores/userinfo?${params.toString()}`,
        { method: 'GET' }
    );
    const data = await res.json();
    console.log('scores:', data);

    if (!res.ok) {
        throw new Error(data.error || 'Unknown server error');
    }

    return data;
}
