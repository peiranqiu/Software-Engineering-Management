package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.EncodeException;

public class MessageTest {
  /***
   * Message created by Message class
   */
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
    message.setMessagePath();
  }

  /***
   * Test message encode and message decode
   */
  @Test
  public void testEncodeAndDecode() {
    String encodeString = "";
    MessageEncoder msgEncoder = new MessageEncoder();
    MessageDecoder msgDecoder = new MessageDecoder();
    try {
      encodeString = msgEncoder.encode(message);
    } catch (EncodeException e) {
      assertNull(e);
    }
    Message afterMessageConversion = msgDecoder.decode(encodeString);
    assertEquals(message.getFrom(), afterMessageConversion.getFrom());
    assertEquals(message.getTo(), afterMessageConversion.getTo());
    assertEquals(message.getContent(), afterMessageConversion.getContent());
    assertEquals(message.getMessageID(), afterMessageConversion.getMessageID());
    assertEquals(message.getMessagePath(), afterMessageConversion.getMessagePath());
    assertEquals(message.getFromID(), afterMessageConversion.getFromID());
    assertEquals(message.getToID(), afterMessageConversion.getToID());
  }

  /***
   * Test if soreMessage function and deleteMessage function both work successfully
   */
  @Test
  public void testStoreMessageDeleteMessage() throws IOException, EncodeException {
    assertTrue(message.storeMessage());
    assertEquals("File deleted successfully", message.deleteMessage(message.getFromID(), message.getMessageID()));
  }
}
