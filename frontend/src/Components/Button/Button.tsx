import React from "react";
import { ButtonProps } from "../../shared/interfaces/Component/ButtonProps";
import './Button.css'

const Button = (props: ButtonProps) => {
    return (
        <button className={props.type} style={props.style} onClick={props.onClick} disabled={props.disabled} >
            {props.text}
        </button>
    );
}

export default Button;