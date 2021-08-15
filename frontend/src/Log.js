import React, {useState} from 'react';
import axios from "axios";
import Textbox from "./Textbox";
import './App.css';
import './Log.css';
import CustomFoodForm from "./CustomFoodForm";
import Border from './Border';

/**
 * Component for the Log page. Allows users to log their food,
 * make custom food logs, and get nutrition facts about a given food.
 */
function Log() {
    //State variables
    const [foodName, setFood] = useState("");
    const [dateStamp, setDate] = useState("");
    const [message, setMessage] = useState([]);
    const [nutrition, setNutrition] = useState("");
    const [nutritionInfo, setNutritionInfo] = useState([]);
    const [nutritionMsg, setNutritionMsg] = useState("");
    // 0 if custom form isn't needed, 1 if custom form is needed
    const [custom, setCustom] = useState("0");

    // sends log info to backend, gets success/error message & info about custom form
    function log() {
        const toSend = {
            food: foodName,
            date: dateStamp
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/logInfo",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["logInfo"])
                setMessage(response.data["logInfo"]);
                setCustom(response.data["custom"]);

            })

            .catch(function (error) {
                console.log(error);
            });
    }

    // sends food name to backend, gets information about the food back
    function getFax() {
        const toSend = {
            foodName: nutrition
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        axios.post(
            "http://localhost:4567/nutritionInfo",
            toSend,
            config
        )
            .then(response => {
                console.log(response)
                setNutritionInfo(response.data["nutritionFacts"])
                setNutritionMsg(response.data["nutritionMessage"])

            })

            .catch(function (error) {
                console.log(error);
            });
    }

    // labels and lists the stats for a food
    const labelledListNutrition = () => {
        if (nutritionInfo.length !== 0){
            return(
                <ul>
                    <h3>Proteins: {nutritionInfo[0]}g</h3>
                    <h3>Fats: {nutritionInfo[1]}g</h3>
                    <h3>Carbohydrates: {nutritionInfo[2]}g</h3>
                    <h3>Calories: {nutritionInfo[3]}kCal</h3>
                </ul>
            )
        }
    }

    return(
        <div className="log">
            <div className="welcomeLog">
                <h2>Welcome to the log page!</h2>
                <h5>Everytime you eat something, don't forget to log it here.</h5>
                <h5>If your food isn't known to our list of foods, you'll be prompted to fill in your own</h5>
            </div>

            <div className="logRow">
                <div>
                    <Textbox
                        type={"food"}
                        label={"Name of food"}
                        change={setFood}
                        id="logRowInput"
                    />
                </div>
                <div>
                    <Textbox
                        type={"integer"}
                        label={"Date eaten (dd/mm/yyyy)"}
                        change={setDate}
                        id="logRowInput"
                    />
                </div>

            </div>

            <div> <button onClick={log}> Submit Log! </button> </div>

            <div> <h3>{message}</h3> </div>

            {/*conditional render*/}
            <div> <CustomFoodForm setFood={setFood} setDate={setDate} name={foodName} date={dateStamp} display={custom}/> </div>

            <Border />

            <div className="nutrition">
                <Textbox
                    type={"food"}
                    label={"Wondering what's the nutrition info for a food?"}
                    change={setNutrition}
                />
            </div>

            <div> <button onClick={getFax}> Get Nutrition Info </button> </div>
            <br />
            <div>
                <h3><em>{nutritionMsg}</em></h3>
                <div>{labelledListNutrition()}</div>
            </div>
        </div>
    );
}

export default Log;