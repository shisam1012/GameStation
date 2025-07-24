import { playConnect4 } from './DifficultyS';
//import { AccessLoginContextProvider } from '../../context/LoginContext.js';

export function createOnSubmit(difficulty, navigate, username) {
    return function (e) {
        //without this the function will be called when the Connect4Difficulty would render
        e.preventDefault();
        console.log('submitting difficulty', { difficulty });
        //navigate('/');
        playConnect4({ username, difficulty })
            .then((data) => {
                console.log('✅', data);
                //AccessLoginContextProvider.setUserLoggedIn({ username });
                console.log('.....');
                navigate('/');
            })
            .catch((err) => {
                console.error('❌ Error submitting data:', err.message);
            });
    };
}
