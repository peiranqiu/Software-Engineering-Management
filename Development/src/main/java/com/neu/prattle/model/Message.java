package com.neu.prattle.model;

import com.neu.prattle.websocket.MessageEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.websocket.EncodeException;


/***
 * The Message class built to get the necessary message attributes and message information
 *
 * @author Yijia Hao
 * @version dated 2020-06-05
 */
public class Message {

  private static final String MESSAGESENT = "/messageSent";
  private static final String MESSAGERECEIVE = "/messageReceived";
  private static final String JSON = ".json";
  private static final String USER = "/User";

  private static Logger logger = Logger.getLogger(Message.class.getName());
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
   * Date of the message sent
   */
  private String currDate;
  /***
   * TimeStamp of the message sent
   */
  private String timeStamp;

  /***
   * Create an object of MessageBuilder class
   */

  ServletContext context;
  public static MessageBuilder messageBuilder() {
    return new MessageBuilder();
  }

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

  public String toStringForPrivateChatLog() {
    return new StringBuilder()
            .append(from).append(": ")
            .append(content).append("   ")
            .append(timeStamp)
            .toString();
  }

  public String toStringForGroupChatLog() {
    return new StringBuilder()
            .append(from).append(": ")
            .append(content).append("   ")
            .append(timeStamp)
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
   * Retrieve id of message sender
   */
  public int getFromID() {
    return fromID;
  }

  /***
   * Set id of message sender
   */
  public void setFromID(int fromID) {
    this.fromID = fromID;
  }

  /***
   * Retrieve id of message receiver
   */
  public int getToID() {
    return toID;
  }

  /***
   * Set id of message receiver
   */
  public void setToID(int toID) {
    this.toID = toID;
  }

  /***
   * Return send to group or not
   */
  public boolean getSendToGroup() {
    return sendToGroup;
  }

  /**
   * Set if sent to group
   */
  public void setSendToGroup(boolean sendToGroup) {
    this.sendToGroup = sendToGroup;
  }

  /***
   * Retrieve current Date
   */
  public String getCurrDate() {
    DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /***
   * Retrieve current Date
   */
  public void setCurrDate() {
    this.currDate = getCurrDate();
  }

  /***
   * Get the parent directory of the message folder
   */
  public String getMessagePath() {
//    String path = Message.class.getResource("").getPath();
//    String mainPath = path.substring(0, path.indexOf("Development") + 11);
//    return mainPath + "/src";
      return "";
  }

  /***
   * Set the parent directory of the message folder
   */
  public void setMessagePath() {
    this.messagePath = getMessagePath();
  }

  /***
   * Retrieve the time stamp of the current message
   */
  public String getTimeStamp() {
    DateTimeFormatter mdy = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    timeStamp = mdy.format(now);
    return timeStamp;
  }

  /***
   * Get the time stamp of the current message
   */

  public void setTimeStamp() {
    this.timeStamp = getTimeStamp();
  }

  /***
   * Create the messageSent and messageReceived directories for the current user
   */
  private void makeDirectory(String messagePath) {
    File userFile = new File(messagePath);
    createDirectory(messagePath, userFile);

    File fileUserSent = new File(messagePath + MESSAGESENT);
    createDirectory(messagePath, fileUserSent);

    File fileUserReceived = new File(messagePath + MESSAGERECEIVE);
    createDirectory(messagePath, fileUserReceived);
    return;
  }

  private void createDirectory(String messagePath, File userFile) {
    try {
      if (userFile.mkdir()) {
        logger.info("Created User Folder: " + messagePath);
      } else {
        logger.info("folder already exists: " + messagePath);
      }
    } catch (SecurityException e) {
      throw new SecurityException("Cannot create this directory." + messagePath);
    }
  }

  /***
   * Make directories and save the current message into a JSON file under the folder of the current sender and receiver
   */
  public boolean storeMessage() throws IOException, EncodeException {
    if (!sendToGroup && fromID != -1 && toID != -1 && !content.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
      logger.info("Preparing to store message." + this.toString());
      String fromPath = "/opt/prattle/messages" + USER + "/" + fromID;
      String toPath = "/opt/prattle/messages" + USER + "/" + toID;
      logger.info("fromPath = " + fromPath);

      makeDirectory(fromPath);
      makeDirectory(toPath);

      File file1 = new File(fromPath + MESSAGESENT + "/" + messageID + JSON);
      File file2 = new File(toPath + MESSAGERECEIVE + "/" + messageID + JSON);

      if (file1.createNewFile()) {
        writeFiles(file1);
      } else {
        throw new IOException("File cannot be created.");
      }

      if (file2.createNewFile()) {
        writeFiles(file2);
        return true;
      } else {
        throw new IOException("File cannot be created.");
      }
    }
    return false;
  }

  private void writeFiles(File myfile) throws IOException, EncodeException {
    try (FileWriter myWriter = new FileWriter(myfile)) {
      MessageEncoder msEncoder = new MessageEncoder();
      myWriter.write(msEncoder.encode(this));
      logger.info("write file success: " + myfile.getName());
    } catch (NullPointerException e) {
      logger.info(e.getMessage());
    }
  }

  /***
   * Write the current message into a JSON file under the folder of the current sender and receiver
   */
  public void writeFile() throws IOException, EncodeException {
    writeSenderReceiverJson(MESSAGESENT, fromID);
    writeSenderReceiverJson(MESSAGERECEIVE, toID);
  }

  public void writeSenderReceiverJson(String folderName, int userID) throws IOException, EncodeException {
    String file = messagePath + USER + "/" + userID + folderName + "/" + messageID + JSON;
    try (FileWriter myWriter = new FileWriter(file)) {
      MessageEncoder msEncoder = new MessageEncoder();
      myWriter.write(msEncoder.encode(this));
    } catch (NullPointerException e) {
      logger.info(e.getMessage());
    }
  }

  /***
   * Remove the current message sent by sender from a personal dialog
   */
  public String deletePersonalMessage() throws IOException {
    String s1 = messagePath + USER + fromID + MESSAGESENT + "/" + messageID + JSON;
    Path path1 = Paths.get(s1);
    try {
      Files.delete(path1);
    } catch (IOException e) {
      logger.info("Message Sender File could not be deleted.");
    }
    String s2 = messagePath + USER + toID + MESSAGERECEIVE + "/" + messageID + JSON;
    Path path2 = Paths.get(s2);
    try {
      Files.delete(path2);
    } catch (IOException e) {
      logger.info("Message Receiver File could not be deleted.");
    }
    String s3 = messagePath + "/PrivateChatHistory/" + fromID + "_" + toID + "_" + currDate + ".txt";
    File file = new File(s3);
    helperDelete(file);
    return "Successfully deleted this message!";
  }

  /***
   * Remove the current message sent by sender from a group dialog
   */
  public String deleteGroupMessage(Group currentGroupObject) throws IOException {
    int groupID = currentGroupObject.getGroupId();
    String s3 = messagePath + "/Group/" + groupID + "_" + currDate + ".txt";
    File file = new File(s3);
    helperDelete(file);
    return "Successfully deleted this message!";
  }

  /**
   * helper method to delete message
   */
  public void helperDelete(File file) throws IOException {
    FileReader in = new FileReader(file);
    try (BufferedReader bufIn = new BufferedReader(in)) {
      CharArrayWriter tempStream = new CharArrayWriter();
      //Substitution
      String line = null;
      while ((line = bufIn.readLine()) != null) {
        line = line.replaceAll(getFrom() + ": " + getContent() + "   " + getTimeStamp(), "");
        //write this line into storage
        tempStream.write(line);
        //Add Line Seperator
        tempStream.append(System.getProperty("line.separator"));
      }
      FileWriter out = new FileWriter(file);
      tempStream.writeTo(out);
      out.close();
    }
  }

  /***
   * Save private chat history under the PrivateChatHistory folder by sender's id, receiver's id, and message sent date.
   */
  public void saveChatLogPerson() throws IOException {
    String privateChat = "/PrivateChatHistory";
    if (!sendToGroup && fromID != -1 && !content.isEmpty() && !from.isEmpty()) {
      if (!Files.exists(Paths.get(messagePath + privateChat))) {
        String privateChatDir = messagePath + privateChat;
        File privateChatDirFile = new File(privateChatDir);
        privateChatDirFile.mkdir();
      }
      String privateChatLogName = messagePath + privateChat + "/" + fromID + "_" + toID + "_" + getCurrDate() + ".txt";
      File privateChatFile = new File(privateChatLogName);
      //Check if the private chat log file already exits
      if (!Files.exists(Paths.get(privateChatLogName))) {
        FileWriter myWriter = new FileWriter(privateChatFile);
        String log = "Chat log file created for sender: " + from + " and receiver: " + to;
        logger.log(Level.INFO, log);
        try {
          myWriter.write(toStringForPrivateChatLog());
        } catch (IOException e) {
          logger.log(Level.INFO, e.getMessage());
        } finally {
          myWriter.close();
        }
      } else {
        FileWriter myWriter = new FileWriter(privateChatFile, true);
        BufferedWriter br = new BufferedWriter(myWriter);
        try {
          br.newLine();
          br.write(toStringForPrivateChatLog());
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
   * Save group chat history under the Group folder by group id and group chat date
   */
  public void saveChatLogGroup(Group currentGroupObject, boolean sendToGroup) throws IOException {
    String group = "/Group";
    if (sendToGroup && fromID != -1 && !content.isEmpty() && !from.isEmpty()) {
      if (!Files.exists(Paths.get(messagePath + group))) {
        String groupDir = messagePath + group;
        File groupDirFile = new File(groupDir);
        groupDirFile.mkdir();
      }
      String groupChatLogName = messagePath + group + "/" + currentGroupObject.getGroupId() + "_" + getCurrDate() + ".txt";
      File groupChatFile = new File(groupChatLogName);
      //check if the chat log file already exists
      if (!Files.exists(Paths.get(groupChatLogName))) {
        FileWriter myWriter = new FileWriter(groupChatFile);
        logger.info("Chat log file created for " + currentGroupObject.getName());
        try {
          myWriter.write(toStringForGroupChatLog());
        } catch (IOException e) {
          logger.log(Level.INFO, e.getMessage());
        } finally {
          myWriter.close();
        }
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
      try {
        Handler fh = new FileHandler("~/message.log");
        logger.addHandler(fh);
      } catch (IOException | SecurityException e2) {
        e2.printStackTrace();
      }
      return message;
    }
  }
}
