import { useEffect, useState, useRef } from "react";
import { Link } from "react-router-dom";
import '../styles/components/Dropdown.css'

function ProfileDropdown({handleLogout}) {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef(null);

    const toggleMenu = () => {
        setIsOpen(prev => !prev);
    }

    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () =>{
            document.removeEventListener("mousedown", handleClickOutside);
        }
    }, []);

    return (
        <div className="dropdown-container" ref={dropdownRef}>
            <button onClick={toggleMenu} className="dropdown-toggle">
                Profile
            </button>

            {isOpen && (
                <ul className="dropdown-menu">
                    <li><Link to="/profile">About</Link></li>
                    <li><button onClick={handleLogout}>Logout</button></li>
                </ul>
            )}

        </div>
    )
}
export default ProfileDropdown;