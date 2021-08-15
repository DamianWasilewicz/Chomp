import React, {useState} from 'react';
import { useHistory } from 'react-router-dom';
import axios from "axios";
import './Login.css';

/**
 * Component for the about page. Contains blurbs about the program and the
 * developers, as well as a button to log in and/or sign up.
 */
function Login(props) {
    // state variables
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [msg, setMsg] = useState("");
    const history = useHistory();

    // function for login button's onClick; sends info to backend to log user in
    const handleSubmit = (event) => {
        event.preventDefault();
        console.log("Prevented");
        console.log("Name: " + name);
        console.log("Password: " + password);

        const toSend = {
            username: name,
            password: password,
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/login",
            toSend,
            config
        ) .then(response => {
                console.log(response)
                let id = response.data['userId'];
                setMsg(response.data["message"]);
                if (id >= 1) {
                    props.setLoggedIn(true);
                    history.push('/');
                }
            }
        )
    }

    // function for signUp button's onClick. Renders SignUp component
    const handleClick = () => {
        history.push('/signup');
    }

    return(
        <div className="login">
            <div className={"loginInstruct"}>
                <h4> <em>log in to access these pages!</em> </h4>
            </div>
            <div className="loginBlock">
                <div className="chompBlurb">
                    <h1> <strong>CHOMP!</strong> </h1>
                    <h3> <em>plan better, eat healthier</em> </h3>
                    <h4 id="serif">Chomp! is dedicated to helping people get on track and stay on track when it comes to
                        eating healthy. We display useful, personalized statistics for our users while also advertising
                        fresh alternatives for daily meal consumption. By providing users with meal options set to their
                        specifications, Chomp! can meet the needs of just about anyone. Sign up today to get started on
                        your journey with Chomp!</h4>
                </div>

                <div>
                    <h1> <strong>Log In</strong> </h1>
                    <form onSubmit={handleSubmit}>
                        <label>Username
                            <input required type={'text'} value={name} onChange={(event) => {
                                setName(event.target.value);
                            }}/>
                        </label>
                        <br/>
                        <label>Password
                            <input required type={'password'} value={password} onChange={(event) => {
                                setPassword(event.target.value);
                            }}/>
                        </label>
                        <br/>
                        <input className="submit" type={'submit'} value={'Submit'}/>
                    </form>
                    <div id="errorMsg"> {msg} </div>
                    <button onClick={handleClick}>New User? Sign Up!</button>
                </div>
            </div>
        </div>
    )
}

export default Login;