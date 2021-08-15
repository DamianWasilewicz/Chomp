import React, {useState} from 'react';
import {Nav,
    NavItem} from 'react-bootstrap'
import {
    BrowserRouter as Router,
    Switch,
    Route,
    NavLink,
    Redirect
} from "react-router-dom";
import Schedule from './Schedule';
import Border from './Border';
import Log from './Log';
import Dashboard from './Dashboard';
import Login from './Login';
import SignUp from './SignUp';
import './App.css';
import About from "./About";

/**
 * Component for top level class - this will control overarching logic, will
 * be where all other components will be rendered
 */
function FoodApp() {
    // boolean state variable for whether or not logged in
    const [loggedIn, setLoggedIn]  = useState(false);

    return(
        <div className="foodApp">
            <Border />
            {/*
              The Router is composed of two parts - Links and Routes.
              The nav is a navigational element that lets the user select which view they want
              to currently view. The nav has links that the user cna click. Once a link is clicked,
              we enter the switch statement.

              The switch section will view whichever of the routes is currently selected.
              This is determined by the route with the same path as a link's "to" property
             */}
            <Router>
                <Nav fill variant={"tabs"}>
                    <NavItem>
                        <NavLink id="nav" to={"/about"}>About</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink id="nav" to={"/logging"}>Log</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink id="nav" to={"/schedule"}>Schedule</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink id="nav" to={"/dashboard"}>Dashboard</NavLink>
                    </NavItem>
                </Nav>

                {/*
                    The switch statement will only render the currently selected path.
                    By using the exact keyword, this prevents multiple routes from being rendered at once.
                    We also make use of the logged in state variable to determine whether to render a page,
                    or to redirect the user to the login page
                */}
                <div>
                    <Switch>
                        <Route exact path={"/"}>
                            <About loggedIn={loggedIn}/>
                        </Route>
                        <Route exact path={"/about"}>
                            <About loggedIn={loggedIn}/> {/*only one accessible without logging in*/}
                        </Route>
                        <Route exact path={"/logging"}>
                            {loggedIn ? <Log/> : <Redirect to={'/login'}/>}
                        </Route>
                        <Route exact path={"/schedule"}>
                            {loggedIn ? <Schedule/> : <Redirect to={'/login'}/>}
                        </Route>
                        <Route exact path={"/dashboard"}>
                            {loggedIn ? <Dashboard/> : <Redirect to={'/login'}/>}
                        </Route>
                        <Route exact path={'/login'}>
                            <Login setLoggedIn={setLoggedIn}/>
                        </Route>
                        <Route exact path = {"/signup"}>
                            <SignUp/>
                        </Route>
                    </Switch>
                </div>
            </Router>
            <Border />
        </div>
    );
}

export default FoodApp;