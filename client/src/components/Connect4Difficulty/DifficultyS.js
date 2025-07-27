//Using REST-API to send the register data to the server

let connect4Socket = null;

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

            connect4Socket = new WebSocket(
                'ws://localhost:8080/connect4-socket'
            );

            connect4Socket.onopen = () => {
                console.log('WebSocket connected');
                connect4Socket.send(JSON.stringify({ type: 'init', username }));
            };

            connect4Socket.onmessage = (event) => {
                const message = JSON.parse(event.data);
                console.log('WebSocket message:', message);
            };

            connect4Socket.onerror = (error) => {
                console.error('WebSocket error:', error);
            };

            connect4Socket.onclose = () => {
                console.log('WebSocket closed');
            };

            return connect4Socket;
        });
}
