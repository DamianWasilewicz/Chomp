package edu.brown.cs.student.user;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import edu.brown.cs.student.food.Food;
import edu.brown.cs.student.Math.Algorithms;

public class AlgorithmsTest {
  Food apple;
  Food banana;
  Food mango;
  Food watermelon;
  Algorithms algorithm;

  @Before
  public void setUp() {
    apple = new Food("apple", 0, 1, 500.0, 0);
    banana = new Food("banana", 1, 2, 600.0, 0);
    mango = new Food("mango", 2, 3, 700.0, 0);
    watermelon = new Food("watermelon", 3, 4, 800.0, 0);
    algorithm = new Algorithms();
  }

  @Test
  public void testElo() {
    setUp();
    double appleWinner = algorithm.eloUpdateWinner(apple, banana, mango);
    double bananaLoser = algorithm.eloUpdateLoserA(mango, banana, apple);
    double watermelonLoser = algorithm.eloUpdateLoserB(banana, apple, watermelon);
    assertEquals(appleWinner, 1050.0, 0.5);
    assertEquals(bananaLoser, 400.0, 0.5);
    assertEquals(watermelonLoser, 350.0, 0.5);
    tearDown();
  }


  @After
  public void tearDown() {
    apple = null;
    banana = null;
    mango = null;
    watermelon = null;
    algorithm = null;
  }
}