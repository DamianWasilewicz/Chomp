# README

## CHOMP!

### 0. Partner division of labor
- `Eva`
  - Designing frontend / UIUX
    - Displaying error messages for various components (logging, custom food creation, nutrition, dashboard, logging in)
    - Logging in and splash page (content blurb)
    - Sign up page (consent blurb)
    - Dashboard stats for user:
    - ~aesthetics~
      - rearranging/reorganizing components
      - css & material UI styling
  - Communicating with backend databases
    - Logging food
    - CustomFood creations
    - Getting nutrition information
    - Displaying stats
  for current user during last week, average macros & sum macros


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
  - Frontend logic of user choosing which food groups they don’t want to be shown
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
  - Worked on implementing a recency score for each food so that the same foods don’t show up repeatedly
  

- `Jordan`
  - Users Class
    - Created a class that handles the creation of a user and the information associated with a user object
    - Incorporated the prebuilt query’s Chotoo created to retrieve the relevant information from the database
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



### 2. Design details specific to your code
- In `Main`, multiple handlers handle specific functionality in the frontend
- The `User` class handles the creation of a user object and all its local variables
- The `Food` class handles the creation of a food object and is used for the construction of a priority queue
- The `CipherHandler` class handles encription for user creation
- The `Algorithms` class handles the ELO calculation to determine the food suggests to a user



<<<<<<< HEAD
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
=======
### 4. How to run any tests you wrote/tried by hand
- //TODO


### 5. How to build/run your program
- //TODO
>>>>>>> 53f6c3ca87b01da31ec86142b61e595f0d0d7420

  
