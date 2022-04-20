import './App.css';
import React from 'react';
import Navbar from './Components/Navbar/Navbar';
import StartPage from './Components/StartPage/StartPage'
import SignPage from './Components/SignPage/SignPage';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <>
      <div className='App'>
        <Router>
          <Navbar />
          <Routes>
            <Route exact path='/' element={<StartPage />} />
          </Routes>
          <Routes>
            <Route exact path='/sign-up' element={<SignPage />} />
          </Routes>
        </Router>
      </div>
    </>
  );
}

export default App;
