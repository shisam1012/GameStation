//import logo from './logo.svg';
import './App.css';
//import Bar from './components/Bar.js';
import Lobby from './components/Lobby.js';
import SignUp from './components/Signup/SignUp.js';
import Login from './components/Login/Login.js';
import Connect4Difficulty from './components/Connect4Difficulty/Connect4Difficulty.js';
import Connect4Game from './components/Connect4Game/Connect4Game.js';
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
                    <Route path='/Connect4Game' element={<Connect4Game />} />
                </Routes>
            </Router>
        </div>
    );
}

export default App;
