import React, { ChangeEvent, useState } from "react";
import { useNavigate, useLocation } from 'react-router-dom';
import Button from "../../Components/Button/Button";
import { register, login } from '../../services/authService'
import { UserData } from "../../interfaces/User/UserData";
import './SignPage.css'

function SignPage() {

    const USERNAME_REGEX = /^\[A-z\][A-z0-9-_]{3,23}$/;
    const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$/;
    const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    const location = useLocation();
    const nav = useNavigate()
    const [userData, setUserData] = useState<UserData>({})
    const [isLoginPage, setIsLoginPage] = useState(location.pathname === '/sign-in')
    const [isUsernameValid, setIsUsernameValid] = useState(false)
    const [isPasswordValid, setIsPasswordValid] = useState(false)
    const [isEmailValid, setIsEmailValid] = useState(false)

    const setData = (name: string, event: ChangeEvent<HTMLInputElement>) => {
        const newUserData = {
            ...userData,
            [name]: event.target.value
        }
        setUserData(newUserData)
        if (userData.username) {
            setIsUsernameValid(USERNAME_REGEX.test(userData.username))
        }
        if (userData.email) {
            setIsEmailValid(EMAIL_REGEX.test(userData.email))
        }
        if (userData.password) {
            setIsPasswordValid(PASSWORD_REGEX.test(userData.password))
        }
    }

    const registerHandler = () => {
        register(userData)
    }

    /*
        >>TODO handle navigate only when request is good
    */
    const loginHandler = () => {
        login(userData)
        nav('/main')
    }

    function navigate() {
        isLoginPage ? nav("/sign-in") : nav("/sign-up")
        setIsLoginPage(!isLoginPage)
    }

    return (
        <div className="main-container">
            <div className="picture-container">
                <img className="sign-picture" src='/images/book.jpg' ></img>
            </div>
            <div className="form-container">
                <form className="sign-form">
                    <div className="form-element">
                        <label htmlFor="username" >Username: </label>
                        <input className="sign-input" type="text" id="username" placeholder="username" onChange={(event) => { setData("username", event) }} />
                        {!isLoginPage && !isUsernameValid ? <span className="validation-message">Username not valid</span> : null}
                    </div>
                    {!isLoginPage ?
                        <div className="form-element">
                            <label htmlFor="email" >Email: </label>
                            <input className="sign-input" type="email" id="email" placeholder="email@mail.com"
                                onChange={(event) => { setData("email", event) }} />
                            {!isLoginPage && !isEmailValid ? <span className="validation-message">Email not valid</span> : null}
                        </div> : null
                    }
                    <div className="form-element">
                        <label htmlFor="password" >Password: </label>
                        <input className="sign-input" type="password" id="password" placeholder="···········" autoComplete="off"
                            onChange={(event) => { setData("password", event) }} />
                        {!isLoginPage && !isPasswordValid ? <span className="validation-message">Password not valid</span> : null}
                    </div>
                </form>
                <Button type="medium-btn"
                    style={{ marginTop: 10 }}
                    text={isLoginPage ? "Sign in" : "Sign up"}
                    disabled={isLoginPage || (isUsernameValid && isEmailValid && isPasswordValid) ? null: "disabled"}
                    onClick={isLoginPage ? loginHandler : registerHandler} />
                <br />
                <Button type="href-btn"
                    style={{ marginTop: 30 }}
                    text={isLoginPage ? "Already have an account?" : "Don't have an account?"}
                    onClick={navigate} />
            </div>
        </div>
    );
}

export default SignPage;