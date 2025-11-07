import { useState } from 'react';
import Header from './components/Header';
import './styles/App.css'
import { Outlet } from 'react-router-dom';
import { logout } from './api/auth';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogout = async () => {
    await logout();
    setIsLoggedIn(false);
  };


  return (
    <div className='app'>
      <Header isLoggedIn={isLoggedIn} onLogout={handleLogout}/>
      <main>
        <Outlet/>
      </main>
    </div>
  )
}

export default App
