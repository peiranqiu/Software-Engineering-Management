package com.neu.prattle.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An abstract class to connect mysql database and offer general crud for
 * tables.
 */
public abstract class DBUtils {


  protected String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=EST5EDT";
  protected String user = "mydb";
  protected String password = "CS5500team4";
  protected Connection con = null;

  public DBUtils() {
    this.con = getConnection();
  }

  /**
   * Connect to the db
   * @return the connect
   */
  public Connection getConnection()
  {
    if (con == null) {
      try {
        con = DriverManager.getConnection(url, user, password);
        return con;
      } catch (SQLException e) {
        System.err.println(e.getMessage());
        System.exit(1);
      }
    }

    return con;
  }

  /**
   * Disconnect
   */
  public void closeConnection() {
    try {
      con.close();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * For a table of terms consisting of an id and string value pair, get the id of the term
   * adding a new term if it does not yet exist in the table
   * @param table The table of terms
   * @param term The term value
   * @return The id of the term
   */
  protected int insertTerm(String table, String valueColumn, String term)
  {
    int key = -1;

    try {
      Connection con = getConnection();
//      Statement stmt = con.createStatement();

      String sqlInsert = "INSERT INTO table (cols) VALUES (?)";
      sqlInsert = sqlInsert.replace("table", table);
      sqlInsert = sqlInsert.replace("cols", valueColumn);
      PreparedStatement stmt = con.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, term);

      stmt.executeUpdate();
//      stmt.executeUpdate(Statement.RETURN_GENERATED_KEYS);
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) key = rs.getInt(1);

      rs.close();
      stmt.close();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new IllegalStateException("sql update failed");
    }

    return key;

  }

}
