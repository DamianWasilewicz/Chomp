package edu.brown.cs.student.user;


import edu.brown.cs.student.Math.Algorithms;
import edu.brown.cs.student.food.Food;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
   * This class is responsible for creating the REPL
   * by entering an infinite while loop to accept user input.
   * It uses a hashmap to see if the command entered by the user
   * is valid.
   */
public class Repl {

    /**
     * Empty constructor for REPL.
     */
  public Repl() throws FileNotFoundException, SQLException, ClassNotFoundException {
    makeRepl();
  }

  /**
   * This method is responsible for creating the REPL. It does this by
   * entering an infinite while loop and constantly reading in user commands
   * before performing the input command if it is contained in the hashmap.
   * @throws FileNotFoundException when incorrect file path is entered
   * @throws SQLException when SQL query cannot be made
   * @throws ClassNotFoundException when class not found
   */
  public void makeRepl() throws
          FileNotFoundException, SQLException, ClassNotFoundException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String read = null;
      try {
        read = input.readLine();
      } catch (IOException e) {
        System.out.println("ERROR: Problem reading input");
      }
      if (read != null) {
        List<String> command = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(read);
        while (m.find()) {
          command.add(m.group(1));
        }
        if (command.get(0).equals("elo")) {
          System.out.println(eloUpdate(command.get(1), command.get(2), command.get(3)));
        }
      } else {
        break;
      }
    }
  }

  public double eloUpdate(String foodA, String foodB, String foodC) {
    Food food1 = new Food(foodA, 1, 0, 5.0, 0);
    Food food2 = new Food(foodB, 2, 1, 6.0, 0);
    Food food3 = new Food(foodC, 3, 2, 4.0, 0);
    Algorithms algorithm = new Algorithms();
    return algorithm.eloUpdateWinner(food1, food2, food3);
  }
}
