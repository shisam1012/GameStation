import { createContext, useContext, useState } from 'react';

const LoginContext = createContext();

const AccessLoginContextProvider = {};
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
