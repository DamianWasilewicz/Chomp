import React, { Component } from "react";
import { ReactComponent as Banana } from './banana.svg';
import { ReactComponent as Apple } from './apple.svg';
import { ReactComponent as Cheese } from './cheese.svg';
import { ReactComponent as Broccoli } from './broccoli.svg';
import { ReactComponent as Burger } from './burger.svg';
import { ReactComponent as Carrot } from './carrot.svg';
import { ReactComponent as Milk } from './milk.svg';
import { ReactComponent as Fish } from './fish.svg';

/**
 * Component for graphics border, which are all imported svgs.
 */
function Border() {
    return (
        <div id="border">
            <Banana height="10vh" width="10vw"/>
            <Apple height="10vh" width="10vw"/>
            <Cheese height="10vh" width="10vw"/>
            <Broccoli height="10vh" width="10vw"/>
            <Burger height="10vh" width="10vw"/>
            <Carrot height="14vh" width="10vw"/>
            <Milk height="13vh" width="8vw"/>
            <Fish height="10vh" width="10vw"/>
        </div>
    );
}

export default Border;