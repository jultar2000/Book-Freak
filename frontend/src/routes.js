import SignPage from './pages/SignPage/SignPage';
import MainPage from './pages/MainPage/MainPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import BookPage from './pages/BookPage/BookPage';
import CartPage from './pages/CartPage/CartPage';
import PreviousOrdersPage from './pages/PreviousOrdersPage/PreviousOrders';
import { useRoutes, Navigate  } from 'react-router-dom';
import React from 'react'

const Routes = () => useRoutes([
    { path: '/',  element: <Navigate to="/sign-in" /> },
    { path: '/sign-up', element: <SignPage /> },
    { path: '/sign-in', element: <SignPage /> },
    { path: '/main', element: <MainPage /> },
    { path: '/profile', element: <ProfilePage /> },
    { path: '/cart', element: <CartPage /> },
    { path: '/book/:id', element: <BookPage /> },
    { path: '/previous-orders', element: <PreviousOrdersPage /> }
])

export default Routes;