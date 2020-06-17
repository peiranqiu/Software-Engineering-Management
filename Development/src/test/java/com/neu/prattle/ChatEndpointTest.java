/*
 * Copyright (c) 2020. Manan Patel
 * All rights reserved
 */

package com.neu.prattle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import com.neu.prattle.websocket.ChatEndpoint;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChatEndpointTest {


  private static User testUser1;


  private static User testUser2;
  private Message message;

  @Mock
  private static User testUser3;

  // Mocking Session to connect with websocket
  @Mock
  private Session session1;
  @Mock
  private Session session2;
  // Mocking basic which is used by session to send message
  @Mock
  private Basic basic;
  private Group group1;

  // To capture messages sent by Websockets
  private ArgumentCaptor<Object> valueCapture;
  // ChatEndpoints to test
  private ChatEndpoint chatEndpoint1;
  private ChatEndpoint chatEndpoint2;

  @BeforeClass
  public static void setupBefore() {
    testUser1 = new User("testName1");
    testUser2 = new User("testName2");
    testUser3 = new User("testName3");
    testUser1.setPassword("User1Password");
    testUser2.setPassword("User2Password");
    testUser3.setPassword("User2Password");

  }

  @Before
  public void setup() throws IOException, EncodeException {
    // Creating session mocks
    session1 = mock(Session.class);
    session2 = mock(Session.class);

    basic = mock(Basic.class);

    message = Message.messageBuilder().build();

    chatEndpoint1 = new ChatEndpoint();
    chatEndpoint2 = new ChatEndpoint();

    // Capturing method calls using when and then
    when(session1.getBasicRemote()).thenReturn(basic);
    when(session2.getBasicRemote()).thenReturn(basic);

    // Setting up argument captor to capture any Objects
    valueCapture = ArgumentCaptor.forClass(Object.class);
    // Defining argument captor to capture messages emitted by websockets
    doNothing().when(basic).sendObject(valueCapture.capture());
    // Capturing method calls to session.getId() using when and then
    when(session1.getId()).thenReturn("id1");
    when(session2.getId()).thenReturn("id2");

    group1 = new Group("testChatGroup1");
    group1.setGroupId(1);
  }

  @Test
  public void testBroadcastInGroupFails() throws IOException, EncodeException {
    GroupService groupService = GroupServiceImpl.getInstance();
    groupService.addGroup(group1);
    UserService userService = UserServiceImpl.getInstance();
    ModerateService moderateService = ModerateService.getInstance();
    userService.addUser(testUser1);
    userService.addUser(testUser2);
    User moderator1 = moderateService.addGroupModerator(group1,testUser1,testUser1);
    moderateService.addGroupMember(group1,testUser1,testUser2);

    User user1 = userService.findUserByName("testName1").get();
    User user2 = userService.findUserByName("testName2").get();
    chatEndpoint1.onOpen(session1, user1.getName());
    chatEndpoint2.onOpen(session2, user2.getName());
    message.setFrom(user1.getName());
    message.setTo("testChatGroup1");
    message.setFromID(user1.getUserId());
    message.setToID(user2.getUserId());
    message.setContent("Welcome to this group again!");
    message.setMessageID();
    message.setMessagePath();
    Group groupEmpty = new Group();
    chatEndpoint1.broadcastInGroup(message, groupEmpty);
    assertNotEquals(testUser1, testUser2);
  }

  @Test
  public void testOnClose() throws IOException, EncodeException {
    chatEndpoint1.onOpen(session1, testUser1.getName());
    chatEndpoint2.onOpen(session2, testUser2.getName());

    chatEndpoint1.onClose(session1);

    // Finding the message with content 'Disconnected!'
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Disconnected!")).findAny();

    if (m.isPresent()) {
      assertEquals("Disconnected!", m.get().getContent());
      assertEquals(testUser1.getName(), m.get().getFrom());
    } else {
      fail();
    }
  }

  @Test
  public void testOnMessage() throws IOException, EncodeException {

    chatEndpoint1.onOpen(session1, testUser1.getName());
    chatEndpoint2.onOpen(session2, testUser2.getName());

    message.setTo(testUser2.getName());
    message.setContent("Hey");

    // Sending a message using onMessage method
    chatEndpoint1.onMessage(session1, message);

    // Finding messages with content hey
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Hey")).findAny();

    if (m.isPresent()) {
      assertEquals("Hey", m.get().getContent());
      assertEquals(testUser1.getName(), m.get().getFrom());
    } else {
      fail();
    }
  }

  @Test
  public void testOnOpen1() throws IOException, EncodeException {
    chatEndpoint1.onOpen(session1, testUser1.getName());

    // Finding the message with content 'Connected!'
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Connected!")).findAny();

    if (m.isPresent()) {
      assertEquals("Connected!", m.get().getContent());
      assertEquals(testUser1.getName(), m.get().getFrom());
    } else {
      fail();
    }
  }

  @Test
  public void testOnOpen2() throws IOException, EncodeException {
    chatEndpoint1.onOpen(session1, testUser3.getName());

    // Finding the message with content 'Connected!'
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("User testName3 could not be found")).findAny();

    if (m.isPresent()) {
      assertEquals("User testName3 could not be found", m.get().getContent());
    } else {
      fail();
    }
  }

  @Test
  public void testSendGroupMessage() throws IOException, EncodeException {
    UserService userService = UserServiceImpl.getInstance();
    User user1 = userService.findUserByName("testName1").get();
    User user2 = userService.findUserByName("testName2").get();
    chatEndpoint1.onOpen(session1, user1.getName());
    chatEndpoint2.onOpen(session2, user2.getName());
    message.setFrom(user1.getName());
    message.setTo("testChatGroup1");
    message.setFromID(user1.getUserId());
    message.setToID(user2.getUserId());
    message.setContent("Welcome to this group!");
    message.setMessageID();
    message.setMessagePath();
    message.setTimeStamp();
    // Sending a message using onMessage method
    chatEndpoint1.sendGroupMessage(message, "testChatGroup1", session1);

    // Finding messages with content hey
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Welcome to this group!")).findAny();

    if (m.isPresent()) {
      String messagePath = message.getMessagePath();
      File file = new File(messagePath + "/Group" + "/" + group1.getGroupId() + "_" + message.getCurrDate() + ".txt");
      assertEquals(true, checkLogHasMessage("testName1: Welcome to this group!   " + message.getTimeStamp(), file));
    } else {
      fail();
    }
  }

  @Test
  public void testSendGroupMessage2() throws IOException, EncodeException {
    UserService userService = UserServiceImpl.getInstance();
    User user1 = userService.findUserByName("testName1").get();
    User user2 = userService.findUserByName("testName2").get();
    chatEndpoint1.onOpen(session1, user1.getName());
    chatEndpoint2.onOpen(session2, user2.getName());
    message.setFrom(user1.getName());
    message.setTo("testChatGroup1");
    message.setFromID(user1.getUserId());
    message.setToID(user2.getUserId());
    message.setContent("Welcome to this group again!");
    message.setMessageID();
    message.setMessagePath();
    message.setTimeStamp();
    // Sending a message using onMessage method
    chatEndpoint1.sendGroupMessage(message, "testChatGroup1", session1);

    // Finding messages with content hey
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Welcome to this group again!")).findAny();

    if (m.isPresent()) {
      String messagePath = message.getMessagePath();
      File file = new File(messagePath + "/Group" + "/" + group1.getGroupId() + "_" + message.getCurrDate() + ".txt");
      assertEquals(true, checkLogHasMessage("testName1: Welcome to this group again!   " + message.getTimeStamp(), file));
    } else {
      fail();
    }
  }

  @Test
  public void testSendPersonalMessage() throws IOException, EncodeException {
    UserService userService = UserServiceImpl.getInstance();
    User user1 = userService.findUserByName("testName1").get();
    User user2 = userService.findUserByName("testName2").get();
    chatEndpoint1.onOpen(session1, user1.getName());
    chatEndpoint2.onOpen(session2, user2.getName());
    message.setFrom(user1.getName());
    message.setTo(user2.getName());
    message.setFromID(user1.getUserId());
    message.setToID(user2.getUserId());
    message.setContent("Hey");
    message.setMessageID();
    message.setMessagePath();
    message.setTimeStamp();
    message.setCurrDate();
    // Sending a message using onMessage method
    chatEndpoint1.sendPersonalMessage(message);

    // Finding messages with content hey
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Hey")).findAny();

     if (m.isPresent()) {
       String messagePath = message.getMessagePath();
       File file = new File(messagePath + "/PrivateChatHistory" + "/" + message.getFromID() + "_" + message.getToID() + "_" + message.getCurrDate() + ".txt");
       assertEquals(true, checkLogHasMessage("testName1: Hey   " + message.getTimeStamp(), file));
    } else {
      fail();
    }
  }

  @Test
  public void testDeleteGroupMessage() throws IOException, EncodeException {
    UserService userService = UserServiceImpl.getInstance();
    User user1 = userService.findUserByName("testName1").get();
    User user2 = userService.findUserByName("testName2").get();
    chatEndpoint1.onOpen(session1, user1.getName());
    chatEndpoint2.onOpen(session2, user2.getName());
    message.setFrom(user1.getName());
    message.setTo("testChatGroup1");
    message.setFromID(user1.getUserId());
    message.setToID(user2.getUserId());
    message.setContent("Welcome to this group again!");
    message.setMessageID();
    message.setMessagePath();
    message.setTimeStamp();
    message.setCurrDate();
    // Sending a message using onMessage method
    chatEndpoint1.sendGroupMessage(message, "testChatGroup1", session1);

    // Finding messages with content hey
    Optional<Message> m = valueCapture.getAllValues().stream()
            .map(val -> (Message) val)
            .filter(msg -> msg.getContent().equals("Welcome to this group again!")).findAny();

    if (m.isPresent()) {
      String messagePath = message.getMessagePath();
      File file = new File(messagePath + "/Group" + "/" + group1.getGroupId() + "_" + message.getCurrDate() + ".txt");
      message.deleteGroupMessage(group1);
      assertEquals(false, checkLogHasMessage("testName1: Welcome to this group again!   " + message.getTimeStamp(), file));
    } else {
      fail();
    }
  }


  public boolean checkLogHasMessage(String msgSent, File file) {
    try {
      Scanner scanner = new Scanner(file);
      int lineNum = 0;
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lineNum++;
        if(line.equals(msgSent)) {
          return true;
        }
      }
    } catch(FileNotFoundException e) {
      return false;
    }
    return false;
  }

}
