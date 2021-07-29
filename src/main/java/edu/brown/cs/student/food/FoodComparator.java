package edu.brown.cs.student.food;

import java.util.Comparator;

/**
 * Comparator class to compare two foods based on their priorities.
 */
public class FoodComparator implements Comparator<Food> {

  @Override
  public int compare(Food o1, Food o2) {
    return (int) (o1.getPriority() - o2.getPriority());
  }
}
