package com.neu.prattle.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {


  protected String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=EST5EDT";
  protected String user = "mydb";
  protected String password ="CS5500team4";
  protected Connection con = null;

  public DBUtils() {
    this.con = getConnection();
  }

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

  public void closeConnection() {
    try {
      con.close();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }


//  public int insertOneRecord(String insertSQL)
//  {
//    // System.out.println("INSERT STATEMENT: "+insertSQL);
//    int key = -1;
//    try {
//
//      // get connection and initialize statement
//      Connection con = getConnection();
//      Statement stmt = con.createStatement();
//
//      stmt.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);
//
//      // extract auto-incremented ID
//      ResultSet rs = stmt.getGeneratedKeys();
//      if (rs.next()) key = rs.getInt(1);
//
//      // Cleanup
//      rs.close();
//      stmt.close();
//
//    } catch (SQLException e) {
//      System.err.println("ERROR: Could not insert record: "+insertSQL);
//      System.err.println(e.getMessage());
//      e.printStackTrace();
//    }
//    return key;
//  }


  /**
   * For a table of terms consisting of an id and string value pair, get the id of the term
   * adding a new term if it does not yet exist in the table
   * @param table The table of terms
   * @param term The term value
   * @return The id of the term
   */
  public int insertTerm(String table, String valueColumn, String term)
  {
    int key = -1;

    try {
      Connection con = getConnection();
      Statement stmt = con.createStatement();

        String sqlInsert = "INSERT INTO "+table+" ("+valueColumn+") VALUES " +
                "('"+term +"')";
        stmt.executeUpdate(sqlInsert,
              Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) key = rs.getInt(1);

      rs.close();
      stmt.close();

    } catch (SQLException e) {
      throw new IllegalStateException("sql update failed");
    }

    return key;

  }

}
