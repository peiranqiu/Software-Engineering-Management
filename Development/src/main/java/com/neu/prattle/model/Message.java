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
 * A Basic POJO for Message.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
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

    private int fromID = -1;

    private int toID = -1;

    @Override
    public String toString() {
        return new StringBuilder()
                .append("From: ").append(from)
                .append("To: ").append(to)
                .append("Content: ").append(content)
                .toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessageID() {this.messageID = UUID.randomUUID().toString();}

    public String getMessageID() {return messageID;}

    public void setMessageDate() {
        DateTimeFormatter mdy = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        messageDate = mdy.format(now);
    }

    public String getMessageDate() {return messageDate;}

    public void setFromID(int fromID) {this.fromID = fromID;}

    public int getFromID() {return fromID;}

    public int setToID(int toID) {this.toID = toID;}

    public int getToID() {return toID;}

    public void makeDirectory(String messagePath, int userID) {
        File fromUserFile = new File(messagePath + "/" + userID);
        fromUserFile.mkdir();
        File fileUser = new File(messagePath + "/" + userID + "/messageSent");
        fileUser.mkdir();
        File fileSender = new File(messagePath + "/" + userID +"/messageReceived");
        fileSender.mkdir();
    }

    public boolean storeMessage() throws IOException, EncodeException {
        if (fromID != -1 && toID != -1 && !content.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
            String path = Message.class.getResource("").getPath();
            String mainPath = path.substring(0, path.indexOf("main") + 4);
            String messagePath = mainPath + "/messages";
            if (!Files.exists(Paths.get(messagePath + "/" + fromID))){
                makeDirectory(messagePath, fromID);
            }
            if (!Files.exists(Paths.get(messagePath + "/" + toID))){
                makeDirectory(messagePath, toID);
            }
            File file1 = new File(messagePath + "/" + fromID + "messageSent" + "/" + messageID + ".txt");
            File file2 = new File(messagePath + "/" + toID  + "messageReceived" + "/" + messageID + ".txt");
            if (file1.createNewFile() && file2.createNewFile()) {
                writeFile(messagePath);
                return true;
            }
        }
        return false;
    }

    private void writeFile(String messagePath) throws IOException, EncodeException {
        MessageEncoder msEncoder = new MessageEncoder();
        FileWriter myWriter = new FileWriter(messagePath + "/" + fromID + "messageSent" + "/" + messageID + ".txt");
        myWriter.write(msEncoder.encode(this));
        myWriter.close();
        MessageEncoder msEncoder2 = new MessageEncoder();
        FileWriter myWriter2 = new FileWriter(messagePath + "/" + toID  + "messageReceived" + "/" + messageID + ".txt");
        myWriter2.write(msEncoder2.encode(this));
        myWriter2.close();
    }

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
