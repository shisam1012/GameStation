//import logo from './logo.svg';
import './App.css';
//import Bar from './components/Bar.js';
import Lobby from './components/Lobby.js';
import SignUp from './components/SignUp.js';
import Login from './components/Login.js';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
//import { LoginContext } from './context/LoginContext.js';

function App() {
    return (
        <Router>
            <Routes>
                <Route path='/' element={<Lobby />} />
                <Route path='/SignUp' element={<SignUp />} />
                <Route path='/Login' element={<Login />} />
            </Routes>
        </Router>
    );
}

export default App;
