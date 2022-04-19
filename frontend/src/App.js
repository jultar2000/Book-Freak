import './App.css';
import React from 'react';
import Navbar from './Components/Navbar/Navbar';
import MainContent from './Components/MainContent/MainContent'
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
        </Router>
      </div>
    </>
  );
}

export default App;
