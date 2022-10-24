import React, { ChangeEvent, useState } from "react";
import { useNavigate } from 'react-router-dom';
import Book from '../../public/book.jpg'
import Button from "../../Components/Button/Button";
import { register, login } from '../../services/authService'
import { UserData } from "../../interfaces/UserData";
import './SignPage.css'

function SignPage() {

    const [userData, setUserData] = useState<UserData>({})
    const [pageState, setPageState] = useState(true)
    const nav = useNavigate()

    const setData = (name: string, event: ChangeEvent<HTMLInputElement>) => {
        setUserData({
            [name]: event.target.value
        })
    }

    const registerHandler = () => {
        register(userData)
    }

    const loginHandler = () => {
        login(userData)
        nav("/main-page")
    }

    function navigate() {
        pageState ? nav("/sign-in") : nav("/sign-up")
        setPageState(!pageState)
    }

    return (
        <div className="main-container">
            <div className="picture-container">
                <img className="sign-picture" src={Book} ></img>
            </div>
            <div className="form-container">
                <form className="sign-form">
                    <label htmlFor="username" >Username: </label>
                    <input className="sign-input" type="text" id="username" placeholder="username" onChange={(event) => { setData("username", event) }} />
                    {pageState ?
                        <>
                            <label htmlFor="email" >Email: </label>
                            <input className="sign-input" type="email" id="email" placeholder="email@mail.com"
                                onChange={(event) => { setData("email", event) }} />
                        </> : null
                    }
                    <label htmlFor="password" >Password: </label>
                    <input className="sign-input" type="password" id="password" placeholder="···········" autoComplete="off"
                        onChange={(event) => { setData("password", event) }} />
                </form>
                <Button type="medium-btn"
                    style={{ marginTop: 10 }}
                    text={pageState ? "Sign up" : "Sign in"}
                    onClick={pageState ? registerHandler : loginHandler} />
                <br />
                <Button type="href-btn"
                    style={{ marginTop: 30 }}
                    text={pageState ? "Already have an account?" : "Don't have an account?"}
                    onClick={navigate} />
            </div>
        </div>
    );
}

export default SignPage;