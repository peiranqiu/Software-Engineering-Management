package com.neu.prattle.service.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * An abstract class to connect mysql database and offer general crud for tables.
 */
public abstract class DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  protected Connection con = null;

  /**
   * Connect to the db
   *
   * @return the connect
   */
  public Connection getConnection() {
    if (con == null) {
      try {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/mydb");
        con = ds.getConnection();
      } catch (SQLException e) {
        LOGGER.log(Level.INFO, e.getMessage());
        System.exit(1);
      } catch (NamingException e) {
        LOGGER.log(Level.INFO, e.getMessage());
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
      LOGGER.log(Level.INFO, e.getMessage());
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
      LOGGER.log(Level.INFO, e.getMessage());
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
      LOGGER.log(Level.INFO, e.getMessage());
      throw new IllegalStateException("sql update failed");
    }
    return key;
  }
}
