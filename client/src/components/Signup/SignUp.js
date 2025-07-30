import { createOnSubmit } from './SignupUtils';
import { useState } from 'react';

function SignUp() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [errors, setErrors] = useState({});

    const onSubmit = createOnSubmit({ username, password, email }, setErrors);

    return (
        <div>
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
                    {errors.password && (
                        <div style={{ color: 'red' }}>{errors.password}</div>
                    )}

                    <input type='submit' value='Submit' />
                </form>
            </div>
        </div>
    );
}

export default SignUp;
