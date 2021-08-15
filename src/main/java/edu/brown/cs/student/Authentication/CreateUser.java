package edu.brown.cs.student.Authentication;

import edu.brown.cs.student.Encryption.CipherHandler;
import edu.brown.cs.student.user.DataProcessing;
import edu.brown.cs.student.user.Repl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class to set the current user session.
 */
public class CreateUser {
  private static Connection conn = null;
  private CipherHandler cipher = new CipherHandler();
  private String key = "shhhh";
  private DataProcessing data;
  private static final int NUMTABLES = 10;
  private static final int NUMCOLUMNS = 1023;
  private static final int NUMFOODS = 8789;
  private static final int INITIALPRIORITY = 1000;
  private static final int NUMCOLUMNS2 = 598;

  /**
   * Constructor for createUser class.
   * @param myData DataProcessing object that allows queries to the database.
   * @param filepath The path to the database.
   */
  public CreateUser(DataProcessing myData, String filepath) {
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + filepath;
      // This is a check so we don't create files that don't exist
      if (new File(filepath).exists()) {
        conn = DriverManager.getConnection(urlToDB);
        data = myData;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Method that returns the id value of the last added user,
   * used when decided what value the next user should be.
   * @return The integer id of the last user in the database
   */
  public int getLastID() {
    int lastID = -1;
    try {
      PreparedStatement prep = conn.prepareStatement(
              "SELECT * FROM users ORDER BY id DESC LIMIT 1");
      ResultSet rs = prep.executeQuery();
      if (rs.next()) {
        lastID = rs.getInt(1);
      }
      rs.close();
      prep.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Math.max(lastID, 1);
  }

  /**
   * Method will be used to create a new row for a new user in each of the priority tables,
   * initializing the value of each priority to 1.
   *
   * @param userID The integer id associated with a user (maintained in database).
   */
  public void populatePrioritiesInitially(int userID) {
    try {
      int foodCounter = 0;
      for (int i = 1; i < NUMTABLES; i++) {
        //prepares statement to populate priority queues
        String table = "priorities" + String.valueOf(i);
        String statement = "INSERT INTO " + table + "(id";
        String values = "VALUES (? ";
        for (int j = 0; (j <= NUMCOLUMNS) && (foodCounter < NUMFOODS); j++) {
          int id = foodCounter;
          statement += ", '" + id + "'";
          values += ", ?";
          foodCounter++;
        }
        values += ")";
        statement += ") " + values;
        PreparedStatement prep = conn.prepareStatement(statement);
        prep.setInt(1, userID);
        if (i != NUMTABLES - 1) {
          for (int k = 2; k <= NUMCOLUMNS + 2; k++) {
            prep.setInt(k, INITIALPRIORITY);
          }
        } else {
          for (int k = 2; k <= NUMCOLUMNS2; k++) {
            prep.setInt(k, INITIALPRIORITY);
          }
        }
        prep.executeUpdate();
        prep.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method will be used to create a new row for a new user in each of the priority tables,
   * initializing the value of each time last seen to 0.
   *
   * @param userID The integer id associated with a user (maintained in database).
   */
  public void populateTimeLastSeenInitially(int userID) {
    try {
      int foodCounter = 0;
      for (int i = 1; i < NUMTABLES; i++) {
        //prepares statement to populate priority queues
        String table = "date" + String.valueOf(i);
        String statement = "INSERT INTO " + table + "(id";
        String values = "VALUES (? ";
        for (int j = 0; (j <= NUMCOLUMNS) && (foodCounter < NUMFOODS); j++) {
          int id = foodCounter;
          statement += ", '" + id + "'";
          values += ", ?";
          foodCounter++;
        }
        values += ")";
        statement += ") " + values;
        PreparedStatement prep = conn.prepareStatement(statement);
        prep.setInt(1, userID);
        if (i != NUMTABLES - 1) {
          for (int k = 2; k <= NUMCOLUMNS + 2; k++) {
            prep.setInt(k, 0);
          }
        } else {
          for (int k = 2; k <= NUMCOLUMNS2; k++) {
            prep.setInt(k, 0);
          }
        }
        prep.executeUpdate();
        prep.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns a userId if a user exists with inputted username and password;
   * if not, returns -1.
   * @param username The String username of a user.
   * @param password The String password of a user.
   * @return The integer id of the user with the given username and password.
   */
  public int getUserFromDatabase(String username, String password) {
    int output = -1;
    System.out.println("username " + username);
    System.out.println("password " + password);
    try {
      // SQL search
      PreparedStatement prep = conn.prepareStatement(
              "SELECT id, password from users WHERE username = ?");
      prep.setString(1, username);
      // String encrypted = Encryption.encrypt(password);
      // prep.setString(2, encrypted);
      // Gather data
      ResultSet result = prep.executeQuery();

      if (result.next()) {
        if (Objects.equals(CipherHandler.decrypt(result.getString(2), key), password)) {
          output = result.getInt(1);
        }
      }
      result.close();
      prep.close();
      return output;
    } catch (Exception e) {
      e.printStackTrace();
      return output;
    }

  }

  /**
   * Returns true if a username has been taken/exists in the database.
   * @param username The String username inputted by a user.
   * @return A boolean that determines if a username is taken already or not.
   */
  public boolean usernameTaken(String username) {
    try {
      // SQL search
      PreparedStatement prep = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
      prep.setString(1, username);
      // Gather data
      ResultSet result = prep.executeQuery();
      boolean toReturn = result.next();
      result.close();
      prep.close();
      return toReturn;
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }

  }

  /**
   * Adds a user, with a new user id, to the database and initializes their priorities to 1.
   * @param username The String username of a user.
   * @param password The String password of a user.
   * @param forbidden A list of the forbidden food groups for a specific user.
   * @return The integer id of the new user.
   */
  public int addUser(String username, String password, List<String> forbidden) {
    try {
      // SQL search
      int newID = getLastID() + 1;
      PreparedStatement prep = conn.prepareStatement(
              "INSERT INTO users(id, username, password) VALUES (?, ?, ?)");
      prep.setInt(1, newID);
      prep.setString(2, username);
      prep.setString(3, CipherHandler.encrypt(password, key));
      prep.executeUpdate();
      prep.close();

      //return user id, checking to make sure that user was created and added.
      PreparedStatement prep2 = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
      prep2.setString(1, username);
      ResultSet result2 = prep2.executeQuery();

      if (result2.next()) {
        int userID = result2.getInt(1);
        populatePrioritiesInitially(userID);
        populateTimeLastSeenInitially(userID);
        result2.close();
        prep2.close();

        String forbiddenIds = convertForbiddensToIDs(forbidden);
        PreparedStatement prep3 = conn.prepareStatement(
                "INSERT INTO UserPreferences(id, forbidden) VALUES(?, ?)");
        prep3.setInt(1, newID);
        prep3.setString(2, forbiddenIds);
        prep3.executeUpdate();
        prep3.close();

        return userID;
      }
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  private String convertForbiddensToIDs(List<String> forbidden) {
    String ids = "";
    try {
      Map<String, Integer> nameToIdMap = data.createIdMapStringKey();
      for (String group : forbidden) {
        ids += nameToIdMap.get(group).toString() + ",";
      }
    } catch (Exception e) {
      System.out.println("Exception occured while converting forbidden");
    }
    return ids;
  }

}
