import React, { useEffect, useState } from "react";
import { getUserData, getUserImage, updateUserData, updateUserImage } from "../../services/userService";
import { ExtendedUserData } from "../../shared/interfaces/User/ExtendedUserData";
import './ProfilePage.css'

const ProfilePage = () => {

    const [userData, setUserData] = useState<ExtendedUserData>()
    const [userImage, setUserImage] = useState<Blob>()

    useEffect(() => {
        getUserData()
            .then((res) => {
                setUserData(res.data)
                console.log(res.data)
            }).catch((err) => {
                console.log(err)
            })
        getUserImage()
            .then((res) => {
                setUserImage(res.data)
            }).catch((err) => {
                console.log(err)
            })

    }, [])

    const updateHandler = () => {
        let extendedUserData: ExtendedUserData = {}
        const nameInput = document.getElementById("") as HTMLInputElement
        const surnameInput = document.getElementById("") as HTMLInputElement
        const birtDateInput = document.getElementById("") as HTMLInputElement
        const fundsInput = document.getElementById("") as HTMLInputElement

        if (nameInput && surnameInput && birtDateInput && fundsInput) {
            if (nameInput.value) {
                extendedUserData.name = nameInput.value
            }
            if (surnameInput.value) {
                extendedUserData.surname = surnameInput.value
            }
            if (birtDateInput.value) {
                extendedUserData.birthDate = birtDateInput.value
            }
            if (fundsInput.value) {
                extendedUserData.funds = +fundsInput.value
            }
        }
        updateUserData(extendedUserData)
            .then(() => {
                window.location.reload()
            }).catch((err) => {
                console.log(err)
            })
    }

    const handleChange = (files: any) => {
        let fileReader = new FileReader();
        const file = files[0]
        const imageInput = document.getElementById("form-image") as HTMLImageElement
        if (imageInput) {
            fileReader.onload = () => {
                imageInput.src = fileReader.result;
            }
        }
        fileReader.readAsDataURL(file)
        setUserImage(file)
    }

    const updateImage = () => {
        const formData = new FormData()
        if (userImage) {
            formData.append("image", userImage)
            updateUserImage(formData)
                .then(() => {
                    window.location.reload()
                }).catch((err) => {
                    console.log(err)
                })
        }
    }

    return (
        <div className="main-container">
            <div className="image-profile-container">
                <img id="form-image" width={450} height={500} src={"data:image/png;base64," + userImage}></img>
                <input
                    type="file"
                    id="change-image-btn"
                    onChange={e => handleChange(e.target.files)} />
                <button
                    type="button"
                    id="update-image-btn"
                    className='update-content-container'
                    onClick={updateImage}>Update image</button>
            </div>
            <form className="basic-data-form">
                <div className='update-content-container'>
                    <span className="update-content-span">Name</span>
                    <input className="update-content-input" id='name-input' placeholder={userData != null ? userData.name: ''} />
                </div>
                <div className='update-content-container'>
                    <span className="update-content-span">Surname</span>
                    <input className="update-content-input" id='surname-input' placeholder={userData != null ? userData.surname: ''}/>
                </div>
                <div className='update-content-container'>
                    <span className="update-content-span">Birth Date</span>
                    <input className="update-content-input" id='birt-date-input' placeholder={userData != null ? userData.birthDate: ''}/>
                </div>
                <div className='update-content-container'>
                    <span className="update-content-span">Funds</span>
                    <input type="number" className="update-content-input" id='funds-input'/>
                </div>
                <button
                    type="button"
                    id="update-content-btn"
                    className='update-content-container'
                    onClick={updateHandler}>Update Data</button>
            </form>

            <div className="right-container">

            </div>
        </div>
    );
}

export default ProfilePage