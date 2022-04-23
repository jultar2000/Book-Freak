import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';
import Book from '../../images/book.jpg'
import Button from "../../components/Button/Button";
import { register, login } from '../../actions/userActions'
import './SignPage.css'

function SignPage() {

    const [state, setState] = useState(true);
    const nav = useNavigate();

    const registerHandler = () => {
        const register_data = {
            "username": document.getElementById("username").value,
            "password": document.getElementById("password").value,
            "email": document.getElementById("email").value
        };
        register(register_data)
    }

    const loginHandler = () => {
        const login_data = {
            "username": document.getElementById("username").value,
            "password": document.getElementById("password").value,
        };
        login(login_data)
        nav("/main-page")
    }

    function navigate() {
        state ? nav("/sign-in") : nav("/sign-up")
        setState(!state)
    }

    return (
        <div className="main-container">
            <div className="picture-container">
                <img className="sign-picture" src={Book} ></img>
            </div>
            <div className="form-container">
                <form className="sign-form">
                    <label htmlFor="username" >Username: </label>
                    <input className="sign-input" type="text" id="username" placeholder="username"></input>
                    {state ?
                        <>
                            <label htmlFor="email" >Email: </label>
                            <input className="sign-input" type="email" id="email" placeholder="email@mail.com"></input>
                        </> : null
                    }
                    <label htmlFor="password" >Password: </label>
                    <input className="sign-input" type="password" id="password" placeholder="···········" autoComplete="off"></input>
                </form>
                <Button type="medium-btn"
                    style={{ marginTop: 10 }}
                    text={state ? "Sign up" : "Sign in"}
                    onClick={state ? registerHandler : loginHandler} />
                <br />
                <Button type="href-btn"
                    style={{ marginTop: 30 }}
                    text={state ? "Already have an account?" : "Don't have an account?"}
                    onClick={navigate} />
            </div>
        </div>
    );
}

export default SignPage;