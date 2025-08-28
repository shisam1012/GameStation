import { submitLoginData } from './LoginApi.js';
import { AccessLoginContextProvider } from '../../context/LoginContext.js';

//Checks if one of the fields is empty and return an array of errors
export function validateForm({ username, password }) {
    const errors = {}; //start with 0 errors
    if (!username) errors.username = 'Username is required';
    if (!password) errors.password = 'Password is required';

    return errors;
}

//Called by createOnSubmit if there are no errors (missing data)
export function handleSubmit({ username, password, navigate }) {
    submitLoginData({ username, password }) //function from LoginApi - pass the data to the server
        .then((data) => {
            console.log('✅ Registered:', data);
            AccessLoginContextProvider.setUserLoggedIn({ username }); //update the login context
            //console.log('.....');
            navigate('/');
        })
        .catch((err) => {
            alert(err.message);
            //console.error('❌ Error submitting data:', err.message);
        });
}

//Called when the client press on the submit button of the login form
export function createOnSubmit({ username, password }, setErrors, navigate) {
    return function (e) {
        e.preventDefault();
        const errors = validateForm({
            username,
            password,
        });
        if (Object.keys(errors).length === 0) {
            //if there are no errors - call the handleSubmit function
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
