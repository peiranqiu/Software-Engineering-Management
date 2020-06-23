package com.neu.prattle.service;

import com.mysql.cj.protocol.MessageSender;
import com.neu.prattle.model.Message;
import com.neu.prattle.service.api.FollowAPI;
import com.neu.prattle.service.api.GroupAPI;
import com.neu.prattle.service.api.MessageAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageServiceImpl implements MessageService {

  private static MessageService messageService;

  static {
    messageService = new MessageServiceImpl();
  }

  private MessageAPI api = new MessageAPI();
  private Logger logger = Logger.getLogger(this.getClass().getName());

  /***
   * MessageServiceImpl is a Singleton class.
   */

  private MessageServiceImpl() {
  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */

  public static MessageService getInstance() {
    return messageService;
  }

  /**
   * Set the api used by Message Service.
   *
   * @param messageAPI message api
   */
  @Override
  public void setAPI(MessageAPI messageAPI) {
    api = messageAPI;
  }


  @Override
  public void deleteMessage(int messageId) {

  }
}
