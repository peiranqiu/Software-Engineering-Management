package com.neu.prattle.service;

import com.neu.prattle.model.DBUtils;
import com.neu.prattle.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupAPI extends DBUtils {
  private PreparedStatement pstmt = null;
  private Connection conn = null;
  private ResultSet results = null;

  public GroupAPI() {
    super();
  }

  public void addGroup(Group group) {
    super.insertTerm("Group", "name", group.getName());
  }

  public Group getGroup(int id) {
    Group group = null;
    try {
      con = getConnection();
      String str = "SELECT * FROM Group WHERE Group_id =?";
      pstmt = getConnection().prepareStatement(str);
      pstmt.setInt(1, id);
      results = pstmt.executeQuery();
      while (results.next()) {
        group = new Group(results.getString("name"));
      }


    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        conn.close();
        pstmt.close();
        results.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return group;
  }

}
