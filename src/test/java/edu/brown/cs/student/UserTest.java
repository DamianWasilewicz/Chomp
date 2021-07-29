package edu.brown.cs.student.user;

import edu.brown.cs.student.food.Food;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A class which tests the User class.
 */
public class UserTest {

  private User Bobby;
  private DataProcessing data;

  /**
   * Sets up the User and the DataBase used for testing.
   */
  @Before
  public void setUp() {
    data = new DataProcessing();
    data.loadData();
    Bobby = new User(data, 1);
  }

  /**
   * Tears Down the MapDatabases used for testing.
   */
  @After
  public void tearDown() {
    data = null;
    Bobby = null;
  }

  /**
   * Tests the construction of the User
   */
  @Test
  public void testUserConstruction() {
    setUp();
    User Firmino = new User(null, 0);
    // Test Null Data case
    assertNull(Firmino.getUserData());

    // Test the ID of users
    assertEquals(Bobby.getUserId(), 1);
    assertEquals(Firmino.getUserId(), 0);

    // Test the size of the priority queue when no user foods have been added
    assertEquals(Bobby.getBreakfastQueue().size(), 0);

    //adds food to the queue before testing the validity of said queue
    Food banana = new Food("Banana", 4, 9, 1, 35);
    Food apple = new Food("Apple", 4, 9, 3, 35);
    Food orange = new Food("Orange", 4, 9, 6, 35);
    banana.setFoodGroupId(900);
    apple.setFoodGroupId(900);
    orange.setFoodGroupId(900);
    Bobby.addToDesignatedQueue(banana);
    Bobby.addToDesignatedQueue(apple);
    Bobby.addToDesignatedQueue(orange);

    //fruits should be added to all three queues
    assertEquals(Bobby.getBreakfastQueue().poll(), banana);
    assertEquals(Bobby.getBreakfastQueue().poll(), apple);
    assertEquals(Bobby.getBreakfastQueue().poll(), orange);

    assertEquals(Bobby.getLunchQueue().poll(), banana);
    assertEquals(Bobby.getLunchQueue().poll(), apple);
    assertEquals(Bobby.getLunchQueue().poll(), orange);

    assertEquals(Bobby.getDinnerQueue().poll(), banana);
    assertEquals(Bobby.getDinnerQueue().poll(), apple);
    assertEquals(Bobby.getDinnerQueue().poll(), orange);

    // Tests the priority queue when the food id has no match
    Bobby.getBreakfastQueue().clear();
    Bobby.getLunchQueue().clear();
    Bobby.getDinnerQueue().clear();
    banana.setFoodGroupId(9);
    apple.setFoodGroupId(9);
    orange.setFoodGroupId(9);
    Bobby.addToDesignatedQueue(banana);
    Bobby.addToDesignatedQueue(apple);
    Bobby.addToDesignatedQueue(orange);
    //nothing should have been added to any of the queues since
    //    the foods now do not match any of the food group ids
    assertEquals(Bobby.getBreakfastQueue().size(), 0);
    assertEquals(Bobby.getLunchQueue().size(), 0);
    assertEquals(Bobby.getDinnerQueue().size(), 0);

    //tests that a dairy food is just aded to breakfast
    Food milk = new Food("milk", 4, 9, 1, 35);
    milk.setFoodGroupId(100);
    Bobby.addToDesignatedQueue(milk);
    assertEquals(Bobby.getBreakfastQueue().poll(), milk);
    assertEquals(Bobby.getLunchQueue().poll(), null);
    assertEquals(Bobby.getDinnerQueue().poll(), null);

    //tests that a poultry food is just added to lunch and dinner
    Bobby.getBreakfastQueue().clear();
    Bobby.getLunchQueue().clear();
    Bobby.getDinnerQueue().clear();
    Food chicken = new Food("chicken", 4, 9, 1, 35);
    chicken.setFoodGroupId(500);
    Bobby.addToDesignatedQueue(chicken);
    assertEquals(Bobby.getBreakfastQueue().poll(), null);
    assertEquals(Bobby.getLunchQueue().poll(), chicken);
    assertEquals(Bobby.getDinnerQueue().poll(), chicken);

    tearDown();
  }
}
