import {useNavigate, Link} from 'react-router-dom'
import "../styles/components/Header.css"
import { useAuth } from './AuthProvider';
import { logout } from '../api/auth';
import { useRef } from 'react';
import SelectAdd from './AddItemDialog';
import ProfileDropdown from './ProfileDropdown';
import Search from './Search';
import AddItemDialog from './AddItemDialog';

function Header() {
    const navigate = useNavigate();
    const {isLoggedIn, setIsLoggedIn} = useAuth();

    const dialogRef = useRef(null);

    const handleLogout = async () => {
        await logout();
        setIsLoggedIn(false);
        navigate("/");
    }

    const handleOpenModal = () => {
        dialogRef.current?.showModal();
    }


    return (
        <header className='header'>
            <Link to='/' className='logo'>
                FilmBase
            </Link>

            <nav>
                <Search/>
                {isLoggedIn && <button onClick={handleOpenModal}>Add</button>}
            </nav>

            <div className='profile'>
                {!isLoggedIn ?
                <Link to="/login">Login</Link>
                :
                <ProfileDropdown handleLogout={handleLogout}/>
            }
            </div>
            {isLoggedIn && <AddItemDialog ref={dialogRef} />}
        </header>
    )
}
export default Header;