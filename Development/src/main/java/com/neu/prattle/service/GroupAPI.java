package com.neu.prattle.service;


import com.neu.prattle.model.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GroupApI is a class that can connect this project with sql database for group entity.
 */
public class GroupAPI extends DBUtils {
  private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private PreparedStatement pstmt = null;
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

    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return false;
  }


}
