import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import BackButton from '../../components/BackButton/BackButton.js';
import Bar from '../../components/Bar/Bar.js';
import { createOnSubmit } from './LoginUtils';

import '../../styles/Form.css';

/**
 *
 * @returns the login page - includes the bar component, back button, form with username and password fields and a submit button
 */
function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [errors, setErrors] = useState({});
    const navigate = useNavigate();
    const onSubmit = createOnSubmit(
        { username, password },
        setErrors,
        navigate
    );

    return (
        <div>
            <Bar />
            <BackButton />
            <div className='main-div'>
                <form onSubmit={onSubmit}>
                    <div className='input-field'>
                        <input
                            type='text'
                            placeholder='Username'
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <label>Enter your username:</label>
                    </div>

                    <div className='input-field'>
                        <input
                            type='password'
                            placeholder='Password'
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <label>Enter your password:</label>
                    </div>

                    <div>
                        <input
                            type='submit'
                            className='button-form'
                            value='Submit'
                        />
                    </div>
                </form>
            </div>
            {errors.username && (
                <div style={{ color: 'red' }}>{errors.username}</div>
            )}
            {errors.password && (
                <div style={{ color: 'red' }}>{errors.password}</div>
            )}
        </div>
    );
}

export default Login;
