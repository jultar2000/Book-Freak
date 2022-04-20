import React, { useState } from "react";
import Book from '../Images/book.jpg'
import Button from "../Button/Button";
import axiosInstance from "../utils/axiosInstance";
import './SignPage.css'

function SignPage() {

    const sign_btn_style = {
        marginTop: 20,
        paddingBottom: 10,
        paddingTop: 10,
        paddingRight: 30,
        paddingLeft: 30
    }

    const state_btn_style = {
        background: "none",
        border: "none",
        marginTop: 30,
        padding: 0,
        color: "white",
        textDecoration: "underline",
        cursor: "pointer",
    }

    const [state, setState] = useState(true);
    let element = state ?
        <>
            <label htmlFor="email" >Email: </label>
            <input className="sign-input" type="email" id="email" placeholder="email@mail.com"></input>
        </> : null

    async function register() {
        const data = {
            "username": document.getElementById("username").value,
            "password": document.getElementById("password").value,
            "email": document.getElementById("email").value
        };
        try {
            await axiosInstance.post("/auth/signup", data, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
        } catch (err) {
            console.error(err);
        }
    }

    async function login() {
        const data = {
            "username": document.getElementById("username").value,
            "password": document.getElementById("password").value,
        };
        try {
            await axiosInstance.post("/auth/login", data, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
        } catch (err) {
            console.error(err);
        }
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

                    {element}

                    <label htmlFor="password" >Password: </label>
                    <input className="sign-input" type="password" id="password" placeholder="···········"></input>
                </form>
                <Button style={sign_btn_style} text={state ? "Sign up" : "Sign in"} onClick={state ? register : login} />
                <br />
                <Button style={state_btn_style}
                    text={state ? "Already have an account?" : "Don't have an account?"}
                    onClick={() => { setState(!state) }} />
            </div>
        </div>
    );
}

export default SignPage;