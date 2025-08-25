let connect4Socket = null;

export function playConnect4({ username, difficulty }) {
    connect4Socket = new WebSocket(`ws://localhost:8080/connect4-socket`); //${data.toString()}`

    connect4Socket.onopen = () => {
        console.log('... IN onopen in DifficultyS ...');
        //console.log('WebSocket connected ✅');
        connect4Socket.send(
            JSON.stringify({ type: 'init', username, difficulty })
        );
        console.log('Sent init message');
    };

    connect4Socket.onmessage = (event) => {
        console.log('... IN onmessage in DifficultyS ...');
        const message = JSON.parse(event.data);
        console.log('WebSocket message:', message);
    };

    connect4Socket.onerror = (error) => {
        console.log('... IN onerror in DifficultyS ...');
        console.error('WebSocket error:', error);
    };

    connect4Socket.onclose = () => {
        console.log('... IN onclose in DifficultyS ...');
        console.log('WebSocket closed');
    };
    console.log('READY STATE:', connect4Socket.readyState);
    return connect4Socket;
}

export function onInit({ username, difficulty }) {
    console.log('... IN onInit in DifficultyS ...');
    connect4Socket.send(JSON.stringify({ type: 'init', username, difficulty }));
    console.log('Sent init message');
}
