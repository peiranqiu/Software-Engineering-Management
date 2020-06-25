package com.neu.prattle.service.api;

import com.neu.prattle.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class are api for crud in message table.
 */
public class MessageAPI extends DBUtils {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private PreparedStatement stmt = null;
  private ResultSet rs = null;

  public MessageAPI() {
    super();
  }

  /**
   * Get all message object for private conversation.
   */
  public List<Message> getAllPrivateMessages(String fromName, String toName) throws SQLException {
    List<Message> allMessages = new ArrayList<>();
    String sql = "SELECT * FROM Message WHERE fromName IN (?,?) AND toName IN (?,?)";
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, fromName);
      stmt.setString(2, toName);
      stmt.setString(3, fromName);
      stmt.setString(4, toName);
      rs = stmt.executeQuery();
      while (rs.next()) {
        Message m = constructMessage(rs);
        allMessages.add(m);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return allMessages;
  }

  public List<Message> getAllGroupMessages(int groupId) throws SQLException {
    List<Message> allGroupMessages = new ArrayList<>();
    String sql = "SELECT * FROM Message WHERE groupId = ?";
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setInt(1, groupId);
      rs = stmt.executeQuery();
      while (rs.next()) {
        Message m = constructMessage(rs);
        allGroupMessages.add(m);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    } finally {
      rs.close();
      stmt.close();
    }
    return allGroupMessages;
  }


  public boolean addMessage(Message message) throws SQLException {
    try {
    Connection con = getConnection();
    String sql = "INSERT INTO Message (fromName, toName, messageText, messageDate, messageTimeStamp, sendToGroup, groupId) VALUES (?, ?, ?, ?, ?, ?,?)";
    stmt = con.prepareStatement(sql);
    stmt.setString(1, message.getFrom());
    stmt.setString(2, message.getTo());
    stmt.setString(3, message.getContent());
    stmt.setString(4, message.getMessageDate());
    stmt.setString(5, message.getTimeStamp());
    stmt.setBoolean(6, message.getSendToGroup());
    stmt.setInt(7, message.getGroupId());
    stmt.executeUpdate();

  } catch (SQLException e) {
    LOGGER.log(Level.INFO, e.getMessage());
  } finally {
    stmt.close();
  }
    return true;
}

  /**
   * A helper method to construct a Message with returned result set.
   *
   * @param rs the result set
   * @return the message
   */
  public Message constructMessage(ResultSet rs) {
    Message message = new Message();
    try {
      message.setFrom(rs.getString("fromName"));
      message.setTo(rs.getString("toName"));
      message.setContent(rs.getString("messageText"));
      message.setMessageDate(rs.getString("messageDate"));
      message.setTimeStamp(rs.getString("messageTimeStamp"));
      message.setSendToGroup(rs.getBoolean("sendToGroup"));
      message.setGroupId(rs.getInt("groupId"));
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return message;
  }

  /**
   * Get user chat log.
   * @param username user name
   * @return
   */
  public List<Message> getUserLog(String username) {
    List<Message> list = new ArrayList<>();
    String sql = "SELECT * FROM mydb.Message WHERE fromName = ?";
    con = getConnection();
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(constructMessage(rs));
      }
      rs.close();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return list;
  }
}