package com.neu.prattle.service;

import com.neu.prattle.model.DBUtils;
import com.neu.prattle.model.DatabaseMysql;
import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

public class UserAPI extends DBUtils {
  public UserAPI(){
    super();
  }

  public void addUser(User user){
    super.insertTerm("User", "name", user.getName());
  }

  public boolean getUsers(String name)  {
   boolean ret = false;
    try {
      Connection con = getConnection();
      Statement stmt = con.createStatement();

      String sql = "SELECT * FROM User WHERE name = '" + name +"';";

      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return true;
      }
      rs.close();
      stmt.close();
    }
    catch (SQLException e){
      return false;
    }
    return ret;
  }



}
