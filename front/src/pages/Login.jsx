import { Link, useNavigate } from "react-router-dom";
import '../styles/LoginForm.css'
import { login } from "../api/auth";
import { useState } from "react";
import AuthForm from "../components/AuthForm";
import { useAuth } from "../components/AuthProvider";

function Login() {
    const navigate = useNavigate();
    const [error, setError] = useState("");
    const { setIsLoggedIn } = useAuth();

    const handleSubmit = async (values) => {
        const res = await login(values.username, values.password);

        if (!res.ok) {
            setError("Invalid credentials");
            return;
        }
        setIsLoggedIn(true);
        navigate("/");

    }

    const handleValidate = (values) => {
        const errors = {}
        if (!values.username) errors.username = "Username is required";
        if (!values.password) errors.password = "Password is required"
        return errors
    }

    return (
    <>
        <h1>Login</h1>
        <AuthForm onSubmit={handleSubmit} onValidate={handleValidate} action={"Login"}/>
        <p>{error}</p>
        <p>No account? 
            <Link to="/register">
             Register</Link>
        </p>
    </>
    )
}
export default Login;