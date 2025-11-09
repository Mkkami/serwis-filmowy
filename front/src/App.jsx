import Header from './components/Header';
import './styles/App.css'
import { Outlet } from 'react-router-dom';
import './styles/Form.css'

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
