import React, {useState} from 'react';
import { useHistory } from 'react-router-dom';
import './About.css'
import jordan from './jordan.jpg';
import damian from './damian.jpg';
import chotoo from './chotoo.jpg';
import eva from './eva.JPG';
import Border from "./Border";

/**
 * Component for the about page. Contains blurbs about the program and the
 * developers, as well as a button to log in and/or sign up.
 */
function About() {

    const [bio, setBio] = useState("")

    // state variable for linking Login component
    const history = useHistory();

    // function for login button's onClick. Renders Login component
    const triggerLogin = () => {
        history.push('/login');
    }

    // function for clicking on a developer's picture
    const toggleBio = (person) => {
        if (person === "eva") {
            setBio("Eva Lau forgot to write a bio until the last minute because she spends too much time messing around\n" +
                "with fun UIUX stuff. She's from Boston and Hong Kong and is a little upset at how the food database her group\n" +
                "and her ended up choosing doesn't have a lot of the yummy foods she'd eat in Hong Kong.\n" +
                "“My favorite food to log is InsertCustomFoodHere.”")
        } else if (person === "damian") {
            setBio("Damian Wasilewicz is a sophomore from New York City, New York (and will let you know about it!)\n" +
                "As a member of the food-eating community from a young age, Damian’s always on the prowl for new\n" +
                "meals to try out, and sees CHOMP as a way to expand the palates of the site’s users.\n" +
                "“Without CHOMP, I never would have tried beluga whale oil, or even jellyfish, dried, salted!”")
        } else if (person === "chotoo") {
            setBio("Chotoo Amin is a sophomore from Charlotte, North Carolina. As someone who eats on the unhealthy\n" +
                "side, working on Chomp has been a real wake up call. In his words, “I have been blessed to be a\n" +
                "part of this project. Chomp has given me the tools to turn the corner in terms of my food\n" +
                "choices.”")
        } else if (person === "jordan") {
            setBio("Jordan Walendom is a sophomore from Abidjan, Côte d’Ivoire (as well as other places but he says\n" +
                "Abidjan to make himself sound interesting.) Some of the cheeses he enjoys are “Cheese, brie”\n" +
                "and “Cheese, gouda” but is excited for future cheeses that CHOMP! recommends. Overall, Jordan\n" +
                "undertook this project due to his passion for eating. “Nom nom nom,” he expressively told us.")
        }
    }

    return(
        <div className="about">
            <div> <button onClick={triggerLogin}> Log In/Sign Up Here! </button> </div>
            <div className="chompAboutBlurb">
                <h1> <strong>CHOMP!</strong> </h1>
                <h3> <em>plan better, eat healthier</em> </h3>
                <h4 id="serif">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</h4>
            </div>

            <Border />

            <div className="developerBlurb">
                <h1> <strong>About us!</strong> </h1>
                <h3> <em>Four students, hanging on for dear life in a pandemic ridden semester.</em> </h3>
                <div className="pics">
                    <figure>
                        <img src={eva} alt="Eva" width="200" height="210" onClick={() => toggleBio("eva")}/>
                        <figcaption id="nonSerifAbout">Eva Lau</figcaption>
                    </figure>
                    <figure>
                        <img src={damian} alt="Damian" width="200" height="210" onClick={() => toggleBio("damian")} />
                        <figcaption id="nonSerifAbout">Damian Wasilewicz</figcaption>
                    </figure>
                    <figure>
                        <img src={chotoo} alt="Chotoo" width="200" height="210" onClick={() => toggleBio("chotoo")} />
                        <figcaption id="nonSerifAbout">Chotoo (Ahmed) Amin</figcaption>
                    </figure>
                    <figure>
                        <img src={jordan} alt="Jordan" width="200" height="210" onClick={() => toggleBio("jordan")} />
                        <figcaption id="nonSerifAbout">Jordan Walendom</figcaption>
                    </figure>





                </div>
                <div className="pics">
                    {bio}
                </div>

            </div>
        </div>

    )
}

export default About;