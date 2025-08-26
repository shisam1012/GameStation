import { createContext, useContext, useState } from 'react';

const LoginContext = createContext();

const AccessLoginContextProvider = {};
//Used in every page in order to check if the user is logged in or not
//The context is visible for every page without passing the username as a prop to every function
export default function LoginContextProvider({ children }) {
    const [userLoggedIn, setUserLoggedIn] = useState(null);
    AccessLoginContextProvider.setUserLoggedIn = setUserLoggedIn;
    return (
        <LoginContext.Provider
            value={{
                userLoggedIn,
                setUserLoggedIn,
            }}
        >
            {children}
        </LoginContext.Provider>
    );
}

export { LoginContext, LoginContextProvider, AccessLoginContextProvider };

export const UserLogin = () => {
    return useContext(LoginContext);
};
