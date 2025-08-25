import { createOnSubmit } from './SignupUtils';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Bar from '../../components/Bar/Bar.js';
import '../../styles/Form.css';
import BackButton from '../../components/BackButton/BackButton.js';
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
                            type='email'
                            placeholder='Email'
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />

                        <label>Enter your email:</label>
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
                    <div className='input-field'>
                        <input
                            type='password'
                            placeholder=' Repeat password'
                            value={repeatedPassword}
                            onChange={(e) =>
                                setRepeatedPassword(e.target.value)
                            }
                        />
                        <label>Enter your password again please:</label>
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
            {errors.email && <div style={{ color: 'red' }}>{errors.email}</div>}
            {errors.password && (
                <div style={{ color: 'red' }}>{errors.password}</div>
            )}
            {errors.repeatedpassword && (
                <div style={{ color: 'red' }}>{errors.repeatedpassword}</div>
            )}
        </div>
    );
}

export default SignUp;
