let socket = null;

export function setSocket(ws) {
    socket = ws;
}

export function getSocket() {
    return socket;
}
