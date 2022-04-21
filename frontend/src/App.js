import './App.css';
import React from 'react';
import Navbar from './components/Navbar/Navbar';
import StartPage from './pages/StartPage/StartPage'
import SignPage from './pages/SignPage/SignPage';
import MainPage from './pages/MainPage/MainPage';
import { BrowserRouter as Router, useRoutes } from 'react-router-dom';

const Routes = () => useRoutes([
  {path: '/', element: <StartPage/>},
  {path: '/sign-up', element: <SignPage/>},
  {path: '/sign-in', element: <SignPage/>},
  {path: '/main-page', element: <MainPage/>}
])

function App() {
  return (
    <>
      <div className='App'>
        <Router>
          <Navbar />
            <Routes />
        </Router>
      </div>
    </>
  );
}

export default App;
