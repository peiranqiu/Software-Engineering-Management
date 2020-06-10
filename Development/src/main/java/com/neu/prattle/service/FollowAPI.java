package com.neu.prattle.service;

import com.neu.prattle.model.Group;
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

/**
 * This class are api for crud in User_follow_User and User_follow_Group table.
 */
public class FollowAPI extends DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private UserAPI userAPI;
  private GroupAPI groupAPI;
  private ResultSet rs = null;

  /**
   * Construct a followAPI object.
   */
  public FollowAPI() {
    super();
    userAPI = new UserAPI();
    groupAPI = new GroupAPI();
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
      int result = stmt.executeUpdate();
      if (result == 1) {
        key = 1;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
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
    con = getConnection();
    String sql = "DELETE FROM User_follows_User WHERE follower_id = ? AND followee_id = ?";
    executeUnfollow(sql, user1Id, user2Id);
    return true;
  }

  /**
   * Get the user's following user list.
   * @param userId the given user id
   * @return the list of users that the user follows
   */
  public List<User> getFollowingUsers(int userId) {
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

  /**
   * Get the user's follower list.
   * @param userId the given user
   * @return the user's follower list
   */
  public List<User> userGetFollowers(int userId) {
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

  /**
   * User follows group.
   *
   * @param userId the user's id
   * @param groupId the group's id
   * @return true if follow is successful
   */
  public boolean userFollowGroup(int userId, int groupId) {
    int key = -1;
    con = getConnection();
    String sqlInsert = "INSERT INTO User_follows_Group (User_User_id, Group_Group_id) VALUES (?, ?)";
    try (PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, groupId);
      int result = stmt.executeUpdate();
      if (result == 1) {
        key = 1;
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Follow request failed.");
    }
    return key != -1;
  }

  /**
   * User unfollows group.
   *
   * @param userId the user's id
   * @param groupId the group's id
   * @return true if unfollow is successful
   */
  public boolean userUnfollowGroup(int userId, int groupId) {
    con = getConnection();
    String sql = "DELETE FROM User_follows_Group WHERE User_User_id = ? AND Group_Group_id = ?";
    executeUnfollow(sql, userId, groupId);
    return true;
  }

  /**
   * Get list of groups that the user is following.
   * @param userId the given user id
   * @return list of groups that the user is following
   */
  public List<Group> getFollowingGroups(int userId) {
    List<Group> list = new ArrayList<>();
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User_follows_Group WHERE User_User_id =?";
      try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        rs = stmt.executeQuery();
        while (rs.next()) {
          int groupId = rs.getInt("Group_Group_id");
          Group group = groupAPI.getGroupById(groupId);
          list.add(group);
        }
        rs.close();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return list;
  }

  /**
   * Get the group's follower list.
   * @param groupId the given group id
   * @return the group's follower list
   */
  public List<User> groupGetFollowers(int groupId) {
    List<User> list = new ArrayList<>();
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM User_follows_Group WHERE Group_Group_id =?";
      try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, groupId);
        rs = stmt.executeQuery();
        while (rs.next()) {
          int userId = rs.getInt("User_User_id");
          User user = userAPI.getUserById(userId);
          list.add(user);
        }
        rs.close();
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return list;
  }

  /**
   * Helper class to execute unfollow operation.
   * @param sql the sql query string
   * @param id1 the first id to replace in the sql string
   * @param id2 the second id to replace in the sql string
   */
  public void executeUnfollow(String sql, int id1, int id2) {
    try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, id1);
      stmt.setInt(2, id2);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException("Unfollow request failed.");
    }
  }
}
