package com.neu.prattle.websocket;

/**
 * A simple chat client based on websockets.
 *
 * @author https://github.com/eugenp/tutorials/java-websocket/src/main/java/com/baeldung/websocket/ChatEndpoint.java
 * @version dated 2017-03-05
 */

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.MessageService;
import com.neu.prattle.service.MessageServiceImpl;
import com.neu.prattle.service.ModerateService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * The Class ChatEndpoint.
 *
 * This class handles Messages that arrive on the server.
 */
@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  /**
   * The Constant chatEndpoints.
   */
  private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
  /**
   * The users.
   */
  private static HashMap<String, String> users = new HashMap<>();
  /**
   * The account service.
   */
  private UserService accountService = UserServiceImpl.getInstance();
  /**
   * The session.
   */
  private Session session;
  /**
   * The group service.
   */
  private GroupService groupService = GroupServiceImpl.getInstance();

  private ModerateService moderateService = ModerateService.getInstance();

  private MessageService messageService = MessageServiceImpl.getInstance();

  /**
   * Broadcast.
   *
   * Send a Message to each session in the pool of sessions. The Message sending action is
   * synchronized.  That is, if another Message tries to be sent at the same time to the same
   * endpoint, it is blocked until this Message finishes being sent..
   */
  private static void broadcast(Message message) {
    chatEndpoints.forEach(endpoint0 -> {
      final ChatEndpoint endpoint = endpoint0;
      synchronized (endpoint) {
        try {
          endpoint.session.getBasicRemote()
                  .sendObject(message);
        } catch (IOException | EncodeException e) {
          LOGGER.log(Level.INFO, e.getMessage());
        }
      }
    });
  }

  /**
   * Set services to be used in chatendpoint
   *
   * @param newAccountService  new user service
   * @param newGroupService    new group service
   * @param newModerateService new moderate service
   */
  public void setService(UserService newAccountService, GroupService newGroupService, ModerateService newModerateService, MessageService newMessageService) {
    accountService = newAccountService;
    groupService = newGroupService;
    moderateService = newModerateService;
    messageService = newMessageService;
  }

  /**
   * On open.
   *
   * Handles opening a new session (websocket connection). If the user is a known user (user
   * management), the session added to the pool of sessions and an announcement to that pool is made
   * informing them of the new user.
   *
   * If the user is not known, the pool is not augmented and an error is sent to the originator.
   *
   * @param session  the web-socket (the connection)
   * @param username the name of the user (String) used to find the associated UserService object
   * @throws IOException     Signals that an I/O exception has occurred.
   * @throws EncodeException the encode exception
   */
  @OnOpen
  public boolean onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {

    Optional<User> user = accountService.findUserByName(username);
    if (!user.isPresent()) {
      Message error = Message.messageBuilder()
              .setMessageContent(String.format("User %s could not be found", username))
              .build();

      session.getBasicRemote().sendObject(error);
      return false;
    }

    addEndpoint(session, username);
    Message message = createConnectedMessage(username);
    broadcast(message);
    return true;
  }

  /**
   * Creates a Message that some user is now connected - that is, a Session was opened
   * successfully.
   *
   * @param username the username
   * @return Message
   */
  private Message createConnectedMessage(String username) {
    return Message.messageBuilder()
            .setFrom(username)
            .setMessageContent("Connected!")
            .build();
  }

  /**
   * Adds a newly opened session to the pool of sessions.
   *
   * @param session  the newly opened session
   * @param username the user who connected
   */
  private void addEndpoint(Session session, String username) {
    this.session = session;
    chatEndpoints.add(this);
    /* users is a hashmap between session ids and users */
    users.put(session.getId(), username);
  }

  /**
   * On message.
   *
   * When a message arrives, broadcast it to all connected users.
   *
   * @param session the session originating the message
   * @param message the text of the inbound message
   */
  @OnMessage
  public void onMessage(Session session, Message message) {
    message.setFrom(users.get(session.getId()));

    try {
      if (!message.getSendToGroup()) {
        sendPersonalMessage(message);
      } else {
        sendGroupMessage(message, message.getTo(), session);
      }
    } catch (IOException | EncodeException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }

  }

  /**
   * On close.
   *
   * Closes the session by removing it from the pool of sessions and broadcasting the news to
   * everyone else.
   *
   * @param session the session
   */
  @OnClose
  public void onClose(Session session) {
    chatEndpoints.remove(this);
    Message message = new Message();
    message.setFrom(users.get(session.getId()));
    message.setContent("Disconnected!");
    broadcast(message);
  }

  /**
   * On error.
   *
   * Send a message to the specific user and guarantee that both of the users could see the posted
   * message
   *
   * @param message the message to be sent
   */
  public void sendPersonalMessage(Message message) throws IOException, EncodeException {
    chatEndpoints.forEach(endpoint0 -> {
      final ChatEndpoint endpoint = endpoint0;
      if (message.getFrom().equals(users.get(endpoint.session.getId())) || message.getTo().equals(users.get(endpoint.session.getId()))) {
        synchronized (endpoint) {
          try {
            endpoint.session.getBasicRemote()
                    .sendObject(message);
          } catch (IOException | EncodeException e) {
            LOGGER.log(Level.INFO, e.getMessage());
          }
        }
      }
    });
    message.setTimeStamp();
    message.setDate();
    message.setGroupId(-1);
    messageService.addMessage(message);
  }

  public void sendGroupMessage(Message message, String groupName, Session session) throws IOException, EncodeException {
    Optional<Group> group = groupService.findGroupByName(groupName);
    if (!group.isPresent()) {
      Message error = Message.messageBuilder()
              .setMessageContent(String.format("Group %s could not be found", groupName))
              .build();

      session.getBasicRemote().sendObject(error);
      return;
    }
    Group currentGroupObject = group.get();
    broadcastInGroup(message, currentGroupObject);
  }

  public void broadcastInGroup(Message message, Group currentGroupObject) throws IOException {
    List<User> members = moderateService.getMembers(currentGroupObject);
    if (members.isEmpty()) return;
    chatEndpoints.forEach(endpoint0 -> {
      ChatEndpoint endpoint = endpoint0;
      if (members.contains(new User(users.get(endpoint.session.getId())))) {
        synchronized (endpoint) {
          try {
            endpoint.session.getBasicRemote()
                    .sendObject(message);
          } catch (IOException | EncodeException e) {
            LOGGER.log(Level.INFO, e.getMessage());
          }
        }
      }
    });
    message.setTimeStamp();
    message.setDate();
    message.setGroupId(currentGroupObject.getGroupId());
    message.setTo(currentGroupObject.getName());
    messageService.addMessage(message);
  }
}

