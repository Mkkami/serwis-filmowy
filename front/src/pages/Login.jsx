import { Link } from "react-router-dom";
import '../styles/LoginForm.css'

function Login() {
    const handleSubmit = (e) => {
        e.preventDefault();
        handleLogin();
    }

    const handleLogin = () => {
        alert('yay');
    }

    return (
    <>
        <h1>Login</h1>
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="">Username:</label>
                <input type="text" />
            </div>
            <div>
                <label htmlFor="">Password:</label>
                <input type="password" />
            </div>
            <button type="submit">Login</button>
        </form>
        <p>No account? 
            <Link to="/register">
             Register</Link>
        </p>
    </>
    )
}
export default Login;