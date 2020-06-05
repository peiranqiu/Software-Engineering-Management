package com.neu.prattle.model;

import com.neu.prattle.websocket.MessageEncoder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.websocket.EncodeException;

/***
 * The Message class built to get the necessary message attributes and message information
 *
 * @author Yijia Hao
 * @version dated 2020-06-05
 */
public class Message {
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
    public void setMessageID() {this.messageID = UUID.randomUUID().toString();}

    /***
     * Retrieve id of message
     */
    public String getMessageID() {return messageID;}

    /***
     * Retrieve date of message
     */
    public String getMessageDate() {return messageDate;}

    /***
     * Set id of message sender
     */
    public void setFromID(int fromID) {this.fromID = fromID;}

    /***
     * Retrieve id of message sender
     */
    public int getFromID() {return fromID;}

    /***
     * Set id of message receiver
     */
    public void setToID(int toID) {this.toID = toID;}

    /***
     * Retrieve id of message receiver
     */
    public int getToID() {return toID;}

  /***
   * Get the parent directory of the message folder
   */
  public String getMessagePath() {
    String path = Message.class.getResource("").getPath();
    String mainPath = path.substring(0, path.indexOf("Development") + 11);
    String messagePath = mainPath + "/src";
    return messagePath;
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
  public void makeDirectory(String messagePath, int userID) {
    File fromUserFile = new File(messagePath + "/" + userID);
    if (fromUserFile.mkdir()) {
      System.out.println("Successfully create user directory.");
    } else {
      System.out.println("Creating user directory fails.");
    }
    File fileUserSent = new File(messagePath + "/" + userID + "/messageSent");
    if (fileUserSent.mkdir()) {
      System.out.println("Successfully create sender directory.");
    } else {
      System.out.println("Creating sender directory fails.");
    }
    File fileUserReceived = new File(messagePath + "/" + userID +"/messageReceived");
    if (fileUserReceived.mkdir()) {
      System.out.println("Successfully create receiver directory.");
    } else {
      System.out.println("Creating receiver directory fails.");
    }
    }

    /***
     * Make directories and save the current message into a JSON file under the folder of the current sender and receiver
     */
    public boolean storeMessage() throws IOException, EncodeException {
        if (fromID != -1 && toID != -1 && !content.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
            messagePath = getMessagePath();
            if (!Files.exists(Paths.get(messagePath + "/" + fromID))){
                makeDirectory(messagePath, fromID);
            }
            if (!Files.exists(Paths.get(messagePath + "/" + toID))){
                makeDirectory(messagePath, toID);
            }
            File file1 = new File(messagePath + "/" + fromID + "/messageSent" + "/" + messageID + ".json");
            File file2 = new File(messagePath + "/" + toID  + "/messageReceived" + "/" + messageID + ".json");
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
        FileWriter myWriter = new FileWriter(messagePath + "/" + fromID + "/messageSent" + "/" + messageID + ".json");
        myWriter.write(msEncoder.encode(this));
        myWriter.close();
        MessageEncoder msEncoder2 = new MessageEncoder();
        FileWriter myWriter2 = new FileWriter(messagePath + "/" + toID  + "/messageReceived" + "/" + messageID + ".json");
        myWriter2.write(msEncoder2.encode(this));
        myWriter2.close();
    }

    /***
     * Remove the current message sent by sender
     */
    public String deleteMessage(int userID, String messageID) {
        messagePath = getMessagePath();
        String output = "File remove fails.";
        File file = new File(messagePath + "/" + userID + "/messageSent" + "/" + messageID + ".json");
        if(file.delete())
        {
            output = "File deleted successfully";
        }
        return output;
    }

    /***
     * Create an object of MessageBuilder class
     */
    public static MessageBuilder messageBuilder()   {
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

        public MessageBuilder()    {
            message = new Message();
            message.setFrom("Not set");
        }

        public MessageBuilder setFrom(String from)    {
            message.setFrom(from);
            return this;
        }

        public MessageBuilder setTo(String to)    {
            message.setTo(to);
            return this;
        }

        public MessageBuilder setMessageContent(String content)   {
            message.setContent(content);
            return this;
        }

        public Message build()  {
            return message;
        }
    }
}
