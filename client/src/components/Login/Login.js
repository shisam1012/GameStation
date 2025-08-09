import { createOnSubmit } from './LoginUtils';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Bar from '../Bar';
import '../../CSS/form.css';

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
