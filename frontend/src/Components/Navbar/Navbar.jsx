import React from "react";
import { useNavigate, useLocation } from 'react-router-dom';
import GithubIcon from '../../images/GitHub-Mark-Light-64px.png'
import logo from '../../images/logo.png'
import user_icon from '../../images/user_icon.png'
import Button from '../Button/Button'
import './Navbar.css'

function Navbar() {

    const nav = useNavigate();
    const location = useLocation();

    function checkPathState() {
        let current_path = location.pathname;
        if (current_path === "/sign-up" || current_path === "/sign-in" || current_path === "/")
            return true
        return false
    }

    return (
        <nav className='navbar'>
            <ul className='nav-list'>
                <li className='logo'>
                    <img src={logo} width={70} height={70}></img>
                    <figcaption className='caption'> Book Freak </figcaption>
                </li>
                <li className='git-icon'>
                    <a href='https://github.com/jultar2000/BookFreak'>
                        <img src={GithubIcon} style={{ marginRight: "10px" }} ></img>
                        Github
                    </a>
                </li>
                {
                    checkPathState() ?
                        <li> <Button type="large-btn" text="SIGN UP" onClick={() => nav('/sign-up')} /> </li> :
                        <li className='user-icon'>
                            <img src={user_icon} width={75} height={75} onClick={() => nav('/profile')}></img>
                            <figcaption className='caption'> Profile </figcaption>
                        </li>
                }
            </ul>
        </nav>
    )
}

export default Navbar