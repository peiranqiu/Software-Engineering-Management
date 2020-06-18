package com.neu.prattle.service.api;

import com.neu.prattle.model.Group;

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
 * GroupApI is a class that can connect this project with sql database for group entity.
 */
public class GroupAPI extends DBUtils {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private PreparedStatement stmt = null;
  private ResultSet rs = null;
  private ResultSet rs1=null;
  private PreparedStatement stmt1=null;

  public GroupAPI() {
    super();
  }

  /**
   * add group into database
   *
   * @param group adding group object.
   */
  public boolean addGroup(Group group) {
    try {
      super.insertTerm("mydb.Group", "name", group.getName());
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return true;
  }

  /**
   * check if group exists in the database.
   *
   * @param name group name
   * @return the group
   */
  public Group getGroup(String name) throws SQLException {
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM mydb.Group WHERE name =?";
      stmt = con.prepareStatement(sql);
      stmt.setString(1, name);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return constructGroup(rs);
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
   * check if group exists in the database.
   *
   * @param id group id
   * @return the group
   */
  public Group getGroupById(int id) throws SQLException {
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM mydb.Group WHERE Group_id = ?";
      stmt = con.prepareStatement(sql);
      stmt.setInt(1, id);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return constructGroup(rs);
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
   * a method to get sub groups of one group by group id
   *
   * @return a list of groups
   */

  public List<Group> getSubGroupList(int groupId) throws SQLException {
    List<Group> subGroupList = new ArrayList<>();
    try {
      Connection con = getConnection();
      String sql = "SELECT * FROM Group_has_Group WHERE super_Group_id =?";
      stmt1 = con.prepareStatement(sql);
      stmt1.setInt(1, groupId);
      rs1= stmt1.executeQuery();
      while (rs1.next()) {
        int subGroupId = rs1.getInt("sub_Group_id");
        subGroupList.add(getGroupById(subGroupId));
      }
      rs1.close();
      stmt1.close();

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    finally {
      rs1.close();
      stmt1.close();
    }


    return subGroupList;
  }


  /**
   * method to add subgroup into a group
   */
  public boolean addSubgroupIntoGroup(int groupId, int subGroupId) throws SQLException {
    try {
      Connection con = getConnection();
      String sql = "INSERT INTO Group_has_Group (super_Group_id, sub_Group_id) VALUES (?, ?)";
      stmt = con.prepareStatement(sql);
      stmt.setInt(1, groupId);
      stmt.setInt(2, subGroupId);
      stmt.executeUpdate();

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      stmt.close();
    }
    return true;
  }

  /**
   * method to delete a subgroup from a group
   */
  public void deleteSubgroupfromGroup(int groupId, int subGroupId) throws SQLException {
    try {
      con = getConnection();
      String sql = "DELETE FROM Group_has_Group WHERE super_Group_id = ? AND sub_Group_id = ?";
      stmt = con.prepareStatement(sql);
      stmt.setInt(1, groupId);
      stmt.setInt(2, subGroupId);
      stmt.executeUpdate();

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      stmt.close();
    }
  }

  /**
   * method to set password for a group so that it can be private group
   *
   * @param groupId  groupId
   * @param password password
   */
  public boolean setPasswordforGroup(int groupId, String password) throws SQLException {
    try {
      Connection con = getConnection();
      String sql = "UPDATE mydb.Group SET password = ? WHERE Group_id =?";
      stmt = con.prepareStatement(sql);
      stmt.setString(1, password);
      stmt.setInt(2, groupId);
      stmt.executeUpdate();

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      stmt.close();
    }
    return true;
  }


  /**
   * A helper method to construct a group object with returned result set.
   *
   * @param rs the result set
   * @return the group
   */
  public Group constructGroup(ResultSet rs) {
    Group group = new Group();
    try {
      group.setGroupId(rs.getInt("Group_id"));
      group.setName(rs.getString("name"));
      group.setPassword(rs.getString("password"));
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return group;
  }

  /**
   * delete a group from all group_has_group relationship.
   */
  public void deleteGroupFromGroupHasGroup(int groupId) throws SQLException {
    try {
      con = getConnection();
      String sql = "DELETE FROM Group_has_Group WHERE super_Group_id = ? OR sub_Group_id = ?";
      stmt = con.prepareStatement(sql);
      stmt.setInt(1, groupId);
      stmt.setInt(2, groupId);
      stmt.executeUpdate();

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      stmt.close();
    }
  }

  /**
   * delete a group object
   *
   * @param groupId id of the group object to be deleted
   */
  public boolean deleteGroup(int groupId) {
    boolean b;
    String sql = "DELETE FROM mydb.Group WHERE Group_id = ?";
    con = getConnection();
    try (PreparedStatement stmt2 = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt2.setInt(1, groupId);
      stmt2.executeUpdate();
      b = true;
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
      throw new IllegalStateException("Delete group failed.");
    }
    return b;
  }


  /**
   * a method to get all groups in the database
   *
   * @return a list of groups
   */

  public List<Group> getAllGroups() throws SQLException {
    List<Group> GroupList = new ArrayList<>();
    try {
      con = getConnection();
      String sql = "SELECT * FROM mydb.Group";
      stmt1 = con.prepareStatement(sql);
      rs1= stmt1.executeQuery();
      while (rs1.next()) {
        int subGroupId = rs1.getInt("Group_id");
        GroupList.add(getGroupById(subGroupId));
      }
      rs1.close();
      stmt1.close();

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    finally {
      rs1.close();
      stmt1.close();
    }

    return GroupList;
  }

}
