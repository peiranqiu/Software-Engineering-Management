//package com.neu.prattle.controller;
//
//import com.google.gson.Gson;
//
//import com.neu.prattle.model.Group;
//import com.neu.prattle.model.Message;
//import com.neu.prattle.model.User;
//import com.neu.prattle.service.FollowService;
//import com.neu.prattle.service.MessageService;
//import com.neu.prattle.service.MessageServiceImpl;
//import com.neu.prattle.service.ModerateService;
//import com.neu.prattle.service.UserService;
//import com.neu.prattle.service.UserServiceImpl;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.core.MediaType;
//
///***
// * A message controller class to handle http requests.
// */
//
//@Path(value = "/messages")
//public final class MessageController {
//
//  private static final MessageController messageController = new MessageController();
//  private MessageService messageService = MessageServiceImpl.getInstance();
//
//  /**
//   * Singleton instance for message controller
//   *
//   * @return a singleton instance
//   */
//  public static MessageController getInstance() {
//    return messageController;
//  }
//
//  /**
//   * Set user service for the message controller
//   *
//   * @param service message service
//   */
//  public void setMessageService(MessageService service) {
//    messageService = service;
//  }
//
//  /**
//   * Get all messages
//   *
//   * @return all messages
//   */
//  @GET
//  @Path("/messages/getAllMessages/{from}/{to}")
//  @Consumes(MediaType.APPLICATION_JSON)
//  public String getAllPrivateMessages(@PathParam("from") String from, @PathParam("to") String to) {
//    return new Gson().toJson(messageService.getAllPrivateMessages(from, to));
//  }
//
//
//}
