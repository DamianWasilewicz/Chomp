import React from 'react';

/**
 * Component for the ScheduleElement. Represents a food of the schedule.
 */
function ScheduleElement(props) {

    const returnLI = () => {
        {props.isClicked ? <li className={"clicked"}>
                {props.food}
        </li> :
        <li className={"unclicked"} onClick={props.updateWinner}>
                {props.food}
        </li>}
    }
    return (
        <div>
            {returnLI()}
        </div>
    );
}

export default ScheduleElement;