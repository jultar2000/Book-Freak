import React from "react";
import { getUserImage, getCurrentUser, getUserData } from "../../actions/userActions"

function ProfilePage() {

    return ( 
            <div className="main-container">
                <div className="left-container">
                    <button onClick={() => console.log(getUserImage())}></button>
                </div>
                <div className="right-container">

                </div>
            </div>    
    );
}
 export default ProfilePage