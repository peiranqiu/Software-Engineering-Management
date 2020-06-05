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

//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;


public class MessageTest {
  /***
   * Message created by Message class
   */
  @Mock
  private Message message;
  @Mock
  private static MessageEncoder msgEncoder;

  @BeforeClass
  public static void setupBefore() {
    msgEncoder = new MessageEncoder();
    msgEncoder = mock(MessageEncoder.class);
  }

  @Before
  public void setup() {
    message.setFrom("peiran");
    message.setTo("rouni");
    message.setContent("Good Morning!");
    message.setFromID(123);
    message.setToID(456);
    message.setMessageID();
    message = mock(Message.class);
  }

//  @Test
  /***
   * Test message encode and message decode
   */
  public void testEncodeAndDecode() throws EncodeException {
    MessageEncoder msgEncoder = new MessageEncoder();
    msgEncoder.encode(message);
  }

  @Test
  /***
   * Test if soreMessage function and deleteMessage function both work successfully
   */
  public void testStoreMessageDeleteMessage() throws IOException, EncodeException {
    when(message.storeMessage()).thenReturn(true);
    assertTrue(message.storeMessage());
    assertEquals("File deleted successfully", message.deleteMessage(message.getFromID(), message.getMessageID()));
  }
}
