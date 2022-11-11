import React, { useEffect, useState } from "react";
import { getUserData, getUserImage, updateUserData, updateUserFunds, updateUserImage } from "../../services/userService";
import { ExtendedUserData } from "../../shared/interfaces/User/ExtendedUserData";
import './ProfilePage.css'

const ProfilePage = () => {

    const [userData, setUserData] = useState<ExtendedUserData>()
    const [userImage, setUserImage] = useState<Blob>()

    useEffect(() => {
        getUserData()
            .then((res) => {
                setUserData(res.data)
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
        const nameInput = document.getElementById("name-input") as HTMLInputElement
        const surnameInput = document.getElementById("surname-input") as HTMLInputElement
        const birtDateInput = document.getElementById("birth-date-input") as HTMLInputElement
        const fundsInput = document.getElementById("funds-input") as HTMLInputElement

        if (nameInput && nameInput.value) {
            extendedUserData.name = nameInput.value
        }
        if (surnameInput && surnameInput.value) {
            extendedUserData.surname = surnameInput.value
        }
        if (birtDateInput && birtDateInput.value) {
            extendedUserData.birthDate = birtDateInput.value
        }

        let promises: any[] = []
        if(extendedUserData.name || extendedUserData.surname || extendedUserData.birthDate) {
            promises.push(updateUserData(extendedUserData))
        }
        if(fundsInput.value) {
            promises.push(updateUserFunds(+fundsInput.value))
        }

        Promise.all(promises)
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
                    <input className="update-content-input" id='name-input' placeholder={userData != null ? userData.name : 'name'} />
                </div>
                <div className='update-content-container'>
                    <span className="update-content-span">Surname</span>
                    <input className="update-content-input" id='surname-input' placeholder={userData != null ? userData.surname : 'surname'} />
                </div>
                <div className='update-content-container'>
                    <span className="update-content-span">Birth Date</span>
                    <input className="update-content-input" id='birth-date-input' placeholder={userData != null ? userData.birthDate : 'xx-yy-zzzz'} />
                </div>
                <div className='update-content-container'>
                    <span className="update-content-span">Funds</span>
                    <input type="number" className="update-content-input" id='funds-input' placeholder={userData != null ? userData.funds : ''} />
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