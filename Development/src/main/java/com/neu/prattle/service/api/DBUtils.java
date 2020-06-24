package com.neu.prattle.service.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * An abstract class to connect mysql database and offer general crud for tables.
 */
public abstract class DBUtils {

  protected Connection con = null;
  private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  /**
   * Connect to the db
   *
   * @return the connect
   */
  public Connection getConnection() {
    if (con == null) {
      try {
        Class.forName("com.mysql.jdbc.Driver");
        String dbName = "mydb";
        String userName = "mydb";
        String pd = "CS5500team4";
        String hostname = "mydb.cd4ztimoe6ek.us-east-1.rds.amazonaws.com";
        String port = "3306";
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + pd;
        con = DriverManager.getConnection(jdbcUrl);
        logger.info("Remote connection successful.");
      } catch (SQLException e) {
        logger.info(e.getMessage());
      } catch (ClassNotFoundException e) {
        logger.info("Class not found error");
      }
    }
    return con;
  }

  /**
   * Disconnect
   */
  public boolean closeConnection() {
    try {
      con.close();
    } catch (SQLException e) {
      logger.info(e.getMessage());
    }
    return true;

  }

  /**
   * For a table of terms consisting of an id and string value pair, get the id of the term adding a
   * new term if it does not yet exist in the table
   *
   * @param table The table of terms
   * @param term  The term value
   * @return The id of the term
   */
  protected int insertTerm(String table, String valueColumn, String term) throws SQLException {
    int key = -1;

    try {
      con = getConnection();

      String sqlInsert = "INSERT INTO table (cols) VALUES (?)";
      sqlInsert = sqlInsert.replace("table", table);
      sqlInsert = sqlInsert.replace("cols", valueColumn);
      key = prepareStatement(con, sqlInsert, term);
    } catch (NullPointerException e) {
      logger.info(e.getMessage());
    }
    return key;

  }


  protected int prepareStatement(Connection con, String sqlInsert, String term) throws SQLException {
    int key = -1;
    try (PreparedStatement stmt = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, term);
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) key = rs.getInt(1);
      rs.close();
    } catch (SQLException e) {
      logger.info(e.getMessage());
      throw new IllegalStateException("sql update failed");
    }
    return key;
  }
}
