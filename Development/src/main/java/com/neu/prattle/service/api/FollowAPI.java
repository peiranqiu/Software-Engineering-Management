package com.neu.prattle.service.api;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.DBUtils;

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
  public boolean userFollowUser(int user1Id, int user2Id) {
    con = getConnection();
    String sql = "INSERT INTO User_follows_User (follower_id, followee_id) VALUES (?, ?)";
    executeFollow(sql, user1Id, user2Id, "Follow");
    return true;
  }

  /**
   * User1 unfollows user2.
   *
   * @param user1Id the follower's id
   * @param user1Id the follower's id
   * @return true if unfollow is successful
   */
  public boolean userUnfollowUser(int user1Id, int user2Id) {
    con = getConnection();
    String sql = "DELETE FROM User_follows_User WHERE follower_id = ? AND followee_id = ?";
    executeFollow(sql, user1Id, user2Id, "Unfollow");
    return true;
  }

  /**
   * Get the user's following user list.
   *
   * @param userId the given user id
   * @return the list of users that the user follows
   */
  public List<User> getFollowingUsers(int userId) {
    List<User> list = new ArrayList<>();
    Connection con = getConnection();
    String sql = "SELECT * FROM User_follows_User WHERE follower_id =?";
    getUserList(con, sql, list, userId, "followee_id");
    return list;
  }

  /**
   * Get the user's follower list.
   *
   * @param userId the given user
   * @return the user's follower list
   */
  public List<User> userGetFollowers(int userId) {
    List<User> list = new ArrayList<>();
    Connection con = getConnection();
    String sql = "SELECT * FROM User_follows_User WHERE followee_id =?";
    getUserList(con, sql, list, userId, "follower_id");
    return list;
  }

  /**
   * User follows group.
   *
   * @param userId  the user's id
   * @param groupId the group's id
   * @return true if follow is successful
   */
  public boolean userFollowGroup(int userId, int groupId) {
    con = getConnection();
    String sql = "INSERT INTO User_follows_Group (User_User_id, Group_Group_id) VALUES (?, ?)";
    executeFollow(sql, userId, groupId, "Follow");
    return true;
  }

  /**
   * User unfollows group.
   *
   * @param userId  the user's id
   * @param groupId the group's id
   * @return true if unfollow is successful
   */
  public boolean userUnfollowGroup(int userId, int groupId) {
    con = getConnection();
    String sql = "DELETE FROM User_follows_Group WHERE User_User_id = ? AND Group_Group_id = ?";
    executeFollow(sql, userId, groupId, "Unfollow");
    return true;
  }

  /**
   * Get list of groups that the user is following.
   *
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
   *
   * @param groupId the given group id
   * @return the group's follower list
   */
  public List<User> groupGetFollowers(int groupId) {
    List<User> list = new ArrayList<>();
    Connection con = getConnection();
    String sql = "SELECT * FROM User_follows_Group WHERE Group_Group_id =?";
    getUserList(con, sql, list, groupId, "User_User_id");
    return list;
  }

  /**
   * Helper method to execute follow/unfollow operation.
   *
   * @param sql the sql query string
   * @param id1 the first id to replace in the sql string
   * @param id2 the second id to replace in the sql string
   */
  public void executeFollow(String sql, int id1, int id2, String operation) {
    try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, id1);
      stmt.setInt(2, id2);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException(operation + " request failed.");
    }
  }

  /**
   * Helper method to get corresponding user list of user's followers, user's following list and
   * group's followers.
   */
  public boolean getUserList(Connection con, String sql, List<User> list, int id, String col) {
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt(col);
        User user = userAPI.getUserById(userId);
        list.add(user);
      }
      rs.close();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return true;
  }
}
