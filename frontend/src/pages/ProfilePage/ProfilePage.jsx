import React from "react";
import { getUserImage, getCurrentUser, getUserData } from "../../actions/userActions"

function ProfilePage() {

    return ( 
            <div className="main-container">
                <div className="left-container">
                    <form className="basic-data-form">
                        <input class="user-image" name="user-image" type="file"> </input>
                        
                    </form>

                </div>
                <div className="right-container">

                </div>
            </div>    
    );
}

export default ProfilePage