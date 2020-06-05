package com.neu.prattle.model;

public interface DatabaseAPI {
  /**
   * Set connection settings
   * @param url
   * @param user
   * @param password
   */
  public void authenticate(String user, String password);

  /**
   * Close the connection when application finishes
   */
  public void closeConnection();

  }
