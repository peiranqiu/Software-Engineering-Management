package com.neu.prattle.service;

import com.neu.prattle.service.api.MessageAPI;

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
  /**
   * remove the message in database by messageId
   */
  void deleteMessage(int messageId);
}
