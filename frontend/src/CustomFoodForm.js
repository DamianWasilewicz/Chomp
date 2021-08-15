import Textbox from "./Textbox";
import React, {useState} from "react";
import axios from "axios";
import './Log.css';

/**
 * Component for the Custom Food Form that appears in the Log component
 * if the user enters a food that isn't in our databases.
 */
function CustomFoodForm(props) {
    const [cals, setCals] = useState("");
    const [protein, setProtein] = useState("");
    const [fat, setFat] = useState("");
    const [carbs, setCarbs] = useState("");
    const [message, setMessage] = useState("")

    // sends form information about food to backend, received success/failure message
    const sendForm = () => {
        const toSend = {
            // id: userId,
            date: props.date,
            food: props.name,
            cals: cals,
            protein: protein,
            fat: fat,
            carbs: carbs
        }

        console.log(toSend)
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/customForm",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["logInfo"])
                setMessage(response.data["logInfo"]);
            })

            .catch(function (error) {
                console.log(error);
            });
    }

    // conditional rendering if the display boolean prop is true
    if (props.display === "1"){
        return(
            <div>
                <div className="customForm">
                    <Textbox
                        type={"foodName"}
                        label={"Food Name:"}
                        change={props.setFood}
                        value={props.name}
                    />
                    <Textbox
                        type={"date of log"}
                        label={"Date:"}
                        change={props.setDate}
                        value={props.date}
                    />
                    <Textbox
                        type={"calories"}
                        label={"Calories (kCal):"}
                        change={setCals}
                    />
                    <Textbox
                        type={"protein"}
                        label={"Protein (g):"}
                        change={setProtein}
                    />
                    <Textbox
                        type={"fat"}
                        label={"Fat (g):"}
                        change={setFat}
                    />
                    <Textbox
                        type={"carbs"}
                        label={"Carbohydrates (g):"}
                        change={setCarbs}
                    />
                    <button className="submitLog" onClick={sendForm}> Log food! </button>
                </div>
                <div>
                    {message}
                </div>
            </div>
        );
    } else {
        return null
    }
}

export default CustomFoodForm;