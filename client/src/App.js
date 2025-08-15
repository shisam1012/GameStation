import './App.css';
import Lobby from './pages/Lobby/Lobby.js';
import SignUp from './pages/Signup/SignUp.js';
import Login from './pages/Login/Login.js';
import Connect4Difficulty from './pages/connect4/Difficulty/Difficulty.js';
import GameRoomC4Wrapper from './pages/connect4/Game/GameRoomWrapper';
import HighscoreContainer from './pages/Highscore/HighscoreUtils.js';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
//import { LoginContext } from './context/LoginContext.js';

function App() {
    return (
        <div className='App-background'>
            <Router>
                <Routes>
                    <Route path='/' element={<Lobby />} />
                    <Route path='/SignUp' element={<SignUp />} />
                    <Route path='/Login' element={<Login />} />
                    <Route
                        path='/Connect4Difficulty'
                        element={<Connect4Difficulty />}
                    />
                    <Route path='/GameRoomC4' element={<GameRoomC4Wrapper />} />
                    <Route path='/Highscore' element={<HighscoreContainer />} />
                </Routes>
            </Router>
        </div>
    );
}

export default App;
