package com.neu.prattle.service;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.api.MessageAPI;

import java.util.List;

/***
 * Acts as an interface between the data layer and the
 * servlet controller.
 *
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on user accounts.
 *
 *
 */
public interface MessageService {
  /**
   * Set the message api used by this service
   */
  void setAPI(MessageAPI api);

  public List<Message> getAllPrivateMessages(String fromName, String toName);

  public boolean deleteMessage(String fromName, String toName, String timeStamp);

  public boolean addMessage(Message message);


}
