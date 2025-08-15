import { submitLoginData } from './LoginApi.js';
import { AccessLoginContextProvider } from '../../context/LoginContext.js';

export function validateForm({ username, password }) {
    const errors = {};

    if (!username) errors.username = 'Username is required';
    //if (!email) errors.email = 'Email is required';
    //else if (!validateEmail(email)) errors.email = 'Invalid email address';

    if (!password) errors.password = 'Password is required';
    // else if (password.length < 6) errors.password = 'Password too short';

    return errors;
}

export function handleSubmit({ username, password, navigate }) {
    submitLoginData({ username, password })
        .then((data) => {
            console.log('✅ Registered:', data);
            AccessLoginContextProvider.setUserLoggedIn({ username });
            console.log('.....');
            navigate('/');
        })
        .catch((err) => {
            alert(err.message);
            //console.error('❌ Error submitting data:', err.message);
        });
}

export function createOnSubmit({ username, password }, setErrors, navigate) {
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
                navigate,
            });
        } else {
            setErrors(errors);
        }
    };
}
