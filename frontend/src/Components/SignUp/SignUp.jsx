import React from "react";
import Book from '../Images/book.jpg'
import Button from "../Button/Button";
import './SignUp.css'

function SignUp() {

    return (
        <div className="main-container">
            <div className="picture-container">
                <img className="sign-picture" src={Book} ></img>
            </div>
            <div className="form-container">
                <form className="sign-form">
                    <label htmlFor="username" >Username: </label>
                    <input className="sign-input" type="text" id="username" placeholder="username"></input>

                    <label htmlFor="email" >Email: </label>
                    <input className="sign-input" type="email" id="email" placeholder="email@mail.com"></input>

                    <label htmlFor="password" >Password: </label>
                    <input className="sign-input" type="password" id="password" placeholder="···········"></input>
                </form>
                <Button style={{marginTop: 30, paddingRight: 40, paddingLeft: 40}} text="Sign up"></Button>
            </div>
        </div>
    );
}

export default SignUp;