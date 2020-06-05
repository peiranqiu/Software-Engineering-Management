package com.neu.prattle.service;

import com.neu.prattle.model.DBUtils;
import com.neu.prattle.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * GroupApI is a class that can connect this project with sql database for group entity.
 */
public class GroupAPI extends DBUtils {
  private PreparedStatement pstmt = null;
  private Connection conn = null;
  private ResultSet results = null;

  public GroupAPI() {
    super();
  }

  /**
   * add group into database
   * @param group adding group object.
   */
  public void addGroup(Group group) {
    super.insertTerm("mydb.Group", "name", group.getName());
  }

  /**
   * check if group exists in the database.
   * @param name group name
   * @return boolean
   */
  public boolean getGroup(String name) {
    Group group = null;
    try {
      con = getConnection();
      String str = "SELECT * FROM mydb.Group WHERE name =?";
      pstmt = getConnection().prepareStatement(str);
      pstmt.setString(1, name);
      results = pstmt.executeQuery();
      while (results.next()) {
        return true;
      }
      results.close();
      pstmt.close();
      return false;

    } catch (SQLException e) {
      return false;
    }
  }


}
