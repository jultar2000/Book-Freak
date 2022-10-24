import React from "react";
import './Button.css'

function Button(props) {
    return (
        <button className={props.type} style={props.style} onClick={props.onClick} >
            {props.text}
        </button>
    );
}

export default Button;