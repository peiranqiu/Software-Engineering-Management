package com.neu.prattle.service.api;

import com.neu.prattle.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class are api for crud in user table.
 */
public class UserAPI extends DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private PreparedStatement stmt = null;
  private ResultSet rs = null;

  /**
   * Get all users.
   */
  public List<User> getAllUsers() throws SQLException {
    List<User> allUsers = new ArrayList<>();
    String sql = "SELECT * FROM User";
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        User u = constructUser(rs);
        allUsers.add(u);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return allUsers;
  }

  /**
   * Add a user into the db
   *
   * @param user the user to add
   */
  public boolean addUser(User user) {

    boolean b = false;
    con = getConnection();
    String sqlInsert = "INSERT INTO User (name, password, isModerator) VALUES (?, ?, ?)";
    try (PreparedStatement sttmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
      sttmt.setString(1, user.getName());
      sttmt.setString(2, user.getPassword());
      sttmt.setBoolean(3, false);
      sttmt.executeUpdate();
      ResultSet result = sttmt.getGeneratedKeys();
      if (result.next()) {
        b = true;
      }
      result.close();
    } catch (SQLException e) {
      throw new IllegalStateException("Create user failed.");
    }
    return b;
  }

  /**
   * Fetch the user with the given name from db
   *
   * @param name the name of the user
   * @return if the user is in the db
   */
  public User getUserByName(String name) throws SQLException {
    String sql = "SELECT * FROM User WHERE name =?";
    return getUser(sql, -1, name);
  }

  /**
   * Fetch the user with the given id from db
   *
   * @param id the id of the user
   * @return if the user is in the db
   */
  public User getUserById(int id) throws SQLException {
    String sql = "SELECT * FROM User WHERE User_id =?";
    return getUser(sql, id, null);
  }

  /**
   * Helper method to get user with given name or id.
   *
   * @param sql  the sql query string
   * @param id   the user id
   * @param name the user name
   * @return the user if exist
   */
  public User getUser(String sql, int id, String name) throws SQLException {
    User u = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (name == null) {
        stmt.setInt(1, id);
      } else {
        stmt.setString(1, name);
      }
      rs = stmt.executeQuery();
      if (rs.next()) {
        u = constructUser(rs);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return u;
  }

  /**
   * A helper method to construct a user with returned result set.
   *
   * @param rs the result set
   * @return the user
   */
  public User constructUser(ResultSet rs) {
    User user = new User();
    try {
      user.setUserId(rs.getInt("User_id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
      user.setModerator(rs.getBoolean("isModerator"));
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return user;
  }

  /**
   * Update user info with given new fields.
   *
   * @param user  the user to update
   * @param field the field to update
   * @param value the new value of the given field
   * @return the updated user
   */
  public User updateUser(User user, String field, String value) throws SQLException {
    String sql = "UPDATE User SET field = ? WHERE name = ?";
    sql = sql.replace("field", field);
    return executeUpdate(sql, user, value, false);
  }

  /**
   * Update user role.
   *
   * @param user the user to update
   * @return the updated user
   */
  public User setModerator(User user) throws SQLException {
    String sql = "UPDATE User SET isModerator = ? WHERE name = ?";
    return executeUpdate(sql, user, null, user.getModerator());
  }

  /**
   * Update user watched by government
   *
   * @param userId the user id
   * @return updated user
   */
  public User setWatched(int userId) throws SQLException {
    String sql = "UPDATE User SET watched = ? WHERE name = ?";
    User user = getUserById(userId);
    return executeUpdate(sql, user, null, true);
  }

  /**
   * Helper method to update user information
   *
   * @param sql      the sql query string
   * @param user     the user to be updated
   * @param value    the string value to be update if exist
   * @param moderate the isModerator boolean value to be update if exist
   * @return the updated user
   */
  public User executeUpdate(String sql, User user, String value, boolean moderate) throws SQLException {
    User u = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (value == null) {
        stmt.setBoolean(1, moderate);
      } else {
        stmt.setString(1, value);
      }
      stmt.setString(2, user.getName());
      int result = stmt.executeUpdate();
      if (result == 1) {
        u = user;
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      stmt.close();
    }
    return u;
  }
}
