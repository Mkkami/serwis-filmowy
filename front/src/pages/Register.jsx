import "../styles/LoginForm.css"

function Register() {

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
            <button type="submit">Register</button>
        </form>
        <p>Already have an account?
            <Link to="/login">
             Login</Link>
        </p>
    </>
    )
}
export default Register;