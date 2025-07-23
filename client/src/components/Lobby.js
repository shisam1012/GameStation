import Bar from './Bar';
import { useEffect } from 'react';
import { UserLogin } from '../context/LoginContext';
function Lobby() {
    const { userLoggedIn } = UserLogin();
    useEffect(() => {
        console.log('userLoggedIn updated:', userLoggedIn);
    }, [userLoggedIn]);
    return (
        <div>
            <Bar />
            <p>logo place</p>
            <div>
                <p>
                    text text text text text text text text text text t e x t t
                    e x t
                </p>
            </div>
            <div>
                {userLoggedIn
                    ? `Welcome, ${userLoggedIn.username}`
                    : 'Please log in'}
            </div>
        </div>
    );
}

export default Lobby;
