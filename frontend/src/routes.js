import StartPage from './pages/StartPage/StartPage'
import SignPage from './pages/SignPage/SignPage';
import MainPage from './pages/MainPage/MainPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import { useRoutes } from 'react-router-dom';
import React from 'react'

const Routes = () => useRoutes([
    { path: '/', element: <StartPage /> },
    { path: '/sign-up', element: <SignPage /> },
    { path: '/sign-in', element: <SignPage /> },
    { path: '/main', element: <MainPage /> },
    { path: '/profile', element: <ProfilePage /> }
])

export default Routes;