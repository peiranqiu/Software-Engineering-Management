package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.api.MessageAPI;

import org.junit.Test;

import java.sql.SQLException;


public class testMessageServiceSelf {
  @Test
  public void testaddMessage() throws SQLException {
    Message message = new Message();
    message.setFrom("Julia123");
    message.setTo("Alice123");
    message.setContent("How are you");
    message.setTimeStamp();
    message.setGroupId(-1);
    message.setDate();
    MessageAPI msgAPI = new MessageAPI();
    msgAPI.addMessage(message);
  }

  @Test
  public void testGetAllPrivateMessages() throws SQLException {
    MessageAPI msgAPI = new MessageAPI();
    msgAPI.getAllPrivateMessages("Julia123", "Alice123");
  }
}
