package edu.brown.cs.student.food;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * A class which tests the User class.
 */
public class FoodTest {

  private Food pamplemousse;

  /**
   * Sets up the User and the DataBase used for testing.
   */
  @Before
  public void setUp() {
    pamplemousse = new Food("grapefruit", 1, 2, 3, 4);
  }

  /**
   * Tears Down the MapDatabases used for testing.
   */
  @After
  public void tearDown() {
    pamplemousse= null;
  }

  /**
   * Tests the construction of the User
   */
  @Test
  public void testGetterandSetterMethods() {
    setUp();
    assertEquals(pamplemousse.getName(), "grapefruit");
    assertEquals(pamplemousse.getPriority(), 3.0, 0);
    pamplemousse.setPriority(4);
    assertEquals(pamplemousse.getPriority(), 4.0, 0);
    assertEquals(pamplemousse.getId(), 1);
    assertEquals(pamplemousse.getIndexId(), 2);
    assertEquals(pamplemousse.getTimeSinceLastSeen(), 4.0, 0);
    pamplemousse.setTimeLastSeen(5);
    assertEquals(pamplemousse.getTimeSinceLastSeen(), 5.0, 0);
    pamplemousse.resetTimeLastSeen();
    assertEquals(pamplemousse.getTimeSinceLastSeen(), -1, 0);
    assertEquals(pamplemousse.getFoodGroupId(), -1);
    pamplemousse.setFoodGroupId(3);
    assertEquals(pamplemousse.getFoodGroupId(), 3);
    assertNull(pamplemousse.getNutrition());
    HashMap<String, Double> newMap = new HashMap();
    pamplemousse.setNutrition(newMap);
    assertEquals(pamplemousse.getNutrition(), newMap);
    tearDown();
  }

  /**
   * Tests the comparison of foods
   */
  @Test
  public void testFoodComparison() {
    setUp();
    Food apple = new Food("apple", 3, 2, 5, 0);
    Food pomme = new Food("apple", 3, 2, 2, 0);
    Food manzanaIthink = new Food("apple", 3, 2, 3, 0);
    FoodComparator comparator = new FoodComparator();
    assertTrue(comparator.compare(pamplemousse, apple) < 0);
    assertTrue(comparator.compare(pamplemousse, pomme) > 0);
    assertEquals(comparator.compare(pamplemousse, manzanaIthink), 0);
    tearDown();
  }
}
