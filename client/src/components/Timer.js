import { useEffect, useState } from 'react';

function Timer({ initialTime = 30, isMyTurn, onTimeOut }) {
    const [timeLeft, setTimeLeft] = useState(initialTime);

    useEffect(() => {
        // Reset timer whenever turn changes
        setTimeLeft(initialTime);
    }, [isMyTurn, initialTime]);

    useEffect(() => {
        if (!isMyTurn) return;

        const interval = setInterval(() => {
            setTimeLeft((prev) => {
                if (prev <= 1) {
                    clearInterval(interval);
                    onTimeOut();
                    return 0;
                }
                return prev - 1;
            });
        }, 1000);

        return () => clearInterval(interval);
    }, [isMyTurn, onTimeOut]);

    return (
        <div className='turn-timer'>
            <p>Time left: {timeLeft}s</p>
        </div>
    );
}

export default Timer;
