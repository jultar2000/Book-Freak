import { useNavigate } from 'react-router-dom';
import GithubIcon from '../../images/GitHub-Mark-Light-64px.png'
import logo from '../../images/logo.png'
import Button from '../Button/Button'
import './Navbar.css'

function Navbar() {

    const nav = useNavigate();
    function navigate() {
        nav("/sign-up");
    }

    return (
        <nav className='navbar'>
            <ul className='nav-list'>
                <li className='logo'>
                    <img src={logo} width={70} height={70}></img>
                    <figcaption className='caption'> Book Freak </figcaption>
                </li>
                <li className='git-icon'>
                    <a href='https://github.com/jultar2000/BookFreak'>
                        <img src={GithubIcon} style={{ marginRight: "10px" }} ></img>
                        Github
                    </a>
                </li>
                <li>
                    <Button type="large-btn" text="SIGN UP" onClick={navigate} />
                </li>
            </ul>
        </nav>
    )
}

export default Navbar