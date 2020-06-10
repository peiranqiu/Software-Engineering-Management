package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FollowAPI extends DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private UserAPI userAPI;
  private PreparedStatement stmt = null;
  private ResultSet rs = null;

  public FollowAPI() {
    super();
    userAPI = new UserAPI();
  }

  /**
   * User1 follow user2.
   *
   * @param user1Id the follower's id
   * @param user1Id the follower's id
   * @return true if follow is successful
   */
  public boolean UserFollowUser(int user1Id, int user2Id) {


    int key = -1;
    con = getConnection();

    String sqlInsert = "INSERT INTO User_follows_User (follower_id, followee_id) VALUES (?, ?)";
    try (PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, user1Id);
      stmt.setInt(2, user2Id);
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) key = rs.getInt(1);
      rs.close();
    } catch (SQLException e) {
      throw new IllegalStateException("Follow request failed.");
    }
    return key != -1;
  }

  /**
   * User1 unfollows user2.
   *
   * @param user1Id the follower's id
   * @param user1Id the follower's id
   * @return true if unfollow is successful
   */
  public boolean UserUnfollowUser(int user1Id, int user2Id) {
    int key = -1;
    con = getConnection();

    String sqlInsert = "DELETE FROM User_follows_User WHERE follower_id = ? AND followee_id = ?";
    try (PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, user1Id);
      stmt.setInt(2, user2Id);
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) key = rs.getInt(1);
      rs.close();
    } catch (SQLException e) {
      throw new IllegalStateException("Unfollow request failed.");
    }
    return key != -1;
  }

  public List<User> getFollowedUsers(int userId) {
    List<User> list = new ArrayList<>();
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User_follows_User WHERE follower_id =?";
      try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        rs = stmt.executeQuery();
        while (rs.next()) {
          int followeeId = rs.getInt("followee_id");
          User user = userAPI.getUserById(followeeId);
          list.add(user);
        }
        rs.close();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return list;
  }



  public List<User> getFollowers(int userId) {
    List<User> list = new ArrayList<>();
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User_follows_User WHERE followee_id =?";
      try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        rs = stmt.executeQuery();
        while (rs.next()) {
          int followerId = rs.getInt("follower_id");
          User user = userAPI.getUserById(followerId);
          list.add(user);
        }
        rs.close();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return list;
  }
}
