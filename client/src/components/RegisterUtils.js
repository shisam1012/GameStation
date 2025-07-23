import { submitRegisterData } from './RegisterDB.js';
//import { UserLogin } from '../context/LoginContext';
import { useNavigate } from 'react-router-dom';
export function validateForm({ username, password }) {
    const errors = {};
    //const { userLoggedIn, setUserLoggedIn } = UserLogin();!!!
    if (!username) errors.username = 'Username is required';
    //if (!email) errors.email = 'Email is required';
    //else if (!validateEmail(email)) errors.email = 'Invalid email address';

    if (!password) errors.password = 'Password is required';
    // else if (password.length < 6) errors.password = 'Password too short';

    return errors;
}

export function handleSubmit({
    username,
    password,
    setUserLoggedIn,
    userLoggedIn,
    navigate,
}) {
    submitRegisterData({ username, password })
        .then((data) => {
            console.log('✅ Registered:', data);
            setUserLoggedIn({ username });
            console.log('.....');
            console.log(userLoggedIn);
            navigate('/');
        })
        .catch((err) => {
            console.error('❌ Error submitting data:', err.message);
        });
}

export function createOnSubmit(
    { username, password },
    setErrors,
    setUserLoggedIn,
    userLoggedIn,
    navigate
) {
    return function (e) {
        e.preventDefault();
        const errors = validateForm({
            username,
            password,
        });
        if (Object.keys(errors).length === 0) {
            handleSubmit({
                username,
                password,
                setUserLoggedIn,
                userLoggedIn,
                navigate,
            });
        } else {
            setErrors(errors);
        }
    };
}
