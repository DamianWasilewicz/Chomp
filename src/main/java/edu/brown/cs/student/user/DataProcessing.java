package edu.brown.cs.student.user;

import edu.brown.cs.student.food.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

/**
 * Class dedicated to database querying and manipulation.
 */
public class DataProcessing {

  private List<String> foodNames = new ArrayList<>();
  private List<Integer> foodID = new ArrayList<>();
  private final int numFoods = 8789;
  private final String userDbFilename = "data/Users.db";
  private final String foodDbFilename = "data/usda.sql3";
  private final int proteinId = 203;
  private final int fatId = 204;
  private final int carbId = 205;
  private final int calId = 208;
  private final int numTables = 10;
  private final int numColumns = 1025;
  private final int numColumns2 = 598;
  private final int numColumns3 = 1024;
  private final int randomBounds1 = 2000;
  private final int randomBounds2 = 9000;
  private static Connection conn1 = null;
  private static Connection conn2 = null;


  /**
   * Constructor for DataProcessing which sets the current database that will
   * be used.
   */
  public DataProcessing() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Could not communicate with the databsase");
    }
    String urlToDB = "jdbc:sqlite:" + foodDbFilename;
    try {
      conn1 = DriverManager.getConnection(urlToDB);
    } catch (SQLException throwables) {
      System.out.println("ERROR: Could not communicate with the databsase");
    }
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Could not communicate with the databsase");
    }
    urlToDB = "jdbc:sqlite:" + userDbFilename;
    try {
      conn2 = DriverManager.getConnection(urlToDB);
    } catch (SQLException throwables) {
      System.out.println("ERROR: Could not communicate with the databsase");
    }
  }

  /**
   * Retrieves all the food names and food id's from the USDA database.
   */
  public void loadData() {
    PreparedStatement prep = null;
    try {
      prep = conn1.prepareStatement("SELECT DISTINCT long_desc, id FROM 'food'");
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {
        foodNames.add(rs.getString(1));
        foodID.add(rs.getInt(2));
      }
      prep.close();
      rs.close();
    } catch (SQLException throwables) {
      System.out.println("ERROR: Communicating with the database");
    }
  }

  /**
   * Returns the names of all the foods in our database
   * NOTE: Order here matters as it is the same order used for priorities and foodGroupIds.
   *
   * @return an ordered list of the foods names
   */
  public List<String> getFoodNames() {
    return foodNames;
  }

  /**
   * Returns the ids of all the foods in our database
   * NOTE: Order, as described in the getFoodNames method, matters.
   *
   * @return an ordered list of the food ids
   */
  public List<Integer> getFoodID() {
    return foodID;
  }

  /**
   * Method that retrieves the priorities of the foods for a specific user as a specified
   * by the id parameter.
   *
   * @param id the id of the user from which we will retrieve the priorities
   * @return an ArrayList of the priorities of the food where the index of the
   * array indicates the specific food
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   */
  public List<Double> retrievePriorities(int id) throws ClassNotFoundException, SQLException {
    List<Double> priorities = new ArrayList<>();
    for (int i = 1; i < numTables; i++) {
      String table = "priorities" + String.valueOf(i);
      PreparedStatement prep = conn2.prepareStatement(
              "SELECT * FROM '" + table + "' WHERE id = ?");
      prep.setInt(1, id);
      ResultSet rs = prep.executeQuery();

      while (rs.next()) {
        if (i != numTables - 1) {
          for (int j = 2; j <= numColumns; j++) {
            priorities.add(rs.getDouble(j));
          }
        } else {
          for (int j = 2; j <= numColumns2; j++) {
            priorities.add(rs.getDouble(j));
          }
        }
      }

      rs.close();
      prep.close();
    }
    return priorities;
  }

  /**
   * Method that retrieves the dates of the foods for a specific user as a specified
   * by the id parameter.
   *
   * @param id the id of the user from which we will retrieve the priorities
   * @return an ArrayList of the priorities of the food where the index of the
   * array indicates the specific food
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   */
  public List<Double> retrieveTimeLastSeen(int id) throws ClassNotFoundException, SQLException {
    List<Double> timeLastSeen = new ArrayList<>();
    for (int i = 1; i < numTables; i++) {
      String table = "date" + String.valueOf(i);
      PreparedStatement prep = conn2.prepareStatement(
              "SELECT * FROM '" + table + "' WHERE id = ?");
      prep.setInt(1, id);
      ResultSet rs = prep.executeQuery();

      while (rs.next()) {
        if (i != numTables - 1) {
          for (int j = 2; j <= numColumns; j++) {
            timeLastSeen.add(rs.getDouble(j));
          }
        } else {
          for (int j = 2; j <= numColumns2; j++) {
            timeLastSeen.add(rs.getDouble(j));
          }
        }
      }

      rs.close();
      prep.close();
    }
    System.out.println("Length of time last seen list: " + timeLastSeen.size());
    return timeLastSeen;
  }


  /**
   * Gets the nutrition information for foods.
   *
   * @param foodId the id of the food whose information we are retrieving
   * @param userId userID
   * @return a hashmap mapping from the nutrional component to its value for the specified food
   */
  public Map<String, Double> getNutrition(int foodId, String userId) {
    Map<String, Double> nutrition = new HashMap<>();

    try {
      PreparedStatement prep = conn2.prepareStatement(
              "SELECT * FROM 'CustomFoods' WHERE foodId = ? AND userId = ?");
      prep.setInt(1, foodId);
      prep.setString(2, userId);

      ResultSet rs = prep.executeQuery();
      if (rs.next()) {
        nutrition.put("Protein", rs.getDouble("protein"));
        nutrition.put("Fat", rs.getDouble("fats"));
        nutrition.put("Carbohydrates", rs.getDouble("carbs"));
        nutrition.put("Calories", rs.getDouble("calories"));
        return nutrition;
      }

      rs.close();
      prep.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      PreparedStatement prep1 = conn1.prepareStatement(
              "SELECT amount FROM 'nutrition' WHERE food_id = ? AND nutrient_id = ?");
      prep1.setInt(1, foodId);
      prep1.setInt(2, proteinId);
      ResultSet rs1 = prep1.executeQuery();
      if (rs1.next()) {
        nutrition.put("Protein", rs1.getDouble(1));
      }
      rs1.close();
      prep1.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      PreparedStatement prep2 = conn1.prepareStatement(
              "SELECT amount FROM 'nutrition' WHERE food_id = ? AND nutrient_id = ?");
      prep2.setInt(1, foodId);
      prep2.setInt(2, fatId);
      ResultSet rs2 = prep2.executeQuery();
      if (rs2.next()) {
        nutrition.put("Fat", rs2.getDouble(1));
      }
      rs2.close();
      prep2.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      PreparedStatement prep3 = conn1.prepareStatement(
              "SELECT amount FROM 'nutrition' WHERE food_id = ? AND nutrient_id = ?");
      prep3.setInt(1, foodId);
      prep3.setInt(2, carbId);
      ResultSet rs3 = prep3.executeQuery();
      if (rs3.next()) {
        nutrition.put("Carbohydrates", rs3.getDouble(1));
      }
      rs3.close();
      prep3.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      PreparedStatement prep4 = conn1.prepareStatement(
              "SELECT amount FROM 'nutrition' WHERE food_id = ? AND nutrient_id = ?");
      prep4.setInt(1, foodId);
      prep4.setInt(2, calId);
      ResultSet rs4 = prep4.executeQuery();
      if (rs4.next()) {
        System.out.println("mapped cals");
        nutrition.put("Calories", rs4.getDouble(1));
      }
      rs4.close();
      prep4.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return nutrition;
  }

  /**
   * Method to create the priority table.
   * @throws SQLException If there is an error executing the SQL query.
   * @throws ClassNotFoundException If the class is not found.
   */
  public void resetPriorityTables() throws SQLException, ClassNotFoundException {
    int foodCounter = 0;
    for (int i = 1; i < numTables; i++) {
      String table = "priorities" + String.valueOf(i);
      PreparedStatement prep = conn2.prepareStatement(
              "CREATE TABLE IF NOT EXISTS " + table + " (id INTEGER, PRIMARY KEY (id))");
      prep.executeUpdate();
      prep.close();
    }
  }

  /**
   * Method to create data tables.
   * @throws SQLException If there is an error executing the SQL query.
   * @throws ClassNotFoundException If the class is not found.
   */
  public void resetDateTables() throws SQLException, ClassNotFoundException {
    int foodCounter = 0;
    for (int i = 1; i < numTables; i++) {
      String table = "date" + String.valueOf(i);
      PreparedStatement prep = conn2.prepareStatement(
              "CREATE TABLE IF NOT EXISTS " + table + " (id INTEGER, PRIMARY KEY (id))");
      prep.executeUpdate();
      prep.close();
    }
  }


  /**
   * ALters priority tables to add columns for food IDS.
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   */
  public void updatePriorityTables() throws ClassNotFoundException, SQLException {
    int foodCounter = 0;
    for (int i = 1; i < numTables; i++) {
      String table = "priorities" + String.valueOf(i);
      System.out.println(table);
      for (int j = 0; j < numColumns - 1; j++) {
        PreparedStatement prep = conn2.prepareStatement(
                "ALTER TABLE " + table + "  ADD '" + foodCounter + "'REAL");
        prep.executeUpdate();
        foodCounter++;
        prep.close();
      }
    }
  }

  /**
   * ALters time last seen tables to add columns for food IDS.
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   *
   */
  public void updateTimeSinceSeenTables() throws ClassNotFoundException, SQLException {
    int foodCounter = 0;
    for (int i = 1; i < numTables; i++) {
      String table = "date" + String.valueOf(i);
      for (int j = 0; j < numColumns - 1; j++) {
        PreparedStatement prep = conn2.prepareStatement(
                "ALTER TABLE " + table + "  ADD '" + foodCounter + "'REAL");
        prep.executeUpdate();
        foodCounter++;
        prep.close();
      }
    }
  }

  /**
   * Iterates through each of the priority tables and updates database priority value
   * based on value in foodlist.
   *
   * @param userID The integer id of the user we want to update priorities for.
   * @param foods The list of all foods in the database.
   */
  public void updatePriorities(int userID, List<Food> foods) {
    updatePriorityTable(userID, foods, 1, 0, numColumns3 - 1);
    updatePriorityTable(userID, foods, 2, numColumns3, (2 * numColumns3) - 1);
    updatePriorityTable(userID, foods, 3, 2 * numColumns3, (3 * numColumns3) - 1);
    updatePriorityTable(userID, foods, 4, 3 * numColumns3, (4 * numColumns3) - 1);
    updatePriorityTable(userID, foods, 5, 4 * numColumns3, (5 * numColumns3) - 1);
    updatePriorityTable(userID, foods, 6, 5 * numColumns3, (6 * numColumns3) - 1);
    updatePriorityTable(userID, foods,
            (numTables - 3), 6 * numColumns3, ((numTables - 3) * numColumns3) - 1);
    updatePriorityTable(userID, foods, (numTables - 2),
            (numTables - 3) * numColumns3, ((numTables - 2) * numColumns3) - 1);
    updatePriorityTable(userID, foods, (numTables - 1),
            (numTables - 2) * numColumns3, ((numTables - 2) * numColumns3) + 596);
  }

  /**
   * Iterates through each of the dates tables and updates database priority value
   * based on value in foodlist.
   *
   * @param userID The integer id of the user we want to update dates for.
   * @param foods The list of all foods from the database.
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   */
  public void updateDates(int userID, List<Food> foods)
          throws ClassNotFoundException, SQLException {
    updateDateTable(userID, foods, 1, 0, numColumns3 - 1);
    updateDateTable(userID, foods, 2, numColumns3, (2 * numColumns3) - 1);
    updateDateTable(userID, foods, 3, 2 * numColumns3, (3 * numColumns3) - 1);
    updateDateTable(userID, foods, 4, 3 * numColumns3, (4 * numColumns3) - 1);
    updateDateTable(userID, foods, 5, 4 * numColumns3, (5 * numColumns3) - 1);
    updateDateTable(userID, foods, 6, 5 * numColumns3, (6 * numColumns3) - 1);
    updateDateTable(userID, foods, (numTables - 3),
            6 * numColumns3, ((numTables - 3) * numColumns3) - 1);
    updateDateTable(userID, foods, (numTables - 2),
            (numTables - 3) * numColumns3, ((numTables - 2) * numColumns3) - 1);
    updateDateTable(userID, foods, (numTables - 1),
            (numTables - 2) * numColumns3, ((numTables - 2) * numColumns3) + 596);
  }


  /**
   * Helper method to update the database priorities of a particular priority table for
   * foods based on logical priority value.
   * @param userID The integer id of the user we want to update priorities for.
   * @param foods The list of all foods from the database.
   * @param table The priority table we want to update.
   * @param start The starting index of the table.
   * @param end The ending index of the table.
   */

  private void updatePriorityTable(int userID, List<Food> foods, int table, int start, int end) {
    String statement = "UPDATE priorities" + table + " SET";
    for (int i = start; i < end; i++) {
      statement += " '" + i + "' = ?,";
    }
    statement += " '" + end + "' = ?";
    statement += "WHERE id = ?";
    try {
      System.out.println(statement);
      PreparedStatement prep = conn2.prepareStatement(statement);
      int index = 1;
      for (int j = start; j <= end; j++) {
        Food current = foods.get(j);
        double currentPriority = current.getPriority();
        prep.setDouble(index, currentPriority);
        index++;
      }
      prep.setInt(index, userID);
      prep.executeUpdate();
      prep.close();
    } catch (Exception e) {
      System.out.println("Exception occured while updating priority table");
      e.printStackTrace();
      return;
    }
  }

  /**
   * Helper method to update the database priorities of a particular priority table for
   * foods based on logical priority value.
   * @param userID The integer id of the user we want to update dates for.
   * @param foods The list of all foods from the database.
   * @param table The date table we want to update.
   * @param start The starting index in the table.
   * @param end The ending index in the table.
   */
  private void updateDateTable(int userID, List<Food> foods, int table, int start, int end) {
    String statement = "UPDATE date" + table + " SET";
    for (int i = start; i < end; i++) {
      statement += " '" + i + "' = ?,";
    }
    statement += " '" + end + "' = ?";
    statement += "WHERE id = ?";
    try {
      System.out.println(statement);
      PreparedStatement prep = conn2.prepareStatement(statement);
      int index = 1;
      for (int j = start; j <= end; j++) {
        Food current = foods.get(j);
        //Change this line to get current date instead of current priority
        double currentTimeSinceLastSeen = current.getTimeSinceLastSeen();
        prep.setDouble(index, currentTimeSinceLastSeen);
        index++;
      }
      prep.setInt(index, userID);
      prep.executeUpdate();
      prep.close();
    } catch (Exception e) {
      System.out.println("Exception occured while updating priority table");
      e.printStackTrace();
      return;
    }
  }

  public List<Double> getPriorities(int id) throws ClassNotFoundException, SQLException {
    List<Double> priorities = new ArrayList<>();
    for (int i = 1; i < numTables; i++) {
      String currentTable = "priorities" + String.valueOf(i);
      PreparedStatement prep = conn2.prepareStatement("SELECT priority FROM ? WHERE id = ?");
      prep.close();
    }
    return priorities;
  }

  /**
   * Creates a map with the food group id as an integer key and the the corresponding food
   * group as the string value.
   *
   * @return a map mapping from food group id (integer) to food group (string)
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   */
  public Map<Integer, String> createIdMap() throws ClassNotFoundException, SQLException {
    Map<Integer, String> foodGroups = new HashMap<>();
    PreparedStatement prep = conn1.prepareStatement("SELECT * FROM 'food_group'");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      foodGroups.put(rs.getInt(1), rs.getString(2));
    }
    prep.close();
    return foodGroups;
  }

  public Map<String, Integer> createIdMapStringKey() throws ClassNotFoundException, SQLException {
    Map<String, Integer> foodGroups = new HashMap<>();
    PreparedStatement prep = conn1.prepareStatement("SELECT * FROM 'food_group'");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      foodGroups.put(rs.getString(2), rs.getInt(1));
    }
    prep.close();
    return foodGroups;
  }

  /**
   * Retrieve the food group ids of the foods in order.
   * @return an array of food group ids in order
   * @throws ClassNotFoundException If the class is not found.
   * @throws SQLException If there is an error executing the SQL query.
   */
  public List<Integer> retrieveFoodGroupIds() throws ClassNotFoundException, SQLException {
    PreparedStatement prep = conn1.prepareStatement("SELECT food_group_id FROM 'food'");
    ResultSet rs = prep.executeQuery();
    List<Integer> ids = new ArrayList<>();
    while (rs.next()) {
      ids.add(rs.getInt(1));
    }
    prep.close();
    return ids;
  }

  /**
   * Queries the logs for a given user.
   * @param id The user's ID
   * @return list with log information (each row is a food id + date)
   */
  public List<List<String>> queryLogs(String id) {

    // sql code to query logs
    PreparedStatement prep;
    try {
      prep = conn2.prepareStatement("SELECT * "
              + " FROM logs WHERE "
              + "(logs.userId == ?);");
      prep.setString(1, id);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: prepareStatement SQL query failed.");
      return null;
    }

    // gets the food ID and date associated with each log
    try {
      ResultSet rs = prep.executeQuery();
      List<List<String>> logs = new ArrayList<>();

      while (rs.next()) {
        List<String> oneLog = new ArrayList<>();
        String foodId = rs.getString("foodID");
        String date = rs.getString("date");
        oneLog.add(foodId);
        oneLog.add(date);
        logs.add(oneLog);
      }

      rs.close();
      prep.close();

      return logs;
    } catch (Exception e) {

      System.out.println("ERROR: Could not query Check ins");
      return null;
    }
  }

  /**
   * Queries for a food ID in the USDA Food db.
   *
   * @param foodName name of food
   * @return foodID if it's in USDA Food db; null otherwise
   */
  public String queryDBFood(String foodName) {
    // queries into food table, tries to find ID of food with given foodName
    PreparedStatement prep;
    try {
      prep = conn1.prepareStatement("SELECT food.id "
              + " FROM food WHERE "
              + "(food.long_desc == ?);");

      prep.setString(1, foodName);

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: prepareStatement SQL query failed.");
      return null;
    }

    // check if result set is not empty - food exists
    ResultSet rs;
    try {
      rs = prep.executeQuery();
      if (rs.next()) {
        return rs.getString("id");
      }

      rs.close();
      prep.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: executeQuery failed.");
      return null;
    }

    return null;
  }

  /**
   * Queries for a food ID in the CustomFoods table.
   *
   * @param foodName name of food
   * @param userId   user ID
   * @return foodID if food is in CustomFoods table; null otherwise
   */
  public String queryCustomFood(String foodName, String userId) {
    // queries into CustomFoods, tries to find ID of food with given foodName and userID
    PreparedStatement prep;
    try {
      prep = conn2.prepareStatement("SELECT CustomFoods.foodID "
              + " FROM CustomFoods WHERE "
              + "(CustomFoods.foodName == ?) AND (CustomFoods.userID == ?);");

      prep.setString(1, foodName);
      prep.setString(2, userId);

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: prepareStatement SQL query failed.");
      return null;
    }

    // returns food id in result set, if exists
    ResultSet rs;
    try {
      rs = prep.executeQuery();
      if (rs.next()) {
        String toReturn = rs.getString("foodID");
        rs.close();
        prep.close();
        return toReturn;
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: executeQuery failed.");
      return null;
    }

    return null;
  }

  public String queryFoodNameFromId(String foodId, String userId) {
    // queries into CustomFoods, tries to find ID of food with given foodName and userID
    PreparedStatement prep;
    try {
      prep = conn2.prepareStatement("SELECT CustomFoods.foodName "
              + " FROM CustomFoods WHERE "
              + "(CustomFoods.foodId == ?) AND (CustomFoods.userID == ?);");

      prep.setString(1, foodId);
      prep.setString(2, userId);

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: prepareStatement SQL query failed.");
      return null;
    }

    // returns food id in result set, if exists
    ResultSet rs;
    try {
      rs = prep.executeQuery();
      if (rs.next()) {
        String toReturn = rs.getString("foodName");
        rs.close();
        prep.close();
        return toReturn;
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: executeQuery failed.");
      return null;
    }

    return null;
  }

  /**
   * Inserts custom food into CustomFood table
   * a single log into the logs table.
   *
   * @param log list of log fields
   * @return success or failure number
   * -1 - error: sql error :/
   * 1 - error: calories is not a double
   * 2 - error: proteins is not a double
   * 3 - error: fats is not a double
   * 4 - error: carbs is not a double
   * 5 - error: date format incorrect
   * 0 - inserted successfully!
   */
  public int insertCustomLog(List<String> log) {

    String foodId = Integer.toString((int) (Math.random() * (randomBounds1) + randomBounds2));
    String foodName = log.get(0);
    String calories = log.get(1);
    String protein = log.get(2);
    String fats = log.get(3);
    String carbs = log.get(4);
    String userId = log.get(5);
    String date = log.get(6);

    // validating cals is a double
    try {
      if (calories.equals("")) {
        return 1;
      }
      Double.parseDouble(calories);
    } catch (Exception e) {
      System.out.println("ERROR: calories are not a double");
      return 1;
    }

    // validating protein is a double
    try {
      if (protein.equals("")) {
        return 2;
      }
      Double.parseDouble(protein);
    } catch (Exception e) {
      System.out.println("ERROR: proteins are not a double");
      return 2;
    }

    // validating fat is a double
    try {
      if (fats.equals("")) {
        return 3;
      }
      Double.parseDouble(fats);
    } catch (Exception e) {
      System.out.println("ERROR: fats are not a double");
      return 3;
    }

    // validating carbs is a double
    try {
      if (carbs.equals("")) {
        return 4;
      }
      Double.parseDouble(carbs);
    } catch (Exception e) {
      System.out.println("ERROR: carbs are not a double");
      return 4;
    }

    // check date is formatted correctly
    Pattern pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
            + "(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2"
            + "\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579]"
            + "[26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|"
            + "[2-9]\\d)?\\d{2})$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(date);
    boolean matchFound = matcher.find();
    if (!matchFound) {
      System.out.println("ERROR: Date not in right format");
      return 5;
    }

    PreparedStatement prep;
    try {
      prep = conn2.prepareStatement(
              "INSERT INTO CustomFoods VALUES (?, ?, ?, ?, ?, ?, ?);");

      prep.setString(1, userId);
      prep.setString(2, foodId);
      prep.setString(3, foodName);
      prep.setString(4, calories);
      prep.setString(5, protein);
      prep.setString(6, fats);
      prep.setString(7, carbs);

      prep.addBatch();
      prep.executeBatch();
      prep.close();

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: insert failed");
      return -1;
    }

    // inserts log into logs table
    List<String> logData = new ArrayList<>();
    logData.add(userId);
    logData.add(foodName);
    logData.add(date);
    this.insertLog(logData);

    return 0;
  }

  /**
   * Inserts a single log into the logs table.
   *
   * @param log list of 1 log
   *            index 0: userId
   *            index 1: foodname
   *            index 2: date
   * @return success or failure number
   * -1 - error: sql error :/
   * 1 - error: food isn't in the CustomFood nor USDA Food db
   * 3 - error: date isn't formatted correctly
   * 0 - inserted successfully!
   **/
  public int insertLog(List<String> log) {
    String userID = log.get(0);
    String foodName = log.get(1);
    String date = log.get(2);

    // check if food is in the CustomFood or USDAFood db
    String foodId = queryCustomFood(foodName, userID);
    if (foodId == null) {
      foodId = queryDBFood(foodName);
      if (foodId == null) {
        System.out.println("User must create own food log");
        return 1;
      }
    }

    // check date is formatted correctly
    Pattern pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)"
            + "(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2"
            + "\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579]"
            + "[26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|"
            + "[2-9]\\d)?\\d{2})$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(date);
    boolean matchFound = matcher.find();
    if (!matchFound) {
      System.out.println("ERROR: Date not in right format");
      return 2;
    }

    if (date.length() < numTables) {
      StringBuilder dateFixer = new StringBuilder(date);
      //if the first slash is out of place
      if (date.indexOf("/") != 2) {
        //it means that the month is just m so make mm
        dateFixer.insert(0, '0');
        //if the first and second slash is out of place
        //it means day is just d so make dd
        if (date.indexOf("/", 2) != 4) {
          dateFixer.insert(3, '0');
          //if both month and day were wrong and year is wrong
          if (date.substring(4).length() == 2) {
            dateFixer.insert(6, '0');
            dateFixer.insert(6, '2');
          }
          //if the month is incorrect, the day is correct, y is incorrect
        } else if (date.substring(5).length() == 2) {
          dateFixer.insert(6, '0');
          dateFixer.insert(6, '2');
        }
        //if the month is correct and the day is incorrect
      } else if (date.indexOf("/", 3) != 5) {
        dateFixer.insert(3, '0');
        //if the day and the year are wrong
        if (date.substring(5).length() == 2) {
          dateFixer.insert(6, '0');
          dateFixer.insert(6, '2');
        }
        //if the day and month are correct, but the year is incorrect
      } else if (date.substring(5).length() == 2) {
        dateFixer.insert(6, '0');
        dateFixer.insert(6, '2');
      }
      date = dateFixer.toString();
    }
    System.out.println("Fixed Date: " + date);

    // insert into logs table
    PreparedStatement prep;
    try {
      prep = conn2.prepareStatement(
              "INSERT INTO logs VALUES (?, ?, ?);");

      // a row in the logs table goes user ID, food ID, date
      prep.setString(1, userID);
      prep.setString(2, foodId);
      prep.setString(3, date);

      prep.execute();
      prep.close();

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ERROR: insert failed");
      return -1;
    }

    return 0;
  }

  /**
   * Inserts a single log into the logs table.
   *
   * @param foodName name of Food
   * @param userId   userID
   * @return success or failure number
   * -1 - error: sql error :/
   * 1 - error: food isn't in the CustomFood nor USDA Food db
   * 0 - inserted successfully!
   **/
  public List<String> queryNutrition(String foodName, String userId) {

    boolean isCustomFood = false;

    // check if food is in the CustomFood or USDAFood db
    String foodId = queryCustomFood(foodName, userId);
    if (foodId == null) {
      foodId = queryDBFood(foodName);
      if (foodId == null) {
        System.out.println("User must create own food log");
        return null;
      }
    } else {
      isCustomFood = true;
    }

    PreparedStatement prep1;
    PreparedStatement prep2 = null;
    PreparedStatement prep3 = null;
    PreparedStatement prep4 = null;
    if (isCustomFood) {
      try {
        prep1 = conn2.prepareStatement("SELECT * "
                + " FROM CustomFoods WHERE "
                + "(CustomFoods.foodID == ?) AND (CustomFoods.userID == ?);");

        prep1.setString(1, foodId);
        prep1.setString(2, userId);

//          prep.addBatch();
//          prep.executeBatch();

      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("ERROR: insert failed");
        return null;
      }
    } else {
      try {

        prep1 = conn1.prepareStatement("SELECT amount "
                + " FROM nutrition WHERE food_id == ? AND nutrient_id = ?;");

        prep1.setInt(1, parseInt(foodId));
        prep1.setInt(2, proteinId);

        prep2 = conn1.prepareStatement("SELECT amount "
                + " FROM nutrition WHERE food_id == ? AND nutrient_id = ?;");

        prep2.setInt(1, parseInt(foodId));
        prep2.setInt(2, fatId);

        prep3 = conn1.prepareStatement("SELECT amount "
                + " FROM nutrition WHERE food_id == ? AND nutrient_id = ?;");

        prep3.setInt(1, parseInt(foodId));
        prep3.setInt(2, carbId);

        prep4 = conn1.prepareStatement("SELECT amount "
                + " FROM nutrition WHERE food_id == ? AND nutrient_id = ?;");

        prep4.setInt(1, parseInt(foodId));
        prep4.setInt(2, calId);


      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("ERROR: insert failed");
        return null;
      }
    }

    ResultSet rs;
    ResultSet rs1;
    ResultSet rs2;
    ResultSet rs3;
    ResultSet rs4;

    List<String> nutritionFacts = new ArrayList<>();
    if (isCustomFood) {
      try {
        rs = prep1.executeQuery();
        if (rs.next()) {
          String calories = rs.getString("calories");
          String protein = rs.getString("protein");
          String fats = rs.getString("fats");
          String carbs = rs.getString("carbs");
          nutritionFacts.add(protein);
          nutritionFacts.add(fats);
          nutritionFacts.add(carbs);
          nutritionFacts.add(calories);
          return nutritionFacts;
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("ERROR: executeQuery failed.");
        return null;
      }
    } else {
      try {
        rs1 = prep1.executeQuery();
        if (rs1.next()) {
          String protein = rs1.getString(1);
          nutritionFacts.add(protein);
          rs1.close();
          prep1.close();
        }
        rs2 = prep2.executeQuery();
        if (rs2.next()) {
          String fats = rs2.getString(1);
          nutritionFacts.add(fats);
          rs2.close();
          prep2.close();
        }
        rs3 = prep3.executeQuery();
        if (rs3.next()) {
          String carbs = rs3.getString(1);
          nutritionFacts.add(carbs);
          rs3.close();
          prep3.close();
        }
        rs4 = prep4.executeQuery();
        if (rs4.next()) {
          String calories = rs4.getString(1);
          nutritionFacts.add(calories);
          rs4.close();
          prep4.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("ERROR: executeQuery failed.");
        return null;
      }
    }
    return nutritionFacts;
  }

  /**
   * Method to retrieve the food groups that are off limits for a user.
   * @param id The id of the user who we want to find the off-limit groups for.
   * @return A list of the food groups that re off limits.
   * @throws SQLException If there is a problem executing the SQL query.
   */
  public List<Integer> getForbiddenGroups(int id) throws SQLException {
    List<Integer> forbidden = new ArrayList<>();
    PreparedStatement prep = conn2.prepareStatement(
            "SELECT forbidden FROM UserPreferences WHERE id = ?");
    prep.setInt(1, id);
    ResultSet rs = prep.executeQuery();
    String forbiddenString = null;
    while (rs.next()) {
      forbiddenString = rs.getString(1);
    }
    if (forbiddenString != null) {
      String[] splitForbiddenString = forbiddenString.split(",");
      for (int i = 0; i < splitForbiddenString.length; i++) {
        if (!splitForbiddenString[i].equals("")) {
          int forbiddenInt = parseInt(splitForbiddenString[i]);
          forbidden.add(forbiddenInt);
        }
      }
    }

    rs.close();
    prep.close();
    return forbidden;
  }

  public String queryFoodFromId(String foodId) {
    //custom foods first
    PreparedStatement prep;
    try {
      prep = conn2.prepareStatement("SELECT CustomFoods.foodName FROM CustomFoods WHERE foodId = ?");
      prep.setString(1, foodId);

      ResultSet rs = prep.executeQuery();
      if (rs.next()) {
        return rs.getString("foodName");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      prep = conn1.prepareStatement("SELECT food.long_desc FROM food WHERE id = ?");
      prep.setString(1, foodId);

      ResultSet rs = prep.executeQuery();
      if (rs.next()) {
        return rs.getString("long_desc");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
