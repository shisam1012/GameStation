//Using REST-API to send the register data to the server

export function playConnect4({ username, difficulty }) {
    const data = new URLSearchParams({
        username,
        difficulty,
    });

    return fetch(`http://localhost:8080/play-connect-4?${data.toString()}`, {
        method: 'GET',
    }).then((res) => {
        if (!res.ok) throw new Error('Server error');
        return res.json();
    });
}
