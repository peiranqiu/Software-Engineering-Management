package com.neu.prattle;
//import com.neu.prattle.model.Message;
//import com.neu.prattle.websocket.MessageDecoder;
//import com.neu.prattle.websocket.MessageEncoder;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import java.io.IOException;
//import javax.websocket.EncodeException;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
// import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;

public class MessageEncoderDecoderTest {
//  @Mock
//  ObjectMapper objectMapper;
//  /***
//   * Message created by Message class
//   */
//  private Message message;
//  private MessageEncoder messageEncoder;
//  private MessageDecoder messageDecoder;
//
//  @Before
//  public void setup() {
//    message = new Message();
//    message.setFrom("peiran");
//    message.setTo("rouni");
//    message.setContent("Good Morning!");
//    messageEncoder = new MessageEncoder();
//
//    objectMapper = new ObjectMapper();
//    objectMapper = mock(ObjectMapper.class);
//  }
//
//  /***
//   * Test message encode and message decode
//   */
//  @Test
//  public void testEncodeAndDecode() {
//    String encodeString = "";
//    MessageEncoder msgEncoder = new MessageEncoder();
//    MessageDecoder msgDecoder = new MessageDecoder();
//    try {
//      encodeString = msgEncoder.encode(message);
//    } catch (EncodeException e) {
//      assertNull(e);
//    }
//    Message afterMessageConversion = msgDecoder.decode(encodeString);
//    assertEquals(message.getFrom(), afterMessageConversion.getFrom());
//    assertEquals(message.getTo(), afterMessageConversion.getTo());
//    assertEquals(message.getContent(), afterMessageConversion.getContent());
//
//    msgDecoder.destroy();
//    msgEncoder.destroy();
//  }
//
//
//  /***
//   * Test for willDecode() in Message Decoder class
//   */
//  @Test
//  public void testWillDecode() {
//    MessageDecoder decode = new MessageDecoder();
//    assertFalse(decode.willDecode(null));
//    assertTrue(decode.willDecode("test"));
//  }
//
//  /***
//   * Test for Message Encode class
//   */
//  @Test
//  public void testEncode() throws IOException, EncodeException {
//    when(objectMapper.writeValueAsString(any(Message.class))).thenThrow(IOException.class);
//    messageEncoder.setObjectMapper(objectMapper);
//    assertEquals("{}", messageEncoder.encode(message));
//  }
}
