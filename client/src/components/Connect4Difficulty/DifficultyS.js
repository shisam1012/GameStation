//Using REST-API to send the register data to the server

export function playConnect4({ username, difficulty }) {
    const data = new URLSearchParams({
        username,
        difficulty,
    });

    return fetch(`http://localhost:8080/play-connect-4?${data.toString()}`, {
        method: 'GET',
    })
        .then((res) => {
            if (!res.ok) throw new Error('Server error');
            return res.json();
        })
        .then((response) => {
            console.log(response.message);

            const socket = new WebSocket('ws://localhost:8080/connect4-socket');

            socket.onopen = () => {
                console.log('WebSocket connected');
                socket.send(JSON.stringify({ type: 'init', username }));
            };

            socket.onmessage = (event) => {
                const message = JSON.parse(event.data);
                console.log('WebSocket message:', message);
            };
            socket.onerror = (error) => {
                console.error('WebSocket error:', error);
            };
            socket.onclose = () => {
                console.log('WebSocket closed');
            };

            //return socket;
        });
}
