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
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  public GroupAPI() {
    super();
  }

  /**
   * add group into database
   *
   * @param group adding group object.
   */
  public void addGroup(Group group) {
    try {
      super.insertTerm("mydb.Group", "name", group.getName());
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }


  }

  /**
   * check if group exists in the database.
   *
   * @param name group name
   * @return boolean
   */
  public boolean getGroup(String name) throws SQLException, NullPointerException {
    Boolean b = false;
    try {
      con = getConnection();
      String str = "SELECT * FROM mydb.Group WHERE name =?";
      try (PreparedStatement pstmt = getConnection().prepareStatement(str)) {
        pstmt.setString(1, name);
        try (ResultSet rs = pstmt.executeQuery()) {
          if (rs.next()) {
            b = true;
          }
          rs.close();
        }
        pstmt.close();
      } catch (SQLException e) {
        LOGGER.log(Level.INFO, e.getMessage());
      }
    } catch (NullPointerException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return b;
  }

}
