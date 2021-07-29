import React, {useState, useEffect, useRef} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import ScheduleElement from './ScheduleElement';
import ToggleButton from '@material-ui/lab/ToggleButton';
import ToggleButtonGroup from '@material-ui/lab/ToggleButtonGroup';
import './ScheduleTable.css';
import axios from "axios";

function ScheduleTable(props) {
    //Schedule variables
    let show = true;
    const [schedule, setSchedule] = useState({
        sunday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        },
        monday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        },
        tuesday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        },
        wednesday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        },
        thursday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        },
        friday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        },
        saturday: {
            breakfast: ["", "", ""],
            lunch: ["", "", ""],
            dinner: ["", "", ""],
        }
    })

    const [selections, setSelections] = useState({
        sunday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            }
        },
        monday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            },
        },
        tuesday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            },
        },
        wednesday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            },
        },
        thursday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            },
        },
        friday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            },
        },
        saturday: {
            breakfast: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            lunch: {
                winner: "",
                loserA: "",
                loserB: "",
            },
            dinner: {
                winner: "",
                loserA: "",
                loserB: "",
            },
        }
    })

    function updateWinner(day, meal, winner, loserA, loserB) {
        // debugger
        console.log("Updated winner");
        // let newSelections = selections.current;
        let newSelections = {...selections};
        newSelections[day][meal].winner = winner;
        newSelections[day][meal].loserA = loserA;
        newSelections[day][meal].loserB = loserB;
        setSelections(newSelections);
        console.log(selections);
    }


    function updateSchedule(newSched) {
        let newSchedule = {
            sunday: {
                breakfast: [newSched[0][0][0], newSched[0][0][1], newSched[0][0][2]],
                lunch: [newSched[0][1][0], newSched[0][1][1], newSched[0][1][2]],
                dinner: [newSched[0][2][0], newSched[0][2][1], newSched[0][2][2]],
            },
            monday: {
                breakfast: [newSched[1][0][0], newSched[1][0][1], newSched[1][0][2]],
                lunch: [newSched[1][1][0], newSched[1][1][1], newSched[1][1][2]],
                dinner: [newSched[1][2][0], newSched[1][2][1], newSched[1][2][2]],
            },
            tuesday: {
                breakfast: [newSched[2][0][0], newSched[2][0][1], newSched[2][0][2]],
                lunch: [newSched[2][1][0], newSched[2][1][1], newSched[2][1][2]],
                dinner: [newSched[2][2][0], newSched[2][2][1], newSched[2][2][2]],
            },
            wednesday: {
                breakfast: [newSched[3][0][0], newSched[3][0][1], newSched[3][0][2]],
                lunch: [newSched[3][1][0], newSched[3][1][1], newSched[3][1][2]],
                dinner: [newSched[3][2][0], newSched[3][2][1], newSched[3][2][2]],
            },
            thursday: {
                breakfast: [newSched[4][0][0], newSched[4][0][1], newSched[4][0][2]],
                lunch: [newSched[4][1][0], newSched[4][1][1], newSched[4][1][2]],
                dinner: [newSched[4][2][0], newSched[4][2][1], newSched[4][2][2]],
            },
            friday: {
                breakfast: [newSched[5][0][0], newSched[5][0][1], newSched[5][0][2]],
                lunch: [newSched[5][1][0], newSched[5][1][1], newSched[5][1][2]],
                dinner: [newSched[5][2][0], newSched[5][2][1], newSched[5][2][2]],
            },
            saturday: {
                breakfast: [newSched[6][0][0], newSched[6][0][1], newSched[6][0][2]],
                lunch: [newSched[6][1][0], newSched[6][1][1], newSched[6][1][2]],
                dinner: [newSched[6][2][0], newSched[6][2][1], newSched[6][2][2]],
            },
        }
        setSchedule(newSchedule);
    }

    //initial UseEffect, populates initial values for schedule
    useEffect(() => {
       requestScheduleInfo();
    }, [])


    //function for making request to backend
    const requestScheduleInfo = () => {
        console.log(schedule)
        const toSend = {
            outcomes: createOutcomeList(),
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/scheduleInfo",
            toSend,
            config
        ).then(response => {
                console.log(response)
                updateSchedule(response.data['scheduleInfo'])
            }
        )
    }


    //Uses to stylize the schedule table
    const useStyles = makeStyles({
        table: {
            minWidth: 800,
            background: "#598E61",

        },
    });


    //Makes use of useStyles
    const classes = useStyles();

    //Creates a row for a particular meal - columns defined by meal type, followed by
    //the food options in those categories by day of the week
    function createData(category, sun, mon, tue, wed, thu, fri, sat) {
        return {category, sun, mon, tue, wed, thu, fri, sat}
    }

    //creates the 4d list of foods that have been selected based on whether they've been clicked
    const createOutcomeList = () => {
        let sunBWinner = selections.sunday.breakfast.winner;
        let sunBLoserA = selections.sunday.breakfast.loserA;
        let sunBLoserB = selections.sunday.breakfast.loserB;
        let sunLWinner = selections.sunday.lunch.winner;
        let sunLLoserA = selections.sunday.lunch.loserA;
        let sunLLoserB = selections.sunday.lunch.loserB;
        let sunDWinner = selections.sunday.dinner.winner;
        let sunDLoserA = selections.sunday.dinner.loserA;
        let sunDLoserB = selections.sunday.dinner.loserB;

        let sundayOutcomes = [
            [sunBWinner, sunBLoserA, sunBLoserB],
                [sunLWinner, sunLLoserA, sunLLoserB],
                [sunDWinner, sunDLoserA, sunDLoserB]
        ]

        let monBWinner = selections.monday.breakfast.winner;
        let monBLoserA = selections.monday.breakfast.loserA;
        let monBLoserB = selections.monday.breakfast.loserB;
        let monLWinner = selections.monday.lunch.winner;
        let monLLoserA = selections.monday.lunch.loserA;
        let monLLoserB = selections.monday.lunch.loserB;
        let monDWinner = selections.monday.dinner.winner;
        let monDLoserA = selections.monday.dinner.loserA;
        let monDLoserB = selections.monday.dinner.loserB;

        let mondayOutcomes = [
            [monBWinner, monBLoserA, monBLoserB],
                [monLWinner, monLLoserA, monLLoserB],
                [monDWinner, monDLoserA, monDLoserB]
        ]

        let tueBWinner = selections.tuesday.breakfast.winner;
        let tueBLoserA = selections.tuesday.breakfast.loserA;
        let tueBLoserB = selections.tuesday.breakfast.loserB;
        let tueLWinner = selections.tuesday.lunch.winner;
        let tueLLoserA = selections.tuesday.lunch.loserA;
        let tueLLoserB = selections.tuesday.lunch.loserB;
        let tueDWinner = selections.tuesday.dinner.winner;
        let tueDLoserA = selections.tuesday.dinner.loserA;
        let tueDLoserB = selections.tuesday.dinner.loserB;

        let tuesdayOutcomes = [
            [tueBWinner, tueBLoserA, tueBLoserB],
                [tueLWinner, tueLLoserA, tueLLoserB],
                [tueDWinner, tueDLoserA, tueDLoserB]
        ]


        let wedBWinner = selections.wednesday.breakfast.winner;
        let wedBLoserA = selections.wednesday.breakfast.loserA;
        let wedBLoserB = selections.wednesday.breakfast.loserB;
        let wedLWinner = selections.wednesday.lunch.winner;
        let wedLLoserA = selections.wednesday.lunch.loserA;
        let wedLLoserB = selections.wednesday.lunch.loserB;
        let wedDWinner = selections.wednesday.dinner.winner;
        let wedDLoserA = selections.wednesday.dinner.loserA;
        let wedDLoserB = selections.wednesday.dinner.loserB;

        let wednesdayOutcomes = [
            [wedBWinner, wedBLoserA, wedBLoserB],
                [wedLWinner, wedLLoserA, wedLLoserB],
                [wedDWinner, wedDLoserA, wedDLoserB]
        ]


        let thuBWinner = selections.thursday.breakfast.winner;
        let thuBLoserA = selections.thursday.breakfast.loserA;
        let thuBLoserB = selections.thursday.breakfast.loserB;
        let thuLWinner = selections.thursday.lunch.winner;
        let thuLLoserA = selections.thursday.lunch.loserA;
        let thuLLoserB = selections.thursday.lunch.loserB;
        let thuDWinner = selections.thursday.dinner.winner;
        let thuDLoserA = selections.thursday.dinner.loserA;
        let thuDLoserB = selections.thursday.dinner.loserB;

        let thursdayOutcomes = [
            [thuBWinner, thuBLoserA, thuBLoserB],
                [thuLWinner, thuLLoserA, thuLLoserB],
                [thuDWinner, thuDLoserA, thuDLoserB]
        ]

        let friBWinner = selections.friday.breakfast.winner
        let friBLoserA = selections.friday.breakfast.loserA
        let friBLoserB = selections.friday.breakfast.loserB
        let friLWinner = selections.friday.lunch.winner;
        let friLLoserA = selections.friday.lunch.loserA;
        let friLLoserB = selections.friday.lunch.loserB;
        let friDWinner = selections.friday.dinner.winner;
        let friDLoserA = selections.friday.dinner.loserA;
        let friDLoserB = selections.friday.dinner.loserB;

        let fridayOutcomes = [
            [friBWinner, friBLoserA, friBLoserB],
                [friLWinner, friLLoserA, friLLoserB],
                [friDWinner, friDLoserA, friDLoserB]
        ]


        let satBWinner = selections.saturday.breakfast.winner;
        let satBLoserA = selections.saturday.breakfast.loserA;
        let satBLoserB = selections.saturday.breakfast.loserB;
        let satLWinner = selections.saturday.lunch.winner;
        let satLLoserA = selections.saturday.lunch.loserA;
        let satLLoserB = selections.saturday.lunch.loserB;
        let satDWinner = selections.saturday.dinner.winner;
        let satDLoserA = selections.saturday.dinner.loserA;
        let satDLoserB = selections.saturday.dinner.loserB;

        let saturdayOutcomes = [
            [satBWinner, satBLoserA, satBLoserB],
                [satLWinner, satLLoserA, satLLoserB],
                [satDWinner, satDLoserA, satDLoserB]
        ]

        let outcomes = [sundayOutcomes, mondayOutcomes, tuesdayOutcomes, wednesdayOutcomes,
        thursdayOutcomes, fridayOutcomes, saturdayOutcomes];

        return outcomes;
    }

    //Creates a row for each food category - actual food items will be decided by state variable schedule
    const rows = [
        createData("breakfast", schedule.sunday.breakfast,
            schedule.monday.breakfast, schedule.tuesday.breakfast,
            schedule.wednesday.breakfast, schedule.thursday.breakfast,
            schedule.friday.breakfast, schedule.saturday.breakfast
        ),
        createData("lunch", schedule.sunday.lunch,
            schedule.monday.lunch, schedule.tuesday.lunch,
            schedule.wednesday.lunch, schedule.thursday.lunch,
            schedule.friday.lunch, schedule.saturday.lunch
        ),
        createData("dinner", schedule.sunday.dinner,
            schedule.monday.dinner, schedule.tuesday.dinner,
            schedule.wednesday.dinner, schedule.thursday.dinner,
            schedule.friday.dinner, schedule.saturday.dinner
        ),
    ]
    return (
        <div className="schedule">
            <div className="instructions">
                <h2>Welcome to your weekly meal plan!</h2>
                <h5>
                    Based on your dietary restrictions and previous meal choices, here are some options for your
                    meals this week. For each day, in each food group, pick the meal you'd like to eat out of the
                    three suggestions. We'll learn from your choices for next's week's meal plans.
                </h5>
                <h6>Click "Request New Schedule" to get next week's schedule</h6>
            </div>
            <button className="requestButton" onClick={requestScheduleInfo}>Request New Schedule</button>
            <TableContainer component={Paper}>
                <Table stickyHeader={true} className={classes.table}>
                    {/*Table header - contains info about what value will appear in each column*/}
                    <TableHead>
                        <TableCell id="nonSerif">Food Category</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Sunday</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Monday</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Tuesday</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Wednesday</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Thursday</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Friday</TableCell>
                        <TableCell id="nonSerif" align={"center"}>Saturday</TableCell>
                    </TableHead>
                    <TableBody>
                        {/*Maps each row, as constructed in rows function,
                    to a table row with each cell determined by the foods stored*/}
                        {rows.map((row) => (
                            <TableRow key={row.category}>
                                <TableCell component={"th"} scope={"row"}>
                                    {row.category}
                                </TableCell>
                                <TableCell align={"center"}>
                                    {/*Converts list of food items to list*/}
                                        <ToggleButtonGroup orientation={'vertical'} value={selections.sunday[row.category].winner}>
                                            <ToggleButton id="nonSerifSchedule" value={row.sun[0]} onClick={() => updateWinner('sunday', row.category, row.sun[0],
                                                row.sun[1], row.sun[2])} value={row.sun[0]}> {row.sun[0]}</ToggleButton>

                                            <ToggleButton id="nonSerifSchedule" value={row.sun[1]} onClick={() => updateWinner('sunday', row.category, row.sun[1],
                                                row.sun[0], row.sun[2]) } value={row.sun[1]}> {row.sun[1]}</ToggleButton>

                                            <ToggleButton id="nonSerifSchedule" value={row.sun[2]} onClick={() => updateWinner('sunday', row.category, row.sun[2],
                                                row.sun[0], row.sun[1]) } value={row.sun[2]}> {row.sun[2]}</ToggleButton>
                                        </ToggleButtonGroup>
                                </TableCell>
                                <TableCell align={"center"}>
                                    <ToggleButtonGroup orientation={'vertical'} value={selections.monday[row.category].winner}>
                                        <ToggleButton id="nonSerifSchedule" value={row.mon[0]} onClick={() => updateWinner('monday', row.category, row.mon[0],
                                            row.mon[1], row.mon[2])} value={row.mon[0]}> {row.mon[0]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.mon[1]} onClick={() => updateWinner('monday', row.category, row.mon[1],
                                            row.mon[0], row.mon[2]) } value={row.mon[1]}> {row.mon[1]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.mon[2]} onClick={() => updateWinner('monday', row.category, row.mon[2],
                                            row.mon[0], row.mon[1]) } value={row.mon[2]}> {row.mon[2]}</ToggleButton>
                                    </ToggleButtonGroup>
                                </TableCell>
                                <TableCell align={"center"}>
                                    <ToggleButtonGroup orientation={'vertical'} value={selections.tuesday[row.category].winner}>
                                        <ToggleButton id="nonSerifSchedule" value={row.tue[0]} onClick={() => updateWinner('tuesday', row.category, row.tue[0],
                                            row.tue[1], row.tue[2])} value={row.tue[0]}> {row.tue[0]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.tue[1]} onClick={() => updateWinner('tuesday', row.category, row.tue[1],
                                            row.tue[0], row.tue[2]) } value={row.tue[1]}> {row.tue[1]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.tue[2]} onClick={() => updateWinner('tuesday', row.category, row.tue[2],
                                            row.tue[0], row.mon[1]) } value={row.tue[2]}> {row.tue[2]}</ToggleButton>
                                    </ToggleButtonGroup>
                                </TableCell>
                                <TableCell align={"center"}>
                                    <ToggleButtonGroup orientation={'vertical'} value={selections.wednesday[row.category].winner}>
                                        <ToggleButton id="nonSerifSchedule" value={row.wed[0]} onClick={() => updateWinner('wednesday', row.category, row.wed[0],
                                            row.wed[1], row.wed[2])} value={row.wed[0]}> {row.wed[0]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.wed[1]} onClick={() => updateWinner('wednesday', row.category, row.wed[1],
                                            row.wed[0], row.wed[2]) } value={row.wed[1]}> {row.wed[1]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.wed[2]} onClick={() => updateWinner('wednesday', row.category, row.wed[2],
                                            row.wed[0], row.wed[1]) } value={row.wed[2]}> {row.wed[2]}</ToggleButton>
                                    </ToggleButtonGroup>
                                </TableCell>
                                <TableCell align={"center"}>
                                    <ToggleButtonGroup orientation={'vertical'} value={selections.thursday[row.category].winner}>
                                        <ToggleButton id="nonSerifSchedule" value={row.thu[0]} onClick={() => updateWinner('thursday', row.category, row.thu[0],
                                            row.thu[1], row.thu[2])} value={row.thu[0]}> {row.thu[0]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.thu[1]} onClick={() => updateWinner('thursday', row.category, row.thu[1],
                                            row.thu[0], row.thu[2]) } value={row.thu[1]}> {row.thu[1]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.thu[2]} onClick={() => updateWinner('thursday', row.category, row.thu[2],
                                            row.thu[0], row.thu[1]) } value={row.thu[2]}> {row.thu[2]}</ToggleButton>
                                    </ToggleButtonGroup>
                                </TableCell>
                                <TableCell align={"center"}>
                                    <ToggleButtonGroup orientation={'vertical'} value={selections.friday[row.category].winner}>
                                        <ToggleButton id="nonSerifSchedule" value={row.fri[0]} onClick={() => updateWinner('friday', row.category, row.fri[0],
                                            row.fri[1], row.fri[2])} value={row.fri[0]}> {row.fri[0]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.fri[1]} onClick={() => updateWinner('friday', row.category, row.fri[1],
                                            row.fri[0], row.fri[2]) } value={row.fri[1]}> {row.fri[1]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.fri[2]} onClick={() => updateWinner('friday', row.category, row.fri[2],
                                            row.fri[0], row.fri[1]) } value={row.fri[2]}> {row.fri[2]}</ToggleButton>
                                    </ToggleButtonGroup>
                                </TableCell>
                                <TableCell align={"center"}>
                                    <ToggleButtonGroup orientation={'vertical'} value={selections.saturday[row.category].winner}>
                                        <ToggleButton id="nonSerifSchedule" value={row.sat[0]} onClick={() => updateWinner('saturday', row.category, row.sat[0],
                                            row.sat[1], row.sat[2])} value={row.sat[0]}> {row.sat[0]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.sat[1]} onClick={() => updateWinner('saturday', row.category, row.sat[1],
                                            row.sat[0], row.sat[2]) } value={row.sat[1]}> {row.sat[1]}</ToggleButton>

                                        <ToggleButton id="nonSerifSchedule" value={row.sat[2]} onClick={() => updateWinner('saturday', row.category, row.sat[2],
                                            row.sat[0], row.sat[1]) } value={row.sat[2]}> {row.sat[2]}</ToggleButton>
                                    </ToggleButtonGroup>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    )
}

export default ScheduleTable;