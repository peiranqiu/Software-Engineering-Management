package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class are api for crud in user table.
 */
public class UserAPI extends DBUtils {
  public UserAPI() {
    super();
  }

  /**
   * Add a user the db
   * @param user the user to add
   */
  public void addUser(User user) {
    super.insertTerm("User", "name", user.getName());
  }

  /**
   * Fetch the user with the given from db
   * @param name the name of the user
   * @return if the user is in the db
   */
  public boolean getUsers(String name) {
    try {
      Connection con = getConnection();
      Statement stmt = con.createStatement();

      String sql = "SELECT * FROM User WHERE name = '" + name + "';";

      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return true;
      }
      rs.close();
      stmt.close();
      return false;
    } catch (SQLException e) {
      return false;
    }

  }


}
