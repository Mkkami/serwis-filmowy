import { createContext, useState, useContext, useEffect } from "react";
import { checkLoginStatus } from "../api/auth";

const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const checkStatus = async () => {
            const res = await checkLoginStatus();
            if (res.ok) {
                setIsLoggedIn(true);
            }
        }
        checkStatus();
    }, [])

    return (
        <AuthContext.Provider value={{isLoggedIn, setIsLoggedIn}}>
            {children}
        </AuthContext.Provider>
    )
}
export const useAuth = () => useContext(AuthContext);