package edu.brown.cs.student.Math;

import edu.brown.cs.student.food.Food;

/**
 * Static class used for algorithms involved in calculating priorities.
 */
public class Algorithms {
  private static final int REWARD = 400;
//
//  public static void eloAlgoUpdate(Food winner, Food loserA, Food loserB){
//    double newWinnerScore = ((loserA.getPriority() + loserB.getPriority()) + 2*REWARD) / 2;
//    double newLoserAScore = ((winner.getPriority() + loserB.getPriority()) - REWARD) / 2;
//    double newLoserBScore = ((winner.getPriority() + loserA.getPriority()) - REWARD) / 2;
//    winner.setPriority(newWinnerScore);
//    loserA.setPriority(newLoserAScore);
//    loserB.setPriority(newLoserBScore);
//  }


  /**
   * Method to calculate the updated score for the winning food.
   * @param winner The winning food.
   * @param loserA One of the losing foods.
   * @param loserB The other losing food.
   * @return The new priority for the winning food.
   */
  public static double eloUpdateWinner(Food winner, Food loserA, Food loserB) {
    double newWinnerScore = ((loserA.getPriority() + loserB.getPriority()) + 2 * REWARD) / 2;
    return newWinnerScore;
  }

  /**
   * Method to calculate the updated score for one of the losing foods.
   * @param winner The winning food.
   * @param loserA One of the losing foods.
   * @param loserB The other losing food.
   * @return The new priority for one of the losing foods.
   */
  public static double eloUpdateLoserA(Food winner, Food loserA, Food loserB) {
    double newLoserAScore = ((winner.getPriority() + loserB.getPriority()) - REWARD) / 2;
    return newLoserAScore;
  }

  /**
   * Method to calculate the updated score for the other losing food.
   * @param winner The winning food.
   * @param loserA One of the losing foods.
   * @param loserB The other losing food.
   * @return The new priority for the other losing food.
   */
  public static double eloUpdateLoserB(Food winner, Food loserA, Food loserB) {
    double newLoserBScore = ((winner.getPriority() + loserA.getPriority()) - REWARD) / 2;
    return newLoserBScore;
  }
}
