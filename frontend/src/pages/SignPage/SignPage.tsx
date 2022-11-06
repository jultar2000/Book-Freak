import React, { ChangeEvent, useState } from "react";
import { useNavigate, useLocation } from 'react-router-dom';
import Button from "../../Components/Button/Button";
import { register, login } from '../../services/authService'
import { UserData } from '../../shared/interfaces/User/UserData'
import './SignPage.css'
import { setItemToLocalStorage } from "../../utils/helpers";
import MessagePopup from "../../Components/MessagePopup/MessagePopup";
import { ResponseStatus } from "../../shared/enums/ResponseStatus";

const SignPage = () => {

    const USERNAME_REGEX = /^[a-zA-Z0-9]+$/;
    const PASSWORD_REGEX = /[a-zA-Z0-9]{8,}/;
    const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    const errorLoginMessage = "Wrong login or password!"
    const errorRegisterMessage = "Cannot register!"
    const successRegisterMessage = "Please check your email to confirm your account."

    const nav = useNavigate()
    const location = useLocation()

    const [userData, setUserData] = useState<UserData>({})
    const [popupTrigger, setPopupTrigger] = useState(false)
    const [responseStatus, setResponseStatus] = useState<ResponseStatus>(ResponseStatus.ERROR)
    const [popupMessage, setPopupMessage] = useState("")
    const [isLoginPage, setIsLoginPage] = useState(location.pathname === "/sign-in")
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
            .then(() => {
                setPopupProps(successRegisterMessage, ResponseStatus.SUCCESS)
            })
            .catch((err) => {
                setPopupProps(errorRegisterMessage, ResponseStatus.ERROR)
                console.log(err)
            })
    }

    const loginHandler = () => {
        login(userData).then((res) => {
            setItemToLocalStorage("authenticationToken", res.data.authenticationToken)
            setItemToLocalStorage("refreshToken", res.data.refreshToken);
            setItemToLocalStorage("expiresAt", res.data.expiresAt);
            setItemToLocalStorage("username", res.data.username);
            nav('/main')
        }).catch((err) => {
            setPopupProps(errorLoginMessage, ResponseStatus.ERROR)
            console.log(err)
        })
    }

    const setPopupProps = (message: string, status: ResponseStatus) => {
        setPopupTrigger(!popupTrigger)
        setPopupMessage(message)
        setResponseStatus(status)
    }

    function navigate() {
        isLoginPage ? nav("/sign-in") : nav("/sign-up")
        setIsLoginPage(!isLoginPage)
    }

    return (
        <div className="sign-container">
            <div id="message-popup-container">
                <MessagePopup trigger={popupTrigger} setTrigger={setPopupTrigger}
                    status={responseStatus} description={popupMessage}></MessagePopup>
            </div>
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
                    disabled={isLoginPage || (isUsernameValid && isEmailValid && isPasswordValid) ? false : true}
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