import { createContext, useContext, useState } from 'react';

const LoginContext = createContext();

export default function LoginContextProvider({ children }) {
    const [userLoggedIn, setUserLoggedIn] = useState(null);
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

export const UserLogin = () => {
    return useContext(LoginContext);
};
