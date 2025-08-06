import { createOnSubmit } from './SignupUtils';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Bar from '../Bar';
function SignUp() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState('');
    const [email, setEmail] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();
    const onSubmit = createOnSubmit(
        { username, password, email, repeatedPassword, navigate },
        setErrors
    );

    return (
        <div>
            <Bar />
            <p>logo place</p>

            <p>Sign up page!</p>

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
                        type='email'
                        placeholder='Email'
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    {errors.email && (
                        <div style={{ color: 'red' }}>{errors.email}</div>
                    )}

                    <input
                        type='password'
                        placeholder='Password'
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <input
                        type='password'
                        placeholder=' Repeat password'
                        value={repeatedPassword}
                        onChange={(e) => setRepeatedPassword(e.target.value)}
                    />
                    {errors.password && (
                        <div style={{ color: 'red' }}>{errors.password}</div>
                    )}
                    {errors.repeatedpassword && (
                        <div style={{ color: 'red' }}>
                            {errors.repeatedpassword}
                        </div>
                    )}

                    <input type='submit' value='Submit' />
                </form>
            </div>
        </div>
    );
}

export default SignUp;
