import { useEffect, useCallback } from 'react';
import { useLocation } from 'react-router-dom';

export function useDisconnectOnLeave(socket, username) {
    const location = useLocation();

    // עטוף ב־useCallback כדי למנוע אזהרות ויציבות בזיכרון
    const sendLeave = useCallback(() => {
        if (socket && socket.readyState === WebSocket.OPEN) {
            console.log('>>> Sending leave...');
            socket.send(JSON.stringify({ type: 'leave', username }));
        }
    }, [socket, username]);

    // כשסוגרים את הדפדפן / רענון
    useEffect(() => {
        window.addEventListener('beforeunload', sendLeave);
        return () => {
            window.removeEventListener('beforeunload', sendLeave);
        };
    }, [sendLeave]);

    // כשעוזבים את הדף בתוך האתר
    useEffect(() => {
        const prevPath = location.pathname;
        return () => {
            if (prevPath === '/connect4/gameroom') {
                sendLeave();
            }
        };
    }, [location.pathname, sendLeave]);
}
