import React, {useState} from 'react';
import {useHistory} from 'react-router-dom';
import ToggleButton from '@material-ui/lab/ToggleButton';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';
import axios from "axios";
import "./SignUp.css";

/**
 * Component for the about page. Contains blurbs about the program and the
 * developers, as well as a button to log in and/or sign up.
 */
function SignUp() {
    // state variables
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [passwordVerify, setPasswordVerify] = useState('');
    const [errorMessage, setErrorMessage] = useState("");
    const [forbiddenGroups, setForbiddenGroups] = useState([]);
    const history = useHistory();

    // handler for clicking the dietary pref form
    const handleToggle = (event, newForbidden) => {
        setForbiddenGroups(newForbidden);
    }

    // handler for submitting a signup
    const handleSubmit = (event) => {
        event.preventDefault();
        const toSend = {
            username: name,
            password: password,
            passwordVerify: passwordVerify,
            forbidden: forbiddenGroups,
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/signup",
            toSend,
            config
        ).then(response => {
                console.log(response)
                let id = response.data['userId'];
                if (id >= 1) {
                    history.push('/login');
                } else {
                    setErrorMessage(response.data['message']);
                }
            }
        );

    }

    return (
        <div className="signUpPage">
            <div className="signUp">
                <div><h2 className="signUpHeader">Sign Up!</h2></div>
                <form onSubmit={handleSubmit}>
                    <label>Username:</label>
                    <br/>
                    <input type={'text'} required value={name} onChange={(event) => {
                        setName(event.target.value);
                    }}/>
                    <br/>
                    <label>Password:</label>
                    <br/>
                    <input type={'password'} required value={password} onChange={(event) => {
                        setPassword(event.target.value);
                    }}/>
                    <br/>
                    <label>Verify Password:</label>
                    <br/>
                    <input type={'password'} required value={passwordVerify} onChange={(event) => {
                        setPasswordVerify(event.target.value);
                    }}/>
                    <br/>
                    <br/>
                    <label>Click any food groups to exclude from recommendations: </label>
                    <ToggleButtonGroup orientation={"horizontal"} value={forbiddenGroups} onChange={handleToggle}>
                        <ToggleButton id="nonSerifSignUp" value={"Dairy and Egg Products"}>Dairy/Egg
                            Products</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Spice and Herbs"}>Spices/Herbs</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Fats and Oils"}>Fats & Oils</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Sausages and Luncheon Meats"}>Sausages & Luncheon
                            Meats</ToggleButton>
                    </ToggleButtonGroup>
                    <ToggleButtonGroup orientation={"horizontal"} value={forbiddenGroups} onChange={handleToggle}>
                        <ToggleButton id="nonSerifSignUp" value={"Fruits and Fruit Juices"}>Fruits & Fruit
                            Juices</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Pork Products"}>Pork Products</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Vegetables and Vegetable Products"}>Vegetables &
                            Vegetable Products</ToggleButton>
                    </ToggleButtonGroup>
                    <ToggleButtonGroup orientation={"horizontal"} value={forbiddenGroups} onChange={handleToggle}>
                        <ToggleButton id="nonSerifSignUp" value={"Nut and Seed Products"}>Nuts & Seeds</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Beef Products"}>Beef Products</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Finfish and Shellfish Products"}>Finfish & Shellfish
                            Products</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Legumes and Legume Products"}>Legumes & Legume
                            Products</ToggleButton>
                    </ToggleButtonGroup>
                    <ToggleButtonGroup orientation={"horizontal"} value={forbiddenGroups} onChange={handleToggle}>
                        <ToggleButton id="nonSerifSignUp" value={"Lamb, Veal, and Game Products"}>Lamb, Veal, & Game
                            Products</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Cereal Grains and Pasta"}>Cereal, Grains, &
                            Pasta</ToggleButton>
                        <ToggleButton id="nonSerifSignUp" value={"Breakfast Cereals"}>Breakfast Cereals</ToggleButton>
                    </ToggleButtonGroup>
                    <br/>
                    <br/>
                    <input required type={'checkbox'}/> By checking this box, I consent to everything listed to the right.
                    <br/>
                    <input type={'submit'} value={'Sign Up!'}/>
                    <br/>
                </form>
                <h4>{errorMessage}</h4>
            </div>

            <div className="consent">
                <h1 className="consentHeader"> Consent </h1>
                <h3><em>please read before making an account</em></h3>
                <h4 id="serif">We would like to be fully transparent with the data and information that you choose to entrust with us. By signing up with <em>Chomp!</em>, you are giving us permission to store and use information about the food that you log (custom or otherwise), the statistics that we gather about the food that you’ve logged, as well as the dietary preferences you fill out in the form. We will also be storing and using the choices you make about the schedule to further suggest food options that better suit your dietary preferences..</h4>
            </div>
        </div>
    );
}

export default SignUp;
