import { useNavigate, Link } from "react-router-dom";
import AuthForm from "../components/AuthForm";
import { useState } from "react";
import { register } from "../api/auth";

function Register() {
    const navigate = useNavigate();
    const [error, setError] = useState("");

    const handleSubmit = async (values) => {
        const res = await register(values.username, values.password);

        if (res.status === 400) {
            setError(res.text())
            return;
        }
        if (res.status === 409) {
            setError(res.text())
            return;
        }
        if (!res.ok) {
            setError("Something went wrong.");
            return;
        }
        alert("Registered successfully.");
        navigate("/login");
    }

    const handleValidate = (values) => {
        const errors = {}
        if (!values.username) errors.username = "Username is required";
        if (!values.password) errors.password = "Password is required";
        if (values.password.length < 8) {
            errors.password = "Password have at least 8 characters";
        }
        return errors;
    }

    return (
    <div className="form">
        <h1>Register</h1>
        <AuthForm onSubmit={handleSubmit} onValidate={handleValidate} action="Register" />
        <p className="error">{error}</p>
        <p>Already have an account?
        <br></br>
        <Link to="/login">Login</Link>
        </p>
    </div>
    )
}
export default Register;