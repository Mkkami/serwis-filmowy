import Header from './components/Header';
import './styles/App.css'
import { Outlet } from 'react-router-dom';

function App() {

  return (
    <div className='app'>
      <Header />
      <main>
        <Outlet/>
      </main>
    </div>
  )
}

export default App
