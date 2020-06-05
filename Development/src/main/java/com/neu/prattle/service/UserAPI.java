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
  public boolean addUser(User user) {

    super.insertTerm("User", "name", user.getName());
    return true;
  }

  /**
   * Fetch the user with the given from db
   * @param name the name of the user
   * @return if the user is in the db
   */
  public User getUsers(String name) {
    try {
      Connection con = getConnection();
      Statement stmt = con.createStatement();

      String sql = "SELECT * FROM User WHERE name = '" + name + "';";

      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        User user = new User();
        user.setUserId(rs.getInt("User_id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
      }
      rs.close();
      stmt.close();
      return null;
    } catch (SQLException e) {
      return null;
    }
  }

  public User updateUser(User user, String field, String value) {
    try {
      Connection con = getConnection();
      Statement stmt = con.createStatement();
      String sql = "UPDATE User SET " + field + "= '" + value + "' WHERE name = '" + user.getName() + "';";

      int result = stmt.executeUpdate(sql);
      if (result == 1) {
        stmt.close();
        return user;
      }
      return null;
    } catch (SQLException e) {
      return null;
    }
  }


}
