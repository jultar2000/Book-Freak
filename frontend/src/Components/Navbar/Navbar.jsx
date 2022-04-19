import { useNavigate } from 'react-router-dom';
import GithubIcon from '../Images/GitHub-Mark-Light-64px.png'
import logo from '../Images/logo2.png'
import Button from '../Button/Button'
import './Navbar.css'

function Navbar() {

    const style = {
        padding: "20px 40px",
        border: "none",
        cursor: "pointer",
        borderRadius: "500px",
        fontWeight: "800",
        font: "20"
    }

    const nav = useNavigate();
    function navigate() {
        nav("/sign-up");
    }

    return (
        <>
            <nav className='navbar'>
                <ul className='nav-list'>
                    <li className='logo'>
                        <img src={logo} width={70} height={70}></img>
                        <figcaption> Book Freak </figcaption>
                    </li>
                    <li className='git-icon'>
                        <a href='https://github.com/jultar2000/BookFreak'>
                            <img src={GithubIcon} style={{ marginRight: "10px" }} ></img>
                            Github
                        </a>
                    </li>
                    <li>
                        <Button style={style} text="SIGN UP" onClick={navigate} />
                    </li>
                </ul>
            </nav>
        </>
    )
}

export default Navbar