import { submitSignupData } from './SignupApi.js';
import { AccessLoginContextProvider } from '../../context/LoginContext.js';

export function validateForm({ username, password, email, repeatedPassword }) {
    const errors = {};

    if (!username) errors.username = 'Username is required';
    if (!email) errors.email = 'Email is required';
    //else if (!validateEmail(email)) errors.email = 'Invalid email address';

    if (!password) errors.password = 'Password is required';
    if (!(password === repeatedPassword))
        errors.repeatedpassword = 'The passwords are not the same';
    // else if (password.length < 6) errors.password = 'Password too short';

    return errors;
}

//Called by createOnSubmit if there are no errors (missing data, mismatching passwords)
export function handleSubmit({ username, password, email, navigate }) {
    submitSignupData({ username, password, email })
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

//Called when the client press on the submit button of the signup form
export function createOnSubmit(
    { username, password, email, repeatedPassword, navigate },
    setErrors
) {
    return function (e) {
        e.preventDefault();
        setErrors({});
        const errors = validateForm({
            username,
            password,
            email,
            repeatedPassword,
        });
        if (Object.keys(errors).length === 0) {
            handleSubmit({ username, password, email, navigate });
        } else {
            setErrors(errors);
        }
    };
}
