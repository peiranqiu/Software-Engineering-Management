package com.neu.prattle;

import com.neu.prattle.model.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    assertTrue(message.storeMessage());

    message.deleteMessage(message.getFromID(), message.getMessageID());
    System.out.println(message.deleteMessage(message.getFromID(), message.getMessageID()));
  }

//  @Test
//  public void testDeleteMessage() {
//    message.deleteMessage(message.getFromID(), message.getMessageID());
//    System.out.println(message.deleteMessage(message.getFromID(), message.getMessageID()));
////    assertEquals("File deleted successfully", message.deleteMessage(message.getFromID(), message.getMessageID()));
//  }

}
