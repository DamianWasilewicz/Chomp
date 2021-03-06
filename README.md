# README

## CHOMP!
Chomp! is dedicated to helping people get on track and stay on track when it comes to eating healthy. We display useful, personalized statistics for our users while also advertising fresh alternatives for daily meal consumption. By providing users with meal options set to their specifications, Chomp! can meet the needs of just about anyone. Sign up today to get started on your journey with Chomp!

![Alt text](./images/HomePage.png?raw=true "Home")

We recommend you a variety of choices for each meal of each day of the week! By keeping track of your dietary restrictions and the food you've chosen, as well as how long it's been since you've chosen a certain food, we reccomend your schedule for next week. By using an ELO algorithm that updates foods' rankings when you choose them and not others but also taking into account recency, Chomp! learns the foods you enjoy and makes sure you're not eating the same meal every day!

![Alt text](./images/MealPlan.png?raw=true "MealPlan")


Get info about foods in our expansive database! If we don't have your meal item stored, you can add info to it, allowing it to be recognized as an item moving forward!

![Alt text](./images/Info.png?raw=true "Info")

Get stats about the nutritious value of the meals you've logged with us!

![Alt text](./images/Stats.png?raw=true "Stats")

(Note: User information is encrypted using the RSA encryption scheme!)


### 0. Partner division of labor
- `Eva`
  - Communicating with backend databases
    - Logging food and inserting into database
    - CustomFood creations 
    - Getting nutrition information
    - Displaying stats & logs
  - Designing frontend / UIUX
    - Displaying error messages for various components (logging, custom food creation, nutrition, dashboard, logging in)
    - About page & bios
    - Log in page
    - Sign up page
    - Dashboard stats/logs for user
    - ~aesthetics~
      - svg graphics
      - rearranging/reorganizing components
      - css & material UI styling
  
- `Damian`
  - Logging in/users, encryption
  - Setting up system of links and routes using the react router system
  - Creating backend version of user
  - ELO algorithm - updating priorities based on which food was clicked in backend and database
  - Schedule
    - Registering system of winners and losers for foods to send back
    - Using dictionary to represent different meal entries for each day of the week, for each meal of the week
    - Communicating with backend and returning schedule of new foods based off of the priority queues for each food
  - Helped out w getting time last seen to show up
  - Frontend logic of user choosing which food groups they don???t want to be shown
  - Created SQL statements that allow for querying and writing to thousands of columns dynamically based on contents of lists, avoiding needing to make a SQL query call within a loop
  
  
- `Chotoo`
  - Manually created Users database
    - Including distributed tables for date, priority
  - Handled some querying of the database
    - Creation of food id map
    - Creation of nutrition map
    - Getting nutrition facts for each food in the USDA database
    - Initially loading foods into a list for each user
  - Worked on functionality for user preferences when they sign up
    - Forbidden foods list
    - Checking if foods are forbidden before adding them to schedule
  - Worked on implementing a recency score for each food so that the same foods don???t show up repeatedly
  - Testing
    -   JUnit
    -   System/Repl
  

- `Jordan`
  - Users Class
    - Created a class that handles the creation of a user and the information associated with a user object
    - Incorporated the prebuilt query???s Chotoo created to retrieve the relevant information from the database
  - Food Class and Food Comparator
    - Supported the foods in the database with a class that served as a data structure in the backend
    - Constructed a comparator to compare foods for the priority queue
  - PriorityQueues
    - Handled the creation of three priority queues to incorporate different meals
    - Differentiated between different foods in the database to decide which food belongs to which meal
    - Created methods to update priorities and retrieve a relevant priority queue
  - Stats
    - Used the java.util.Date API to determine which logs were recorded within the past week
    - Calculated the summation of the nutrition elements within the past week
    - Calculated the daily average of the nutrition elements



### 1. Known bugs
n/a


### 2. Design details specific to your code
- Backend
  - In `Main`, multiple handlers handle specific functionality in the frontend
  - The `User` class handles the creation of a user object and all its local variables
  - The `Food` class handles the creation of a food object and is used for the construction of a priority queue
  - The `CipherHandler` class handles encription for user creation
  - The `Algorithms` class handles the ELO calculation to determine the food suggests to a user
- Frontend
  - `FoodApp` top level organizational class contais routes/links to different components
  - `About` splash/landing page
  - `Log` user can log food and request nutritional information
  - `Login` login page
  - `SignUp` signup page with food preference form for algorithm in backend
  - `CustomFoodLog` conditionally rendered in log.js when food isn't in usda database
  - `Dashboard` displays stats and history of logs
  - `ScheduleTable` interactive food rec schedule generated by algorithms
- Databases
  - `USDA Food` list of ~9000 "foods" from the USDA... not that good of a database tbh
  - `Users` own database, stores...
    -  user info
    - logs
    - custom foods
    - time since last seen (food) - algorithm
    - priority (food) - algorithm
    - forbidden food groups for users



### 3. How to run any tests you wrote/tried by hand
Run these commands in the terminal
- JUnit tests
  - `mvn package` or `mvn test`
- System tests
  - `./cs32-test system/*.test` 


### 4. How to build/run your program
- Open two terminals
  - In the first, run `./run --gui`
  - In the second, run `npm install` then `npm start`  

### 5. Sources
- [Encrypting user login/signups](https://howtodoinjava.com/java/java-security/java-aes-encryption-example/) 
- [Regex for verifying date](https://stackoverflow.com/questions/15491894/regex-to-validate-date-format-dd-mm-yyyy)


### Additional Notes
We have a Repl and a system test but found both to be rather unintuitive given that our app requires specific user interactions in the frontend.
As a result, when it comes to testing, we put much more of our focus on JUnit testing.
The Repl can be fully commented out without any effect on the actual running of the program.

Some JUnit tests are reliant on the person running them having an up-to-date Users database because there are a few tests that use records that are already
in the database to ensure that querying is working properly. If you do not have access to the Users database, simply comment out the JUnit tests to compile the program with mvn package.


  
