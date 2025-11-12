import {useNavigate, Link} from 'react-router-dom'
import "../styles/Header.css"
import { useAuth } from './AuthProvider';
import { logout } from '../api/auth';
import { useState } from 'react';
import SelectAdd from './SelectAdd';
import ProfileDropdown from './ProfileDropdown';
import Search from './Search';

function Header() {
    const navigate = useNavigate();
    const {isLoggedIn, setIsLoggedIn} = useAuth();
    const [isDialogOpen, setDialogOpen] = useState(false);

    const handleLogout = async () => {
        await logout();
        setIsLoggedIn(false);
        navigate("/");
    }


    return (
        <header className='header'>
            <Link to='/' className='logo'>
                FilmBase
            </Link>

            <nav>
                <Search/>
                {isLoggedIn && <button onClick={() => setDialogOpen(true)}>Add</button>}
            </nav>

            <div className='profile'>
                {!isLoggedIn ?
                <Link to="/login">Login</Link>
                :
                <ProfileDropdown handleLogout={handleLogout}/>
            }
            </div>
            {isDialogOpen && <SelectAdd handleClose={() => setDialogOpen(false)}/>}
        </header>
    )
}
export default Header;