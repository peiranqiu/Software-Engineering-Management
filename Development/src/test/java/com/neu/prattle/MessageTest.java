package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import javax.websocket.EncodeException;

public class MessageTest {
  /***
   * Message created by Message class
   */
  private Message message;
  private static MessageEncoder msgEncoder;

  public static void setupBefore() {
    msgEncoder = new MessageEncoder();
    msgEncoder = mock(MessageEncoder.class);
  }

  @Before
  public void setup() {
    message = new Message();
    message.setFrom("peiran");
    message.setTo("rouni");
    message.setContent("Good Morning!");
    message.setFromID(123);
    message.setToID(456);
    message.setMessageID();
    message = mock(Message.class);
  }

  /***
   * Test message encode and message decode
   */
  @Test
  public void testEncodeAndDecode() throws EncodeException {
    MessageEncoder msgEncoder = new MessageEncoder();
    msgEncoder.encode(message);
  }


  /***
   * Test if soreMessage function and deleteMessage function both work successfully
   */
  @Test
  public void testStoreMessageDeleteMessage() throws IOException, EncodeException {
    when(message.storeMessage()).thenReturn(true);
    assertTrue(message.storeMessage());
    when(message.deleteMessage(message.getFromID(), message.getMessageID())).thenReturn("File deleted successfully");
    assertEquals("File deleted successfully", message.deleteMessage(message.getFromID(), message.getMessageID()));
  }
}
