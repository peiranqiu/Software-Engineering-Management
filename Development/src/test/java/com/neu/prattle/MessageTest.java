package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import javax.websocket.EncodeException;

public class MessageTest {

  private Message message;

  @Before
  public void setup() {
    message = new Message();
    message.setFrom("peiran");
    message.setTo("rouni");
    message.setContent("Good Morning!");
    message.setFromID(123);
    message.setToID(456);
    message.setMessageID();
  }

  @Test
  public void testStoreMessage() throws IOException, EncodeException {
    message.storeMessage();
  }
}
