package com.neu.prattle.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Message")
public class Message {

  /**
   * The id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int messageId;

  /**
   * The message
   */
  @Column
  private String messageText;

  /**
   * The sender name should be unique.
   */
  @Column(unique = true)
  private String from;

  /**
   * The receiver name should be unique.
   */
  @Column(unique = true)
  private String to;

  /**
   * The group id should be unique.
   */
  @Column(unique = true)
  private int groupId;

  /**
   * The group id should be unique.
   */
  @Column(unique = true)
  private String timeStamp;

  /**
   * Whether the message is send to group
   */
  @Column
  private boolean sendToGroup;

  /**
   * Date of the message
   */
  @Column
  private String messageDate;

  public static MessageBuilder messageBuilder() {
    return new MessageBuilder();
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
    return messageText;
  }

  /***
   * Set message content
   */
  public void setContent(String message) {
    this.messageText = message;
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
  public String getMessageDate() {
    return messageDate;
  }

  /***
   * Set current Date
   */
  public void setDate() {
    DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    Date date = new Date();
    this.messageDate = dateFormat.format(date);
  }

  /***
   * Set current Date
   */
  public void setMessageDate(String messageDate) {
    this.messageDate = messageDate;
  }

  /***
   * Retrieve the time stamp of the current message
   */
  public String getTimeStamp() {
    return timeStamp;
  }

  /***
   * Set the time stamp of the current message
   */
  public void setTimeStamp()
  {
    DateTimeFormatter mdy = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    this.timeStamp = mdy.format(now);
  }

  /***
   * Set the time stamp of the current message
   */
  public void setTimeStamp(String ts)
  {
    this.timeStamp = ts;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public int getGroupId() {
    return groupId;
  }

    public String toString() {
    return new StringBuilder()
            .append("From: ").append(from)
            .append("To: ").append(to)
            .append("Content: ").append(messageText)
            .toString();
  }

  public String toStringForPrivateChatLog() {
    return new StringBuilder()
            .append(from).append(": ")
            .append(messageText).append("   ")
            .append(timeStamp)
            .toString();
  }

  public String toStringForGroupChatLog() {
    return new StringBuilder()
            .append(from).append(": ")
            .append(messageText).append("   ")
            .append(timeStamp)
            .toString();
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
