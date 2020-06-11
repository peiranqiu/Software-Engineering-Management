package com.neu.prattle.service;

import com.neu.prattle.model.Group;
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
 * This class are api for crud in User_has_Group and User_moderates_Group table.
 */
public class ModerateAPI extends DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private UserAPI userAPI;
  private GroupAPI groupAPI;
  private ResultSet rs = null;

  /**
   * Construct a moderateAPI object.
   */
  public ModerateAPI() {
    super();
    userAPI = new UserAPI();
    groupAPI = new GroupAPI();
  }

  /**
   * Add a moderator to a group.
   * @param groupId the group id
   * @param userId the moderator id
   * @return true if add successful
   */
  public boolean addModerator(int groupId, int userId) {
    String sql = "INSERT INTO User_moderates_Group (User_User_id, Group_Group_id) VALUES (?, ?)";
    execute(sql, userId, groupId, "Add", "Moderator");
    return true;
  }

  /**
   * Set the group moderator to group member.
   * @param groupId the group id
   * @param userId the user id
   * @return true if downgrade is successful
   */
  public boolean deleteModerator(int groupId, int userId) {
    String sql = "DELETE FROM User_moderates_Group WHERE User_User_id = ? AND Group_Group_id = ?";
    execute(sql, userId, groupId, "Delete", "Moderator");
    return true;
  }

  /**
   * Add a member to a group.
   * @param groupId the group id
   * @param userId the member id
   * @return true if add successful
   */
  public boolean addMember(int groupId, int userId) {
    String sql = "INSERT INTO User_has_Group (User_User_id, Group_Group_id) VALUES (?, ?)";
    execute(sql, userId, groupId, "Add", "Member");
    return true;
  }

  /**
   * delete a group member.
   * @param groupId the group id
   * @param userId the user id
   * @return true if delete is successful
   */
  public boolean deleteMember(int groupId, int userId) {
    String sql = "DELETE FROM User_has_Group WHERE User_User_id = ? AND Group_Group_id = ?";
    execute(sql, userId, groupId, "Delete", "Member");
    return true;
  }

  /**
   * Get moderator list of the group.
   * @param groupId the group id
   * @return the list of moderators
   */
  public List<User> getModerators(int groupId) {
    List<User> list = new ArrayList<>();
    String sql = "SELECT * FROM User_moderates_Group WHERE Group_Group_id =?";
    getUserList(sql, list, groupId);
    return list;
  }

  /**
   * Get member list of the group.
   * @param groupId the group id
   * @return the list of members
   */
  public List<User> getMembers(int groupId) {
    List<User> list = new ArrayList<>();
    String sql = "SELECT * FROM User_has_Group WHERE Group_Group_id =?";
    getUserList(sql, list, groupId);
    return list;
  }

  /**
   * Get moderated group list of the user.
   * @param userId the user id
   * @return the group list that user is moderating
   */
  public List<Group> getModerateGroups(int userId) {
    List<Group> list = new ArrayList<>();
    String sql = "SELECT * FROM User_moderates_Group WHERE User_User_id =?";
    getGroupList(sql, list, userId);
    return list;
  }

  /**
   * Helper method to execute add/delete member/moderator operation.
   *
   * @param sql the sql query string
   * @param id1 the first id to replace in the sql string
   * @param id2 the second id to replace in the sql string
   * @param operation the add or delete operation
   * @param role the user's role for this group
   */
  public void execute(String sql, int id1, int id2, String operation, String role) {
    con = getConnection();
    try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, id1);
      stmt.setInt(2, id2);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException(operation + " " + role + " failed.");
    }
  }

  /**
   * Helper method to get corresponding moderator/member list of a group.
   * @param sql the sql query string
   * @param list the user list
   * @param id the group id
   */
  public void getUserList(String sql, List<User> list, int id) {
    con = getConnection();
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt("User_User_id");
        User user = userAPI.getUserById(userId);
        list.add(user);
      }
      rs.close();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  /**
   * Helper method to get corresponding group list of a user.
   * @param sql the sql query string
   * @param list the group list
   * @param id the user id
   */
  public void getGroupList(String sql, List<Group> list, int id) {
    con = getConnection();
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      while (rs.next()) {
        int groupId = rs.getInt("Group_Group_id");
        Group group = groupAPI.getGroupById(groupId);
        list.add(group);
      }
      rs.close();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }
}
