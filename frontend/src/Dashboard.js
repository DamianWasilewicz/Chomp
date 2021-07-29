import React, {useEffect, useState} from 'react';
import axios from "axios";
import Textbox from "./Textbox";
import './Dashboard.css';

/**
 * Component for the Dashboard page. Shows stats for a user.
 */
function Dashboard() {
    // state variables
    const [stats, setStats] = useState([]);
    const [logs, setLogs] = useState([]);

    // initial useEffect, loads dashboard stats
    useEffect(() => {
        requestDashboardInfo()
    }, [])

    // requests stats info from backend
    const requestDashboardInfo = () => {
        const toSend = {
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/dashboardInfo",
            toSend,
            config
        ) .then(response => {
                console.log(response)
                setStats(response.data["stats"]);
                setLogs(response.data["allLogs"])
            }
        )

        .catch(function (error) {
            console.log(error);
        });
    }

    // lists contents from post request's array
    const listStats = () => {
        if (stats.length !== 0){
            return(
                <ul>
                    <h4 id="dashboardList">Average Proteins (last week): {stats[0]}g</h4>
                    <h4 id="dashboardList">Average Carbohydrates (last week): {stats[1]}g</h4>
                    <h4 id="dashboardList">Average Fats (last week): {stats[2]}g</h4>
                    <h4 id="dashboardList">Average Calories (last week): {stats[3]}kCal</h4>
                    <h4 id="dashboardList">Sum Proteins (last week): {stats[4]}g</h4>
                    <h4 id="dashboardList">Sum Carbohydrates (last week): {stats[5]}g</h4>
                    <h4 id="dashboardList">Sum Fats (last week): {stats[6]}g</h4>
                    <h4 id="dashboardList">Sum Calories (last week): {stats[7]}kCal</h4>
                </ul>
            )
        }
    }

    // lists contents from post request's array
    const listLogs = logs.map((pair) =>
        <h4>{pair[1] + ": " + pair[0]}</h4>
    );

    return(
        <div className="dashboard">
            <div className="instructions">
                <h2>Welcome to your stats page!</h2>
                <h5>Based on your logs for the past week, here are some averages and sums for your macros!</h5>
            </div>
            <br />
            <div className="stats">
                {listStats()}
            </div>
            <div>
                <div className="instructions">
                    <h2>History</h2>
                    <h5>These are all the logs that you've submitted!</h5>
                </div>
                <div className="listOfLogs">
                    {listLogs}
                </div>

            </div>
        </div>
    )
}
export default Dashboard;