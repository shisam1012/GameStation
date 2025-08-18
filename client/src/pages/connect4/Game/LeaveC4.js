import { useEffect, useCallback } from 'react';
import { useLocation } from 'react-router-dom';

export function useDisconnectOnLeave(socket, username) {
    const location = useLocation();

    const sendLeave = useCallback(() => {
        if (socket && socket.readyState === WebSocket.OPEN) {
            console.log('>>> Sending leave...');
            socket.send(JSON.stringify({ type: 'leave', username }));
        }
    }, [socket, username]);

    //when closing the browser or reload the page
    useEffect(() => {
        window.addEventListener('beforeunload', sendLeave);
        return () => {
            window.removeEventListener('beforeunload', sendLeave);
        };
    }, [sendLeave]);

    //when leaving the gane page
    useEffect(() => {
        const prevPath = location.pathname;
        return () => {
            if (prevPath === '/connect4/gameroom') {
                sendLeave();
            }
        };
    }, [location.pathname, sendLeave]);
}
