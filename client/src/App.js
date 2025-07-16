//import logo from './logo.svg';
import './App.css';
//import Bar from './components/Bar.js';
import Lobby from './components/Lobby.js';
import SignUp from './components/SignUp.js';
import Register from './components/Register.js';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
    return (
        <Router>
            <Routes>
                <Route path='/' element={<Lobby />} />
                <Route path='/SignUp' element={<SignUp />} />
                <Route path='/Register' element={<Register />} />
            </Routes>
        </Router>
    );
}

export default App;
