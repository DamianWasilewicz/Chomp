package edu.brown.cs.student.user;

import edu.brown.cs.student.food.Food;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DataProcessingTest {

  private DataProcessing data;

  @Before
  public void setup() {
    data = new DataProcessing();
  }

  @Test
  public void testLoadData() {
    data.loadData();
    List<String> foods = data.getFoodNames();
    List<Integer> ids = data.getFoodID();
    assertEquals(foods.size(), 8789);
    assertEquals(ids.size(), 8789);
  }

  @Test
  public void testRetrievePriorities() throws ClassNotFoundException, SQLException {
    List<Double> priorities = data.retrievePriorities(22);
    assertEquals(priorities.size(), 8789);
  }

  @Test
  public void testTimeLastSeen() throws ClassNotFoundException, SQLException {
    List<Double> times = data.retrieveTimeLastSeen(22);
    assertEquals(times.size(), 8789);
  }

  @Test
  public void testNutrition() {
    Map<String, Double> nutrition = data.getNutrition(1001, "22");
    assertEquals(nutrition.get("Protein"), 0.85, 0.1);
    assertEquals(nutrition.get("Fat"), 81.11, 0.1);
    assertEquals(nutrition.get("Carbohydrates"), 0.06, 0.1);
    assertEquals(nutrition.get("Calories"), 717.0, 0.1);
  }

  @Test
  public void testIdMap() throws ClassNotFoundException, SQLException {
    Map<Integer, String> id = data.createIdMap();
    assertTrue(id.get(100).equals("Dairy and Egg Products"));
    assertTrue(id.get(900).equals("Fruits and Fruit Juices"));
    assertTrue(id.get(1900).equals("Sweets"));
  }

  @Test
  public void testIdMapStringKey() throws ClassNotFoundException, SQLException {
    Map<String, Integer> id = data.createIdMapStringKey();
    assertEquals(100, id.get("Dairy and Egg Products"), 0.1);
    assertEquals(900, id.get("Fruits and Fruit Juices"), 0.1);
    assertEquals(1900, id.get("Sweets"), 0.1);
  }

  @Test
  public void testRetrieveFoodGroupIds() throws ClassNotFoundException, SQLException {
    List<Integer> id = data.retrieveFoodGroupIds();
    assertEquals(id.size(), 8789);
  }

  @Test
  public void testQueryDBFood() {
    String id = data.queryDBFood("Butter, salted");
    assertTrue(id.equals("1001"));
  }

  @Test
  public void testQueryCustomFood() {
    String id = data.queryCustomFood("banana", "22");
    assertTrue(id.equals("9969.0"));
  }

  @Test
  public void testQueryFoodNameFromId() {
    String name = data.queryFoodNameFromId("9969.0", "22");
    assertTrue(name.equals("banana"));
  }

  @Test
  public void testQueryNutrition() {
    List<String> nutrition = data.queryNutrition("Butter, salted", "22");
    assertEquals(nutrition.size(), 4);
  }

  @Test
  public void testForbiddenGroups() throws SQLException {
    List<Integer> forbidden = data.getForbiddenGroups(22);
    assertEquals(forbidden.size(), 1);
  }

  @Test
  public void testQueryFoodFromId() {
    String food = data.queryFoodFromId("1001");
    assertTrue(food.equals("Butter, salted"));
  }

  @After
  public void tearDown() {
    data = null;
  }
}