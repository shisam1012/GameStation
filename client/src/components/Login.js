import { createOnSubmit } from './LoginUtils';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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
            <p>logo place</p>

            <p>Register page!</p>

            <div>
                <form onSubmit={onSubmit}>
                    <input
                        type='text'
                        placeholder='Username'
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    {errors.username && (
                        <div style={{ color: 'red' }}>{errors.username}</div>
                    )}

                    <input
                        type='password'
                        placeholder='Password'
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {errors.password && (
                        <div style={{ color: 'red' }}>{errors.password}</div>
                    )}

                    <input type='submit' value='Submit' />
                </form>
            </div>
        </div>
    );
}

export default Login;
