import { submitSignupData } from './SignupDB';

export function validateForm({ username, password, email }) {
    const errors = {};

    if (!username) errors.username = 'Username is required';
    if (!email) errors.email = 'Email is required';
    //else if (!validateEmail(email)) errors.email = 'Invalid email address';

    if (!password) errors.password = 'Password is required';
    // else if (password.length < 6) errors.password = 'Password too short';

    return errors;
}

export function handleSubmit({ username, password, email }) {
    submitSignupData({ username, password, email })
        .then((data) => {
            console.log('✅ Registered:', data);
        })
        .catch((err) => {
            console.error('❌ Error submitting data:', err.message);
        });
}

export function createOnSubmit({ username, password, email }, setErrors) {
    return function (e) {
        e.preventDefault();
        const errors = validateForm({ username, password, email });
        if (Object.keys(errors).length === 0) {
            handleSubmit({ username, password, email });
        } else {
            setErrors(errors);
        }
    };
}

/*function validateEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}*/
