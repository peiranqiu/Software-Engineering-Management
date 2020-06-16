package com.neu.prattle;

import com.neu.prattle.model.Message;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;
import org.junit.Before;
import org.junit.Test;
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
  public void testStoreMessage() throws IOException, EncodeException {
    assertTrue(message.storeMessage());
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
//  @Test
//  public void testMakeDirectory() {
//    deleteDir(new File(message.getMessagePath() + "/" + message.getFromID()));
//    assertEquals("Successfully create receiver directory.", message.makeDirectory(message.getMessagePath(), message.getFromID()));
//  }

  /***
   * Used for removing an existing directory
   */
//  public void deleteDir(File dir) {
//    if (dir.exists()){
//      File[] files = dir.listFiles();
//      if(files != null) {
//        for (final File file : files) {
//          deleteDir(file);
//        }
//      }
//      dir.delete();
//    }
//  }

  /***
   * Test for willDecode() in Message Decoder class
   */
  @Test
  public void testWillDecode() {
    MessageDecoder decode = new MessageDecoder();
    assertFalse(decode.willDecode(null));
    assertTrue(decode.willDecode("test"));
  }
}
