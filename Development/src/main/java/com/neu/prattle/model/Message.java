package com.neu.prattle.model;

import com.neu.prattle.websocket.MessageEncoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;

/***
 * The Message class built to get the necessary message attributes and message information
 *
 * @author Yijia Hao
 * @version dated 2020-06-05
 */
public class Message {

  private Logger logger = Logger.getLogger(Message.class.getName());
  private static final String MESSAGESENT = "/messageSent";
  private static final String MESSAGERECEIVE = "/messageReceived";
  private static final String JSON = ".json";
  /***
   * The name of the user who sent this message.
   */
  private String from;
  /***
   * The name of the user to whom the message is sent.
   */
  private String to;
  /***
   * It represents the contents of the message.
   */
  private String content;
  /***
   * Unique id of the message
   */
  private String messageID;
  /***
   * The date of the message creation
   */
  private String messageDate;
  /***
   * The directory of massage folder
   */
  private String messagePath;
  /***
   * Initialize the ID of sender
   */
  private int fromID = -1;
  /***
   * Initialize the ID of receiver
   */
  private int toID = -1;
  /***
   * Message to group or not
   */
  private boolean sendToGroup = false;

  /***
   * Return the completed message with sender and receiver
   */
  @Override
  public String toString() {
    return new StringBuilder()
            .append("From: ").append(from)
            .append("To: ").append(to)
            .append("Content: ").append(content)
            .toString();
  }

  public String toStringForGroupChatLog() {
    return new StringBuilder()
            .append(from).append(": ")
            .append(content)
            .toString();
  }

  /***
   * Retrieve name of message sender
   */
  public String getFrom() {
    return from;
  }

  /***
   * Set name of message sender
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /***
   * Retrieve name of message receiver
   */
  public String getTo() {
    return to;
  }

  /***
   * Set name of message receiver
   */
  public void setTo(String to) {
    this.to = to;
  }

  /***
   * Retrieve message content
   */
  public String getContent() {
    return content;
  }

  /***
   * Set message content
   */
  public void setContent(String content) {
    this.content = content;
  }

  /***
   * Set id of message
   */
  public void setMessageID() {
    this.messageID = UUID.randomUUID().toString();
  }

  /***
   * Retrieve id of message
   */
  public String getMessageID() {
    return messageID;
  }

  /***
   * Retrieve date of message
   */
  public String getMessageDate() {
    return messageDate;
  }

  /***
   * Set id of message sender
   */
  public void setFromID(int fromID) {
    this.fromID = fromID;
  }

  /***
   * Retrieve id of message sender
   */
  public int getFromID() {
    return fromID;
  }

  /***
   * Set id of message receiver
   */
  public void setToID(int toID) {
    this.toID = toID;
  }

  /***
   * Retrieve id of message receiver
   */
  public int getToID() {
    return toID;
  }

  /***
   * Return send to group or not
   */
  public boolean getSendToGroup() {
    return sendToGroup;
  }
  /***
   * Retrieve sent to group or not
   */
  public void setSendToGroup(boolean sendToGroup) {
    this.sendToGroup = sendToGroup;
  }

  /***
   * Get the parent directory of the message folder
   */
  public String getMessagePath() {
    String path = Message.class.getResource("").getPath();
    String mainPath = path.substring(0, path.indexOf("Development") + 11);
    return mainPath + "/src";
  }

  /***
   * Set the parent directory of the message folder
   */
  public void setMessagePath() {
    this.messagePath = getMessagePath();
  }

  /***
   * Create the messageSent and messageReceived directories for the current user
   */
  public String makeDirectory(String messagePath, int userID) {
    String dirName1 = messagePath + "/" + userID;
    File fromUserFile = new File(dirName1);
    if (!fromUserFile.mkdir()) {
      throw new IllegalArgumentException("Creating user fails.");
    }
    logger.info("Successfully create user directory.");
    String dirName2 = messagePath + "/" + userID + MESSAGESENT;
    File fileUserSent = new File(dirName2);
    if (!fileUserSent.mkdir()) {
      throw new IllegalArgumentException("Creating sender directory fails.");
    }
    logger.info("Successfully create sender directory.");
    String dirName3 = messagePath + "/" + userID + MESSAGERECEIVE;
    File fileUserReceived = new File(dirName3);
    if (!fileUserReceived.mkdir()) {
      throw new IllegalArgumentException("Creating receiver directory fails.");
    }
    logger.info("Successfully create receiver directory.");
    return "Successfully create receiver directory.";
  }

  /***
   * Make directories and save the current message into a JSON file under the folder of the current sender and receiver
   */
  public boolean storeMessage() throws IOException, EncodeException {
    if (!sendToGroup && fromID != -1 && toID != -1 && !content.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
      if (!Files.exists(Paths.get(messagePath + "/" + fromID))) {
        makeDirectory(messagePath, fromID);
      }
      if (!Files.exists(Paths.get(messagePath + "/" + toID))) {
        makeDirectory(messagePath, toID);
      }
      String name1 = messagePath + "/" + fromID + MESSAGESENT + "/" + messageID + JSON;
      String name2 = messagePath + "/" + toID + MESSAGERECEIVE + "/" + messageID + JSON;
      File file1 = new File(name1);
      File file2 = new File(name2);
      if (file1.createNewFile() && file2.createNewFile()) {
        writeFile(messagePath);
        return true;
      } else {
        throw new IOException();
      }
    }
    return false;
  }

  /***
   * Write the current message into a JSON file under the folder of the current sender and receiver
   */
  private void writeFile(String messagePath) throws IOException, EncodeException {
    MessageEncoder msEncoder = new MessageEncoder();
    String file1 = messagePath + "/" + fromID + MESSAGESENT + "/" + messageID + JSON;
    try(FileWriter myWriter = new FileWriter(file1)) {

      myWriter.write(msEncoder.encode(this));
    } catch(NullPointerException e) {
      logger.info(e.getMessage());
    }

    String file2 = messagePath + "/" + toID + MESSAGERECEIVE + "/" + messageID + JSON;

    try(FileWriter myWriter2 = new FileWriter(file2)) {
      MessageEncoder msEncoder2 = new MessageEncoder();
      myWriter2.write(msEncoder2.encode(this));
    } catch(NullPointerException e) {
      logger.info(e.getMessage());
    }
  }

  /***
   * Remove the current message sent by sender
   */
  public String deleteMessage(int userID, String messageID) {
    String output = "File remove fails.";
    String s = messagePath + "/" + userID + MESSAGESENT + "/" + messageID + JSON;
    Path path = Paths.get(s);

    try {
      Files.delete(path);
      output =  "File deleted successfully";
    } catch (IOException e) {
      logger.info("File not deleted.");
    }
    return output;
  }

  public void saveChatLog(Group currentGroupObject, boolean sendToGroup) throws IOException {
    String group ="/Group";
    if (sendToGroup && fromID != -1 && !content.isEmpty() && !from.isEmpty()) {
      if (!Files.exists(Paths.get(messagePath + group))) {
        String groupDir = messagePath + group;
        File groupDirFile = new File(groupDir);
        groupDirFile.mkdir();
        }
      String groupChatLogName =
              messagePath + group + "/" + currentGroupObject.getGroupId() + ".txt";
      File groupChatFile = new File(groupChatLogName);
      //check if the chat log file already exists
      if (!Files.exists(Paths.get(groupChatLogName))) {
        FileWriter myWriter = new FileWriter(groupChatFile);
        logger.info("Chat log file created for " + currentGroupObject.getName());
        myWriter.write(toStringForGroupChatLog());
        myWriter.close();
      } else {
        FileWriter myWriter = new FileWriter(groupChatFile, true);
        BufferedWriter br = new BufferedWriter(myWriter);
        try {
          br.newLine();
          br.write(toStringForGroupChatLog());
        } catch (IOException e) {
          logger.log(Level.INFO, e.getMessage());
        } finally {
          br.close();
          myWriter.close();
        }
      }
    }
  }

  /***
   * Create an object of MessageBuilder class
   */
  public static MessageBuilder messageBuilder() {
    return new MessageBuilder();
  }

  /***
   * A Builder helper class to create instances of {@link Message}
   */
  public static class MessageBuilder {
    /***
     * Invoking the build method will return this message object.
     */
    Message message;

    public MessageBuilder() {
      message = new Message();
      message.setFrom("Not set");
    }

    public MessageBuilder setFrom(String from) {
      message.setFrom(from);
      return this;
    }

    public MessageBuilder setTo(String to) {
      message.setTo(to);
      return this;
    }

    public MessageBuilder setMessageContent(String content) {
      message.setContent(content);
      return this;
    }

    public Message build() {
      return message;
    }
  }
}
