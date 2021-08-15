package edu.brown.cs.student.user;

import edu.brown.cs.student.Math.Algorithms;
import edu.brown.cs.student.food.Food;
import edu.brown.cs.student.food.FoodComparator;
import org.json.JSONArray;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Class that holds user relevant data.
 */
public class User {

  private PriorityQueue<Food> breakfastPrioritiesQueue;
  private PriorityQueue<Food> lunchPrioritiesQueue;
  private PriorityQueue<Food> dinnerPrioritiesQueue;
  private List<Double> timeSinceLastSeenList;
  private DataProcessing data;
  private int userId;
  private List<Food> foodList;
  private List<Integer> forbiddenGroups;
  private HashMap<String, Food> foodMap;
  private Map<Integer, String> foodGroupIdMap;
  private Map<String, Integer> foodGroupIdMapStringKey;
  private final int numDays = 7;
  private final int numMeals = 3;
  private final int timeMultiplier = 100;

  /**
   * Constructor for the user.
   *
   * @param myData the current data table
   * @param userID the id of the user
   */
  public User(DataProcessing myData, int userID) {
    if (myData == null) {
      System.out.println("ERROR: User was not created because a database wasn't passed in");
      return;
    }
    data = myData;
    userId = userID;
    breakfastPrioritiesQueue = new PriorityQueue<>(1, new FoodComparator());
    lunchPrioritiesQueue = new PriorityQueue<>(1, new FoodComparator());
    dinnerPrioritiesQueue = new PriorityQueue<>(1, new FoodComparator());
    foodList = new ArrayList<>();
    foodMap = new HashMap<>();
    foodGroupIdMap = null;
    try {
      foodGroupIdMap = data.createIdMap();
      foodGroupIdMapStringKey = data.createIdMapStringKey();
      timeSinceLastSeenList = data.retrieveTimeLastSeen(userID);
      forbiddenGroups = data.getForbiddenGroups(userId);
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Issue communicating with the database");
    } catch (SQLException throwables) {
      System.out.println("ERROR: Issue communicating with the database");
    }
    buildQueue();
    System.out.println("User with id: " + userId + "has been created");
  }

  /**
   * Accessor for userID.
   * @return The integer id of the user.
   */
  public int getID() {
    return userId;
  }

  /**
   * This method  will build the PriorityQueue from scratch from the database.
   * and will happen when first constructing the User object.,
   */
  public void buildQueue() {
    if (data == null) {
      System.out.println("ERROR: User is not connected to a valid database");
      return;
    }
    try {
      //checks the validity of the returned priorities from the data class
      if (data.retrievePriorities(userId) == null || data.retrievePriorities(userId).size() == 0) {
        System.out.println("ERROR: List of priorities is empty or not defined");
        return;
      }
    } catch (ClassNotFoundException e) {
      System.out.println("Exception occurred while retrieving priorities");
      e.printStackTrace();
      return;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("Exception occurred while retrieving priorities");
      return;
    }
    List<Double> prioritiesList;
    try {
      prioritiesList = data.retrievePriorities(userId);
    } catch (ClassNotFoundException e) {
      System.out.println("Exception occurred while retrieving priorities");
      e.printStackTrace();
      return;
    } catch (SQLException throwables) {
      System.out.println("Exception occurred while retrieving prirotites");
      throwables.printStackTrace();
      return;
    }
    createFoodList(prioritiesList);
    createFoodMap(foodList);
    for (Food f : foodList) {
      addToDesignatedQueue(f);
    }
  }

  /**
   * Method that takes  a food and adds it to its respective queue based on the food group id.
   *
   * @param f the food to be added
   */
  public void addToDesignatedQueue(Food f) {
    if (f == null) {
      System.out.println("ERROR: Food not found");
      return;
    }
    if (foodGroupIdMap == null) {
      System.out.println("ERROR: Unable to find the food group ids");
      return;
    }
    if (f.getFoodGroupId() == -1) {
      System.out.println("ERROR: The group id of the food has not been found.");
      return;
    }
//      String group = "9";
//      System.out.println("foodGroupIdMap value: " + foodGroupIdMap);
    String group = foodGroupIdMap.get(f.getFoodGroupId());
    if (group == null) {
      System.out.println("ERROR: The food's food group id did not match to a food group");
      return;
    }
    switch (group) {
      case "Dairy and Egg Products":
      case "Baked Products":
      case "Breakfast Cereals":
        breakfastPrioritiesQueue.add(f);
        break;
      case "Poultry Products":
      case "Soups,Sauces,and Gravies":
      case "Sausages and Luncheon Meats":
      case "Pork Products":
      case "Vegetables and Vegetable Products":
      case "Legumes and Legume Products":
      case "Beef Products":
      case "Finfish and Shellfish Products":
      case "Lamb,Veal,and Game Products":
      case "Fast Foods":
      case "Meals,Entrees,and Side Dishes":
      case "American Indian/ Alaska Native Foods":
        lunchPrioritiesQueue.add(f);
        dinnerPrioritiesQueue.add(f);
        break;
      case "Fruits and Fruit Juices":
      case "Nut and Seed Products":
      case "Beverages":
      case "Sweets":
      case "Cereal Grains and Pasta":
      case "Snacks":
      case "Restaurant Foods":
        breakfastPrioritiesQueue.add(f);
        lunchPrioritiesQueue.add(f);
        dinnerPrioritiesQueue.add(f);
        break;
      default:
        break;
    }
  }

  /**
   * Creates a list of foods and their priorities as specified for this user.
   *
   * @param prioritiesList the indexed list of foodpriorities
   */
  public void createFoodList(List<Double> prioritiesList) {
    if (data == null) {
      System.out.println("ERROR: User is not connected to a valid database");
      return;
    }
    if (prioritiesList == null || prioritiesList.size() == 0) {
      System.out.println("ERROR: List of priorities is empty or not defined");
      return;
    }

    List<Integer> groupIdList = null;
    try {
      groupIdList = data.retrieveFoodGroupIds();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return;
    }

    if (groupIdList == null) {
      System.out.println("ERROR: Could not find the food group ids.");
      return;
    }
    List<String> foodNames = data.getFoodNames();
    List<Integer> foodIds = data.getFoodID();


    for (int i = 0; i < foodNames.size(); i++) {
      Food food = new Food(foodNames.get(i),
              foodIds.get(i), i, prioritiesList.get(i), timeSinceLastSeenList.get(i));
      //sets the nutritional information of the food.
      food.setFoodGroupId(groupIdList.get(i));
      try {
        foodList.add(food);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Creates a hashMap of food that maps from the name of the food to the food object.
   *
   * @param foods a list of foods to convert to a hashmap
   */
  public void createFoodMap(List<Food> foods) {
    if (foods == null || foods.size() == 0) {
      return;
    }
    for (Food f : foods) {
      foodMap.put(f.getName(), f);
    }
  }

  /**
   * Getter method for the userId.
   *
   * @return the userId
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Getter method for the connected database.
   *
   * @return the database the user is currently connected to
   */
  DataProcessing getUserData() {
    if (data == null) {
      System.out.println("ERROR: User is not connected to a valid database");
    }
    return data;
  }

  /**
   * This method will updated the priority of a food by
   * 1) Checking which priorityQueue the food is a member of
   * 2) Removing the food from that queue
   * 3) Updating the priority of that food
   * 4) Returning that food to its priorityQueue
   * If the food is not a member if any priorityQueue, nothing occurs.
   * A food can be a member for multiple queues and will update on all relevant ones.
   *
   * @param foodName the name of the food to update
   * @param priority the new priority of the food
   */
  public void updatePriority(String foodName, double priority) {
    Food f = foodMap.get(foodName);
    //if the foodName is not specified, f is null
    if (f == null) {
      System.out.println("ERROR: Food name not found");
      return;
    }
    //checks in which queue the food is stored and makes the appropriate priority update
    if (breakfastPrioritiesQueue.contains(f)) {
      //first, removes the food from the priority queue
      breakfastPrioritiesQueue.remove(f);
      //then, updates the priority of the food
      f.setPriority(priority);
      //finally, returns the food to the priority queue
      breakfastPrioritiesQueue.add(f);
    }
    if (lunchPrioritiesQueue.contains(f)) {
      lunchPrioritiesQueue.remove(f);
      f.setPriority(priority);
      lunchPrioritiesQueue.add(f);
    }
    if (dinnerPrioritiesQueue.contains(f)) {
      dinnerPrioritiesQueue.remove(f);
      f.setPriority(priority);
      dinnerPrioritiesQueue.add(f);
    }
  }

  /**
   * Returns a List of Days, each with a List of Meals, each with a List of 3 foods -
   * this represents the next schedule's worth of foods for a particular user.
   *
   * @return A list of lists of lists that contains the food schedule.
   */
  public List<List<List<String>>> getSchedule() {
    List<List<List<String>>> schedule = new ArrayList<>();
    if (breakfastPrioritiesQueue == null || lunchPrioritiesQueue == null
            || dinnerPrioritiesQueue == null) {
      return null;
    }
    System.out.println("HERE1");

    initiliazeScheduleList(schedule);

    //Iterate three times - this is to break up priorities so as to not bunch up all highest
    //priority foods into the first day
    for (int i = 0; i < 3; i++) {
      //Iterate through each day
      for (int j = 0; j < numDays; j++) {
        //Iterate through each meal
        for (int k = 0; k < numMeals; k++) {
          switch (k) {
            case 0:
              System.out.println("HERe2");
              List<Food> bfList = new ArrayList<>(breakfastPrioritiesQueue);
//              Food bfFood = breakfastPrioritiesQueue.remove();
              int dupCounterBfast = i + j;
              Food bfFood = bfList.get(dupCounterBfast);
              while (!checkValidity(bfFood, schedule, k)) {
                dupCounterBfast++;
                bfFood = bfList.get(dupCounterBfast);
              }
              System.out.println("Bfast: " + dupCounterBfast);
              schedule.get(j).get(k).add(bfFood.getName());
//              breakfastPrioritiesQueue.add(bfFood);
              break;
            case 1:
              List<Food> luList = new ArrayList<>(lunchPrioritiesQueue);
//              Food luFood = lunchPrioritiesQueue.remove();
              int dupCounterLunch = i + j;
              Food luFood = luList.get(dupCounterLunch);
              while (!checkValidity(luFood, schedule, k)) {
                dupCounterLunch++;
                luFood = luList.get(dupCounterLunch);
              }
              System.out.println("Lunch: " + dupCounterLunch);
              schedule.get(j).get(k).add(luFood.getName());
//              lunchPrioritiesQueue.add(luFood);
              break;
            case 2:
              List<Food> diList = new ArrayList<>(dinnerPrioritiesQueue);
//              Food diFood = dinnerPrioritiesQueue.remove();
              int dupCounterDin = i + j;
              Food diFood = diList.get(dupCounterDin);
              while (!checkValidity(diFood, schedule, k)) {
                dupCounterDin++;
                diFood = diList.get(dupCounterDin);
              }
              System.out.println("Dinner: " + dupCounterDin);
              schedule.get(j).get(k).add(diFood.getName());
//              dinnerPrioritiesQueue.add(diFood);
              break;
            default:
              break;
          }
        }
      }
    }
    System.out.println("HERER3");
    return schedule;
  }

  //initialize schedule with structure of list of days of weeks, each with list of
  //meals, each with list of food items
  private void initiliazeScheduleList(List<List<List<String>>> schedule) {
    //create list of strings for each day's meals
    List<String> sundayBreakFast = new ArrayList<>();
    List<String> mondayBreakFast = new ArrayList<>();
    List<String> tuesdayBreakFast = new ArrayList<>();
    List<String> wednesdayBreakFast = new ArrayList<>();
    List<String> thursdayBreakFast = new ArrayList<>();
    List<String> fridayBreakFast = new ArrayList<>();
    List<String> saturdayBreakFast = new ArrayList<>();
    List<String> sundayLunch = new ArrayList<>();
    List<String> mondayLunch = new ArrayList<>();
    List<String> tuesdayLunch = new ArrayList<>();
    List<String> wednesdayLunch = new ArrayList<>();
    List<String> thursdayLunch = new ArrayList<>();
    List<String> fridayLunch = new ArrayList<>();
    List<String> saturdayLunch = new ArrayList<>();
    List<String> sundayDinner = new ArrayList<>();
    List<String> mondayDinner = new ArrayList<>();
    List<String> tuesdayDinner = new ArrayList<>();
    List<String> wednesdayDinner = new ArrayList<>();
    List<String> thursdayDinner = new ArrayList<>();
    List<String> fridayDinner = new ArrayList<>();
    List<String> saturdayDinner = new ArrayList<>();

    //create list of lists for each day
    List<List<String>> sunday = new ArrayList<>();
    List<List<String>> monday = new ArrayList<>();
    List<List<String>> tuesday = new ArrayList<>();
    List<List<String>> wednesday = new ArrayList<>();
    List<List<String>> thursday = new ArrayList<>();
    List<List<String>> friday = new ArrayList<>();
    List<List<String>> saturday = new ArrayList<>();

    //populate days with lists of their meals
    sunday.add(sundayBreakFast);
    sunday.add(sundayLunch);
    sunday.add(sundayDinner);
    monday.add(mondayBreakFast);
    monday.add(mondayLunch);
    monday.add(mondayDinner);
    tuesday.add(tuesdayBreakFast);
    tuesday.add(tuesdayLunch);
    tuesday.add(tuesdayDinner);
    wednesday.add(wednesdayBreakFast);
    wednesday.add(wednesdayLunch);
    wednesday.add(wednesdayDinner);
    thursday.add(thursdayBreakFast);
    thursday.add(thursdayLunch);
    thursday.add(thursdayDinner);
    friday.add(fridayBreakFast);
    friday.add(fridayLunch);
    friday.add(fridayDinner);
    saturday.add(saturdayBreakFast);
    saturday.add(saturdayLunch);
    saturday.add(saturdayDinner);

    //populate schedule with days
    schedule.add(sunday);
    schedule.add(monday);
    schedule.add(tuesday);
    schedule.add(wednesday);
    schedule.add(thursday);
    schedule.add(friday);
    schedule.add(saturday);
  }

  /**
   * Getter method for foods list containing all foods.
   * @return List of all foods in database.
   */
  public List<Food> getFoodList() {
    return foodList;
  }

  /**
   * Iterates through the outcomes of clicks from schedule, updates each
   * food's priorities using outcomes.
   *
   * @param arr The JSON Array used to update priorities.
   */
  public void updatePrioritiesFromSchedule(JSONArray arr) {
    try {
      for (int i = 0; i < numDays; i++) {
        JSONArray day = arr.getJSONArray(i);
        for (int j = 0; j < numMeals; j++) {
          JSONArray meal = day.getJSONArray(j);
          updateMealPriorities(meal);
        }
      }
      updateTimeLastSeen();
    } catch (Exception e) {
      System.out.println("Encountered exception when updating schedule priorities");
      return;
    }
  }

  /**
   * Helper method used in updatepriotiiesfromschedule to handle updating a particular meal.
   *
   * @param meal
   */
  private void updateMealPriorities(JSONArray meal) {
    try {
      if (!(meal.getString(0).equals("") || meal.getString(1).equals("")
              || meal.getString(2).equals(""))) {
        String winner = meal.getString(0);
        Food winnerF = foodMap.get(winner);
        winnerF.resetTimeLastSeen();
        String loserA = meal.getString(1);
        Food loserAF = foodMap.get(loserA);
        String loserB = meal.getString(2);
        Food loserBF = foodMap.get(loserB);
        updatePriority(winnerF.getName(), Algorithms.eloUpdateWinner(winnerF, loserAF, loserBF));
        updatePriority(loserAF.getName(), Algorithms.eloUpdateLoserA(winnerF, loserAF, loserBF));
        updatePriority(loserBF.getName(), Algorithms.eloUpdateLoserB(winnerF, loserAF, loserBF));
      }
    } catch (Exception e) {
      System.out.println("Encountered exception when updating meal priorities");
      return;
    }
  }

  /**
   * Method that goes through and increases a food's timelast seen counter
   * and it's priority as a result of this.
   */
  private void updateTimeLastSeen() {
    for (Food food : foodList) {
      food.setTimeLastSeen(food.getTimeSinceLastSeen() + 1);
      double newPriority = food.getPriority() + (timeMultiplier * food.getTimeSinceLastSeen());
      food.setPriority(newPriority);
    }

  }

  /**
   * Setter method for the forbidden food groups of a user.
   * @param forbidden The list that forbiddenGroups will be set to.
   */
  public void setForbiddenGroups(List<Integer> forbidden) {
    forbiddenGroups = forbidden;
  }

  /**
   * Getter method for the forbidden groups of a user.
   * @return The list of forbidden groups.
   */
  public List<Integer> getForbiddenGroups() {
    return forbiddenGroups;
  }

  /**
   * Method to check the validity of a food that could potentially be added to the
   * schedule. Checks if the food is already in the schedule and if it is a part of any
   * forbidden food groups.
   * @param food The food in question.
   * @param schedule The current schedule.
   * @param meal The meal (breakfast, lunch, dinner) that we would add the food to.
   * @return True if the food is valid. False, otherwise.
   */
  private boolean checkValidity(Food food, List<List<List<String>>> schedule, int meal) {
    for (int i = 0; i < numDays; i++) {
      if (schedule.get(i).get(meal).contains(food.getName())) {
        return false;
      }
    }
    if (forbiddenGroups.contains(food.getFoodGroupId())) {
      return false;
    }
    return true;
  }

  /**
   * Getter method for the breakfast queue used primarily for testing.
   * @return the breakdast queue
   */
  public PriorityQueue getBreakfastQueue() {
    return breakfastPrioritiesQueue;
  }

  /**
   * Getter method for the lunch queue used primarily for testing.
   * @return the lunch queue
   */
  public PriorityQueue getLunchQueue() {
    return lunchPrioritiesQueue;
  }

  /**
   * Getter method for the dinner queue used primarily for testing.
   * @return the dinner queue
   */
  public PriorityQueue getDinnerQueue() {
    return dinnerPrioritiesQueue;
  }

}
