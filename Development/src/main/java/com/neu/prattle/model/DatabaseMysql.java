package com.neu.prattle.model;

public class DatabaseMysql implements DatabaseAPI {
  DBUtils dbu;
  public void authenticate(String user, String password) {
    dbu = new DBUtils("jdbc:mysql://localhost:3306/mydb?serverTimezone=EST5EDT",
            user, password);
  }

  public void closeConnection() {
    dbu.closeConnection();

  }
}
