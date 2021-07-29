package edu.brown.cs.student.food;

import java.util.Map;

/**
 * Class that holds data related to foods.
 * (Note: add food data structures as they become relevant)
 */
public class Food {

  private String name;
  private int id;
  private int indexId;
  private int foodGroupId = -1;
  private Map<String, Double> nutrition;
  private double timeSinceLastSeen;
  //TO CONSIDER: Whether one food object is individual for every user
  // --it should be since the database is what is in charge of assigning specific priorities
  // --to each user's food preference while Java is only accounting for the current user
  private double priority;
  private double dateLastAccessed;

  /**
   * Constructor for a food object.
   * @param foodName the name of the food
   * @param foodId the id of the food item as given by sql
   * @param indId the indexId of the food as given by its order in the database
   * @param foodPriority the starting priority of the food
   * @param time A double representing the last time the food was seen.
   */
  public Food(String foodName, int foodId, int indId, double foodPriority, double time) {
    name = foodName;
    id = foodId;
    indexId = indId;
    priority = foodPriority;
    timeSinceLastSeen = time;
  }

  /**
   * Getter method for the food name.
   * @return the name of the food
   */
  public String getName() {
    return name;
  }

  /**
   * Getter method for the food priority.
   * @return the priority of the food
   */
  public double getPriority() {
    return priority;
  }

  /**
   * Setter method that sets the priority of the food.
   * @param p the new priority of the food
   */
  public void setPriority(double p) {
    priority = p;
  }

  /**
   * Getter for time since last seen.
   * @return The time that the food was last seen in the schedule.
   */
  public double getTimeSinceLastSeen() {
    return timeSinceLastSeen;
  }

  /**
   * Setter for time Since last seen.
   * @param time The new time since last seen for the given food.
   */
  public void setTimeLastSeen(double time) {
    timeSinceLastSeen = time;
  }

  /**
   * Method that indicates that this food has been seen.
   * Set to -1 because it will be incremented later,
   * which will set it to 0.
   */
  public void resetTimeLastSeen() {
    timeSinceLastSeen = -1;
  }
  /**
   * Getter method that gets the nutrition map for the food.
   * @return a hashmap of the food's nutritional information
   */
  public Map<String, Double> getNutrition() {
    return nutrition;
  }

  /**
   * Setter method that sets the nutrition facts for the food.
   * @param fullNutrition the hashamp of nutrition values to set the food to
   */
  public void setNutrition(Map<String, Double> fullNutrition) {
    nutrition = fullNutrition;
  }

  /**
   * Getter method that gets the food Id.
   * @return the id of the food.
   */
  public int getId() {
    return id;
  }

  /**
   * Getter method for the index Id.
   * @return the id of the ofood.
   */
  public int getIndexId() {
    return indexId;
  }

  /**
   * Getter method that gets the food group Id.
   * @return the food group id of the food
   */
  public int getFoodGroupId() {
    return foodGroupId;
  }

  /**
   * Setter method that sets the value of the food group Id.
   * @param newId the new value of the food group Id
   */
  public void setFoodGroupId(int newId) {
    foodGroupId = newId;
  }
}

