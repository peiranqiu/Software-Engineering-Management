package com.neu.prattle.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An abstract class to connect mysql database and offer general crud for tables.
 */
public abstract class DBUtils {

  private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  protected String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=EST5EDT";
  protected String user = "mydb";
  protected String pd = "CS5500team4";
  protected Connection con = null;

  public DBUtils() {
    this.con = getConnection();
  }

  /**
   * Connect to the db
   *
   * @return the connect
   */
  public Connection getConnection() {
    if (con == null) {
      try {
        con = DriverManager.getConnection(url, user, pd);
        return con;
      } catch (SQLException e) {
        LOGGER.log(Level.INFO, e.getMessage());
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
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  /**
   * For a table of terms consisting of an id and string value pair, get the id of the term adding a
   * new term if it does not yet exist in the table
   *
   * @param table The table of terms
   * @param term  The term value
   * @return The id of the term
   */
  protected int insertTerm(String table, String valueColumn, String term) throws SQLException, NullPointerException {
    int key = -1;

    try {
      Connection con = getConnection();

      String sqlInsert = "INSERT INTO table (cols) VALUES (?)";
      sqlInsert = sqlInsert.replace("table", table);
      sqlInsert = sqlInsert.replace("cols", valueColumn);
      try(PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, term);

        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) key = rs.getInt(1);

        rs.close();
        stmt.close();

      } catch (SQLException e) {

        throw new IllegalStateException("sql update failed");
      }


    } catch (NullPointerException e) {

      LOGGER.log(Level.INFO, e.getMessage());
    }
    return key;

  }
}
