import React from "react";
import { ResponseStatus } from "../../shared/enums/ResponseStatus"
import { MessagePopupProps } from "../../shared/interfaces/Component/MessagePopupProps";
import './MessagePopup.css'

const MessagePopup = (props: MessagePopupProps) => {

    return (props.trigger) ? (
        <div className={"popup-container " + (props.status === ResponseStatus.SUCCESS ? "tick-popup" : "cross-popup")}>
            <div className="popup-container-content">
                <button className="close-popup-btn" onClick={() => props.setTrigger(false)}>x</button>
                <div className="popup-image-container">
                    <figure>
                        <img width={75} height={75} src={"/images/" + (props.status === ResponseStatus.SUCCESS ? "tick-sign.svg" : "cross-icon.svg")}></img>
                        <figcaption>{props.status === ResponseStatus.SUCCESS ? ResponseStatus["SUCCESS"] : ResponseStatus["ERROR"]}</figcaption>
                    </figure>
                </div>
                <div className="popupt-description-container">
                    {props.description}
                </div>
            </div>
        </div>
    ) : null
}

export default MessagePopup