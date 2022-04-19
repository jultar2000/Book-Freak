import './App.css';
import React from 'react';
import Navbar from './Components/Navbar/Navbar';
import MainContent from './Components/MainContent/MainContent'
import SignUp from './Components/SignUp/SignUp';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <>
      <div className='App'>
        <Router>
          <Navbar />
          <Routes>
            <Route exact path='/' element={<MainContent />} />
          </Routes>
          <Routes>
            <Route exact path='/sign-up' element={<SignUp />} />
          </Routes>
        </Router>
      </div>
    </>
  );
}

export default App;
