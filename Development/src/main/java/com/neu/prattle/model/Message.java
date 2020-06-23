package com.neu.prattle.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
  private String message;

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

  @Column
  private boolean sendToGroup;

  @Column
  private String Date;


  /**
   * Initiate a new message.
   */
  public Message() {

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
    return message;
  }

  /***
   * Set message content
   */
  public void setContent(String message) {
    this.message = message;
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
  public String getDate() {
    DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /***
   * Retrieve current Date
   */
  public void setDate(String Date) {
    this.Date = Date;
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
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public int getGroupId() {
    return groupId;
  }
}
