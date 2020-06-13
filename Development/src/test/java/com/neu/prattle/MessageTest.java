package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import javax.websocket.EncodeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    msgDecoder.destroy();
    msgEncoder.destroy();
  }

  /***
   * Test if storeMessage() function and deleteMessage function both work successfully
   */
  @Test
  public void testStoreMessageDeleteMessage() throws IOException, EncodeException {
    assertTrue(message.storeMessage());
    assertEquals("File deleted successfully", message.deleteMessage(message.getFromID(), message.getMessageID()));
    assertEquals("File remove fails.", message.deleteMessage(message.getFromID(), message.getMessageID()));
    deleteDir(new File(message.getMessagePath() + "/" + message.getFromID()));
    deleteDir(new File(message.getMessagePath() + "/" + message.getToID()));
  }

  /***
   * Test if storeMessage() function fails when the user has invalid userID
   */
  @Test
  public void testStoreMessageFails() throws IOException, EncodeException {
    Message message1 = new Message();
    message1.setFromID(-1);
    assertFalse(message1.storeMessage());
  }

  /***
   * Test toString functions
   */
  @Test
  public void testToString() {
    assertEquals("From: peiranTo: rouniContent: Good Morning!", message.toString());
  }

  /***
   * Test creating user fails for makeDirectory
   */
  @Test(expected=IllegalArgumentException.class)
  public void testMakeDirectoryException1() {
    assertEquals("Creating user fails.", message.makeDirectory(message.getMessagePath(), message.getFromID()));
  }

  /***
   * Test creating sender success and create sender fails for makeDirectory
   */
  @Test(expected=IllegalArgumentException.class)
  public void testMakeDirectoryException2() {
    assertEquals("Successfully create sender directory.", message.makeDirectory(message.getMessagePath() + "/" + 12345 + "/messageSent", 12345));
    assertEquals("Creating sender directory fails.", message.makeDirectory(message.getMessagePath() + "/" + 12345 + "/messageSent", 12345));
  }

  /***
   * Test creating receiver success and create receiver fails for makeDirectory
   */
  @Test(expected=IllegalArgumentException.class)
  public void testMakeDirectoryException3() {
    assertEquals("Successfully create receiver directory.", message.makeDirectory(message.getMessagePath() + "/" + 12345 + "/messageReceived", 12345));
    assertEquals("Creating receiver directory fails.", message.makeDirectory(message.getMessagePath() + "/" + 12345 + "/messageReceived", 12345));
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

  /***
   * Test for deleteDir() function
   */
  @Test
  public void testMakeDirectory() {
    deleteDir(new File(message.getMessagePath() + "/" + message.getFromID()));
    assertEquals("Successfully create receiver directory.", message.makeDirectory(message.getMessagePath(), message.getFromID()));
  }

  /***
   * Used for removing an existing directory
   */
  public void deleteDir(File dir) {
    if (dir.exists()){
      File[] files = dir.listFiles();
      if(files != null) {
        for (final File file : files) {
          deleteDir(file);
        }
      }
      dir.delete();
    }
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
  public void testGetCurrDate() {
    System.out.println(message.getCurrDate());
  }
}
