package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class are api for crud in user table.
 */
public class UserAPI extends DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private PreparedStatement stmt = null;
  private ResultSet rs = null;
  public UserAPI() {
    super();
  }

  /**
   * Add a user the db
   * @param user the user to add
   */
  public boolean addUser(User user) {

    try {
      super.insertTerm("User", "name", user.getName());
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }

    return true;
  }

  /**
   * Fetch the user with the given from db
   * @param name the name of the user
   * @return if the user is in the db
   */
  public User getUsers(String name) throws SQLException {

    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User WHERE name =?";
      stmt = con.prepareStatement(sql);
      stmt.setString(1,name);
      rs = stmt.executeQuery();
      if (rs.next()) {
        User user = new User();
        user.setUserId(rs.getInt("User_id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return null;
  }

  public User updateUser(User user, String field, String value) throws SQLException {
    try {
      Connection con = getConnection();

      String sql =
              "UPDATE User SET field = ? WHERE name = ?";
      sql = sql.replace("field", field);
      stmt = con.prepareStatement(sql);
      stmt.setString(1, value);
      stmt.setString(2, user.getName());

      int result = stmt.executeUpdate();
      if (result == 1) {
        return user;
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return null;
  }

}
