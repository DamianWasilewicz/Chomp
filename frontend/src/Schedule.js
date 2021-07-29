import React, {useState} from 'react';
import axios from "axios";
import ScheduleTable from './ScheduleTable';

/**
 * Component for the Schedule page. Contains the table
 */
function Schedule(){
    return(
        <div> <ScheduleTable/> </div>
    )
}
export default Schedule;