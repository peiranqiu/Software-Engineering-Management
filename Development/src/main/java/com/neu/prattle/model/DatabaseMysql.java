package com.neu.prattle.model;

public class DatabaseMysql implements DatabaseAPI {
  protected DBUtils dbu;

  public DatabaseMysql() {
    String user = "mydb";
    String password = "CS5500team4";

    dbu = new DBUtils();
  }

  public void closeConnection() {
    dbu.closeConnection();

  }
}
