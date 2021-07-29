//package edu.brown.cs.student.Authentication;
//
//import edu.brown.cs.student.Encryption.CipherHandler;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Temporary class created to test logging in an authentication.
// * Most of these should be easily carried over once we are using users from
// * our actual database.
// */
//public class TempDatabase {
//
//  private static Connection conn = null;
//  private CipherHandler cipher = new CipherHandler();
//  private String key = "shhhh";
//
//  public TempDatabase(String filename) {
//    try {
//      Class.forName("org.sqlite.JDBC");
//      String urlToDB = "jdbc:sqlite:" + filename;
//
//      // This is a check so we don't create files that don't exist
//      if (new File(filename).exists()) {
//        conn = DriverManager.getConnection(urlToDB);
//        PreparedStatement prep = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users ('id' INTEGER, 'username' TEXT,  'password' TEXT,  PRIMARY KEY ('id'))");
//        prep.executeUpdate();
//        prep.close();
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public int getUserFromDatabase(String username, String password) {
//    int output = -1;
//    System.out.println("username " + username);
//    System.out.println("password " + password);
//    try {
//      // SQL search
//      PreparedStatement prep = conn.prepareStatement("SELECT id, password from users WHERE username = ?");
//      prep.setString(1, username);
//      // String encrypted = Encryption.encrypt(password);
//      // prep.setString(2, encrypted);
//      // Gather data
//      ResultSet result = prep.executeQuery();
//
//      if (result.next()) {
//        if (cipher.decrypt(result.getString(2), key).equals(password)) {
//          output = result.getInt(1);
//        }
//      }
//      result.close();
//      prep.close();
//      return output;
//    } catch (Exception e) {
//      e.printStackTrace();
//      return output;
//    }
//
//  }
//
//  //
////  public List<String> getInterests(int id) {
////    int output = -1;
////    try {
////      // SQL search
////      PreparedStatement prep = conn
////        .prepareStatement("SELECT interest FROM interests WHERE user = ?");
////      prep.setInt(1, id);
////      // Gather data
////      ResultSet result = prep.executeQuery();
////      List<String> interests = new ArrayList<>();
////      while (result.next()) {
////        System.out.println(result.getString(1));
////        interests.add(result.getString(1));
////      }
////      result.close();
////      prep.close();
////      return interests;
////    } catch (Exception e) {
////      e.printStackTrace();
////      return new ArrayList<>();
////    }
////
////  }
////
//  public boolean usernameTaken(String username) {
//    try {
//      // SQL search
//      PreparedStatement prep = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
//      prep.setString(1, username);
//      // Gather data
//      ResultSet result = prep.executeQuery();
//      return result.next();
//    } catch (Exception e) {
//      e.printStackTrace();
//      return true;
//    }
//
//  }
//
//  public int addUser(String username, String password) {
//    try {
//      // SQL search
//      PreparedStatement prep = conn.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)");
//      prep.setString(1, username);
//      prep.setString(2, cipher.encrypt(password, key));
//      prep.executeUpdate();
//      prep.close();
//
//      //return user id, checking to make sure that user was created and added.
//      PreparedStatement prep2 = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
//      prep2.setString(1, username);
//      ResultSet result2 = prep2.executeQuery();
//      if (result2.next()) {
//        return result2.getInt(1);
//      }
//      return -1;
//    } catch (Exception e) {
//      e.printStackTrace();
//      return -1;
//    }
//
//  }
////
////  public void updatePassword(int id, String newpassword) {
////    try {
////      // SQL search
////      PreparedStatement prep = conn.prepareStatement("UPDATE login SET password = ? WHERE id = ?");
////      prep.setString(1, newpassword);
////      prep.setInt(2, id);
////
////      // prep.setSTring(2, Encryption.encrypt(password));
////      // Gather data
////      prep.executeUpdate();
////      prep.close();
////
////    } catch (Exception e) {
////      e.printStackTrace();
////    }
////  }
//
//}
//
