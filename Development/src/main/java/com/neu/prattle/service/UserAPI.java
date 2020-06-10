package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
   *
   * @param user the user to add
   */
  public boolean addUser(User user) {

    int key = -1;
    con = getConnection();
    String sqlInsert = "INSERT INTO User (name, password) VALUES (?, ?)";
    try (PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, String.valueOf(user.getName()));
      stmt.setString(2, String.valueOf(user.getPassword()));
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) key = rs.getInt(1);
      rs.close();
    } catch (SQLException e) {
      throw new IllegalStateException("Create user failed.");
    }
    return key != -1;
  }

  /**
   * Fetch the user with the given name from db
   *
   * @param name the name of the user
   * @return if the user is in the db
   */
  public User getUserByName(String name) throws SQLException {

    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User WHERE name =?";
      stmt = con.prepareStatement(sql);
      stmt.setString(1, name);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return constructUser(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return null;
  }

  /**
   * Fetch the user with the given id from db
   *
   * @param id the id of the user
   * @return if the user is in the db
   */
  public User getUserById(int id) throws SQLException {

    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User WHERE User_id =?";
      stmt = con.prepareStatement(sql);
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return constructUser(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return null;
  }

  /**
   * Update user info with given new fields.
   * @param user the user to update
   * @param field the field to update
   * @param value the new value of the given field
   * @return the updated user
   * @throws SQLException
   */
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

  /**
   * A helper method to construct a user with returned result set.
   * @param rs the result set
   * @return the user
   */
  public User constructUser(ResultSet rs) {
    User user = new User();
    try {
      user.setUserId(rs.getInt("User_id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return user;
  }
}
