import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from 'react-router-dom';
import Button from '../Button/Button'
import './Navbar.css'
import { getItemFromLocalStorage } from "../../utils/helpers";
import { getUserData, getUserImage } from "../../services/userService";

const Navbar = () => {

    const nav = useNavigate();
    const location = useLocation();
    const [userImage, setUserImage] = useState()

    const checkPathState = () => {
        let current_path = location.pathname;
        if (current_path === "/sign-up" || current_path === "/sign-in" || current_path === "/")
            return true
        return false
    }

    useEffect(() => {
        getUserImage()
            .then((res) => {
                setUserImage(res.data)
            }).catch((err) => {
                console.log(err)
            })
    }, [])

    return (
        <nav className='navbar'>
            <ul className='nav-list'>
                {
                    checkPathState() ?
                        <li className='logo'>
                            <img src='/images/logo.png' width={70} height={70} onClick={() => nav('/main')}></img>
                            <figcaption className='caption'> Book Freak </figcaption>
                        </li>
                        :
                        <li className='user-icon'>
                            <img id="user-image" src={userImage ? "data:image/png;base64," + userImage : "/images/user-icon.svg"} width={80} height={80} onClick={() => nav('/profile')}></img>
                            <figcaption className='caption'> Your profile </figcaption>
                        </li>
                }
                {
                    checkPathState() ?
                        <li className='git-icon'>
                            <a href='https://github.com/jultar2000/BookFreak'>
                                <img src='/images/GitHub-Mark-Light-64px.png' style={{ marginRight: "10px" }} ></img>
                                Github
                            </a>
                        </li>
                        :
                        <li className='logo'>
                            <img src='/images/logo.png' width={70} height={70} onClick={() => nav('/main')}></img>
                            <figcaption className='caption'> Book Freak </figcaption>
                        </li>
                }
                {
                    checkPathState() ?
                        <li> <Button type="large-btn" text="SIGN UP" onClick={() => nav('/sign-up')} /> </li>
                        :
                        <li className='cart'>
                            <img src='/images/shopping-cart.svg' width={70} height={70} onClick={() => nav('/cart')}></img>
                            <figcaption className='caption'> Your cart </figcaption>
                        </li>
                }
            </ul>
        </nav>
    )
}

export default Navbar