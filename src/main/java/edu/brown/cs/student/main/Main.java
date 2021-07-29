package edu.brown.cs.student.main;

import edu.brown.cs.student.Authentication.CreateUser;
import edu.brown.cs.student.user.DataProcessing;
import edu.brown.cs.student.user.Repl;

import java.io.FileNotFoundException;
import java.sql.SQLException;

//Imports for reading input
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

//imports for communicating with frontend
import edu.brown.cs.student.user.User;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONArray;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Main class where we run the backend of our application.
 */
public final class Main {
  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private DataProcessing process;
  private static CreateUser userManager;
  private static User currentUser;
  private final int lastWeek = -7;
  private final int numDays = 7;

  /**
   * Runs the backend.
   * @param args List of Strings that determine the specifications of the current session.
   * @throws SQLException If there is an issue executing the SQL query.
   * @throws ClassNotFoundException If the class is not found.
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException, FileNotFoundException {
    new Main(args).run();
  }

  private final String[] args;

  /**
   * Constructor for main.
   * @param args List of Strings that determine the specifications of the current session.
   */
  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Runs the app (backend).
   */
  private void run() throws FileNotFoundException, SQLException, ClassNotFoundException {
    process = new DataProcessing();
    userManager = new CreateUser(process, "data/Users.db");
//    process.loadData();
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
      .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
    try {
      process.loadData();
    } catch (Exception e) {
      System.out.println("Exception occurred while running main");
      e.printStackTrace();
      return;
    }
//    process.resetPriorityTables();
//    process.updatePriorityTables();
//    process.resetDateTables();
//    process.updateTimeSinceSeenTables();
    Repl repl = new Repl();
  }

  /**
   * Runs the server on the specified port.
   * @param port The port the server will run on.
   */
  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    // Setup Spark Routes
    Spark.post("/scheduleInfo", new ScheduleHandler());
    Spark.post("/login", new LoginHandler());
    Spark.post("/signup", new SignUpHandler());

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }
      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }
      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    // set spark routes
    Spark.post("/logInfo", new LogHandler());
    Spark.post("/scheduleInfo", new ScheduleHandler());
    Spark.post("/dashboardInfo", new DashboardHandler());
    Spark.post("/customForm", new CustomFormHandler());
    Spark.post("/nutritionInfo", new NutritionHandler());
  }

  /**
   * Manages retrieving new Schedule.
   */
  private class ScheduleHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      JSONArray scheduleArr = data.getJSONArray("outcomes");
      currentUser.updatePrioritiesFromSchedule(scheduleArr);
      process.updatePriorities(currentUser.getID(), currentUser.getFoodList());
      process.updateDates(currentUser.getID(), currentUser.getFoodList());
      List<List<List<String>>> schedule = currentUser.getSchedule();
      Map<String, Object> variables = ImmutableMap.of("scheduleInfo", schedule);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles logging the user in - includes authentication, returning -1 for the
   * user id if a user was not found with that name and password combination.
   */
  private class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      String password = data.getString("password");
      //gets userID using createUser, sets currentUser to user with that ID.
      System.out.println(username);
      int userId = userManager.getUserFromDatabase(username, password);
      currentUser = new User(process, userId);
      System.out.println("Successfully created a user!");
      String message = "";
      if (userId < 0) {
        message = "Username or password incorrect :(";
      }
//      currentUser = newUser.getCurrentUser();
      Map<String, Object> variables = ImmutableMap.of("userId", userId, "message", message);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles signing up a new user.
   */
  private static class SignUpHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String username = data.getString("username");
      String password = data.getString("password");
      String passwordVerify = data.getString("passwordVerify");
      String message = "";
      JSONArray forbiddenRaw = data.getJSONArray("forbidden");
      List<String> forbidden = new ArrayList<>();

      for (int i = 0; i < forbiddenRaw.length(); i++) {
        forbidden.add(forbiddenRaw.getString(i));
      }



      if (!password.equals(passwordVerify)) {
        message += "Passwords do not match";
      }
      int userId = -1;

      if (!userManager.usernameTaken(username)) {
        userId = userManager.addUser(username, password, forbidden);
      } else {
        message += "username taken!";
      }

      if (userId < 0) {
        message += " error creating user";
      }

//      currentUser = newUser.getCurrentUser();
      Map<String, Object> variables = ImmutableMap.of("userId", userId, "message", message);
      return GSON.toJson(variables);
    }
  }

  /**
   * Manages retrieving dashboard info.
   */
  private class DashboardHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String dashboardInfo = "";
      List<String> stats = new ArrayList<>();
      List<List<String>> allLogs = new ArrayList<>();
      String id = Integer.toString(currentUser.getUserId());

      // 2d array where each row is [food id, date]
      List<List<String>> logs = process.queryLogs(id);

      // gets the date of a week earlier
      Date dateOfOrder = new Date();
      int noOfDays = lastWeek;
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateOfOrder);
      calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
      Date weekAgoDate = calendar.getTime();
      System.out.println("week ago date: " + weekAgoDate);
      if (logs != null) {
        //TODO: get stats on logs
        //NOTE: For now I will assume that the log query has
        // only retrieved from the most recent week
        int carbs = 0;
        int fats = 0;
        int proteins = 0;
        int calories = 0;
        for (int i = 0; i < logs.size(); i++) {
          System.out.println("queried log size" + logs.size());
          //translates the string log into the current date
          try {
            allLogs.add(convertLog(logs.get(i)));

            String logDate = logs.get(i).get(1);
            int day = Integer.parseInt(logDate.substring(0, 2));
            int month = Integer.parseInt(logDate.substring(3, 5));
            int year = Integer.parseInt(logDate.substring(6));
            System.out.println("date: " + month + "/" + day + "/" + year);
            Calendar calendarLog =  Calendar.getInstance();
            calendarLog.set(year, month - 1, day);
            Date objectLogDate = calendarLog.getTime();
            System.out.println("objectlogdate: " + objectLogDate);

            // checks that the date of a log is within the last week before
            // adding it to the calculation.
            System.out.println("compareTo result: " + objectLogDate.compareTo(weekAgoDate));
            if (objectLogDate.compareTo(weekAgoDate) >= 0) {
              System.out.println("within week");
              //STEP 1: Get the nutritional facts using the query that Chotoo set up
              //TODO: FIX THIS USING DAMIAN'S FORMAT SO I DON'T QUERY ON EVERY LOOP
              Map<String, Double> nutritionalInfo = process.getNutrition(
                      (int) Double.parseDouble(logs.get(i).get(0)), id);
              //STEP 2: Calculate the accumulation of all
              //the nutritional values in local instance variables
              if (nutritionalInfo != null) {
                carbs += nutritionalInfo.get("Carbohydrates");
                proteins += nutritionalInfo.get("Protein");
                fats += nutritionalInfo.get("Fat");
                calories += nutritionalInfo.get("Calories");
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        //adding to stats in this order
        //   index:      value:
        //    0       avg proteins/day
        //    1       avg carbs/day
        //    2       avg fats/day
        //    3       avg calories/day
        //    4       total weekly proteins
        //    5       total weekly carbs
        //    6       total weekly fats
        //    6       total weekly calories
        stats.add(String.valueOf(proteins / numDays));
        stats.add(String.valueOf(carbs / numDays));
        stats.add(String.valueOf(fats / numDays));
        stats.add(String.valueOf(calories / numDays));
        stats.add(String.valueOf(proteins));
        stats.add(String.valueOf(carbs));
        stats.add(String.valueOf(fats));
        stats.add(String.valueOf(calories));


        //STEP 3: Calculate the averages
        dashboardInfo = "Successfully retrieved stats for " + id + "!";
      } else {
        dashboardInfo = "no logs yet! try logging in foods here to get data on your diet!";
      }
      Map<String, Object> variables = ImmutableMap.of("dashboardInfo", dashboardInfo, "stats", stats, "allLogs", allLogs);
      return GSON.toJson(variables);
    }

    // converts a log from food id and date to food name and date
    private List<String> convertLog(List<String> aLog) {
      List<String> convertedLog = new ArrayList<>();
      String foodID = process.queryFoodFromId(aLog.get(0));
      convertedLog.add(foodID);
      convertedLog.add(aLog.get(1));
      return convertedLog;
    }
  }

  /**
   * Manages retrieving nutritional info about a certain food.
   */
  private class NutritionHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
      String foodName = "";
      try {
        JSONObject data = new JSONObject(request.body());
        foodName = data.getString("foodName");
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }

      String nutritionMessage = "";
      String id = Integer.toString(currentUser.getUserId());

      List<String> result = process.queryNutrition(foodName, id);

      if (result == null) {
        System.out.println("food not found");
        nutritionMessage =
                "food was not found in our database, nor in your list of custom foods :)";
        result = new ArrayList<>();
      } else {
        nutritionMessage = "here's the deets on " + foodName;
      }

      Map<String, Object> variables = ImmutableMap.of(
              "nutritionMessage", nutritionMessage, "nutritionFacts", result);
      return GSON.toJson(variables);
    }
  }

  /**
   * Manages inserting info into logs database.
   */
  private class LogHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      List<String> insertInfo = new ArrayList<>();

      JSONObject data = new JSONObject(request.body());

      //get info from front end
      String id = Integer.toString(currentUser.getUserId());
      System.out.println("here");
      String food = data.getString("food");
      String date = data.getString("date");

      // pass info to dataProcessing to insert into logs db
      insertInfo.add(id);
      insertInfo.add(food);
      insertInfo.add(date);

      int result = process.insertLog(insertInfo);

      String logInfo = "";     // success or error message depending on how the insertion went ;)
      String custom = "0";     // boolean for case 2 -- user needs to fill in custom food log
      switch (result) {
        case -1:
          logInfo = "Something (not your fault) went wrong... please hold.";
          break;
        case 1:
          logInfo = "food was not found in our database, please fill in a custom food log :)";
          custom = "1";
          break;
        case 2:
          logInfo = "please follow the MM/DD/YYYY format for the date";
          break;
        default:
          logInfo = food + " logged successfully!";
          break;
      }

      Map<String, Object> variables = ImmutableMap.of("logInfo", logInfo, "custom", custom);
      return GSON.toJson(variables);
    }
  }

  /**
   * Manages inserting custom food info into CustomFood database.
   */
  private class CustomFormHandler implements Route {

    /**
     * attempts to insert info from the custom food form into CustomForm db.
     *
     * @param request
     * @param response
     * @return success/failure string to display in the front end
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {

      //get info from front end
      JSONObject data = new JSONObject(request.body());
      String id = Integer.toString(currentUser.getUserId());
      String date = data.getString("date");
      String food = data.getString("food");
      String cals = data.getString("cals");
      String protein = data.getString("protein");
      String fat = data.getString("fat");
      String carbs = data.getString("carbs");

      // pass info to dataProcessing to insert into food into CustomFoods
      List<String> insertInfo = new ArrayList<>();
      insertInfo.add(food);
      insertInfo.add(cals);
      insertInfo.add(protein);
      insertInfo.add(fat);
      insertInfo.add(carbs);
      insertInfo.add(id);
      insertInfo.add(date);

      // method also logs the entry
      int result = process.insertCustomLog(insertInfo);

      // success or error message depending on how the insertion went ;)
      String logInfo = " ";
      switch (result) {
        case -1:
          logInfo = "Something (not your fault) went wrong... please hold.";
          break;
        case 1:
          logInfo = "calories should be a valid number.";
          break;
        case 2:
          logInfo = "protein should be a valid number.";
          break;
        case 3:
          logInfo = "fats should be a valid number.";
          break;
        case 4:
          logInfo = "carbs should be a valid number.";
          break;
        case 5:
          logInfo = "please follow the MM/DD/YYYY format for the date";
          break;
        default:
          logInfo = "successfully created a new custom food and logged!";
          break;
      }

      Map<String, Object> variables = ImmutableMap.of("logInfo", logInfo);
      return GSON.toJson(variables);
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
