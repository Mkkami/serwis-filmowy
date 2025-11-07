import {useState} from 'react'
import {useNavigate, Link} from 'react-router-dom'
import "../styles/Header.css"

function Header({ isLoggedIn, onLogout }) {
    const navigate = useNavigate();


  const handleLogout = async () => {
    await onLogout();
    navigate("/");
  }

    return (
        <header className='header'>
            <Link to='/'>
                FilmBase
            </Link>

            <nav>
                <input placeholder='Search'></input>
                {isLoggedIn && <Link to="/add">Add title</Link>}
            </nav>

            <div className='profile'>
                {!isLoggedIn ?
                <Link to="/login">Login</Link>
                :
                <>
                    <Link to="/profile">Profile</Link>
                    <button onClick={handleLogout} className='logout'>Logout</button>
                </>
            }
            </div>
        </header>
    )
}
export default Header;