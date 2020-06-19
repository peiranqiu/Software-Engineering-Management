package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageEntityTest {
  /***
   * Message created by Message class
   */
  private Message message;
  private MessageEncoder messageEncoder;

  @Mock
  ObjectMapper objectMapper;

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
    message.setCurrDate();
    messageEncoder = new MessageEncoder();

    objectMapper = new ObjectMapper();
    objectMapper = mock(ObjectMapper.class);
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

    msgDecoder.destroy();
    msgEncoder.destroy();
  }

  /***
   * Test if storeMessage() function and deleteMessage function both work successfully
   */
  @Test
  public void testStoreMessage() throws IOException, EncodeException {
    assertTrue(message.storeMessage());
  }

  /***
   * Test if storeMessage() function fails when the user has invalid userID
   */
  @Test
  public void testStoreMessageFail1() throws IOException, EncodeException {
    Message message1 = new Message();
    message1.setFromID(-1);
    assertFalse(message1.storeMessage());
  }

  /***
   * Test if storeMessage() function fails when the user has invalid userID
   */
  @Test
  public void testStoreMessageFail2() throws IOException, EncodeException {
    Message message1 = new Message();
    message1.setToID(-1);
    assertFalse(message1.storeMessage());
  }

  /***
   * Test if storeMessage() function fails when the user has invalid userID
   */
  @Test
  public void testStoreMessageFail3() throws IOException, EncodeException {
    Message message1 = new Message();
    message1.setFromID(-1);
    message1.setToID(-1);
    assertFalse(message1.storeMessage());
  }

  /***
   * Test toString functions
   */
  @Test
  public void testToString() {
    assertEquals("From: peiranTo: rouniContent: Good Morning!", message.toString());
  }

  @Test
  public void testToStringForGroupChatLog() {
    assertEquals("peiran: Good Morning!   null", message.toStringForGroupChatLog());
  }

  /***
   * Test on MessageBuilder Class
   */
  @Test
  public void testMessageBuilder() {
    Message ms1 = Message.messageBuilder()
            .setFrom(message.getFrom())
            .setTo(message.getTo())
            .setMessageContent("Good Morning!")
            .build();
    assertEquals(ms1.getFrom(), message.getFrom());
    assertEquals(ms1.getTo(), message.getTo());
    assertEquals(ms1.getContent(), message.getContent());
  }

  @Test
  public void testDeletePersonalMessage() throws IOException, EncodeException {
    assertTrue(message.storeMessage());
    message.saveChatLogPerson();
    message.deletePersonalMessage();
  }

  /***
   * Test for willDecode() in Message Decoder class
   */
  @Test
  public void testWillDecode() {
    MessageDecoder decode = new MessageDecoder();
    assertFalse(decode.willDecode(null));
    assertTrue(decode.willDecode("test"));
  }

  @Test
  public void testMakeDir() {
    message.makeDirectory("/Users", 1);
  }
}