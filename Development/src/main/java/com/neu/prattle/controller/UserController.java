package com.neu.prattle.controller;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/***
 * A Resource class responsible for handling CRUD operations on User objects.
 *
 */

@Path(value = "/user")
public final class UserController {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private UserService userService = UserServiceImpl.getInstance();
  private static final UserController userController = new UserController();

  /**
   * Singleton instance for user controller
   * @return a singleton instance
   */
  public static UserController getInstance(){
    return userController;
  }

  /***
   * Handles a HTTP POST request for user creation
   *
   * @param user -> The User object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createUserAccount(User user) {
    try {
      if(userService.addUser(user)) {
        return new Gson().toJson(user);
      }
    } catch (UserAlreadyPresentException e) {
      return new Gson().toJson("User Already Present");
    }
    return null;
  }

  /**
   * Get all users
   * @return all users.
   */
  @GET
  @Path("/getAll")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getAllUsers(){
    List<User> list = userService.getAllUsers();
    return new Gson().toJson(list);
  }

  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  public String login(User user) {
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      User u = optional.get();
      if(u.getPassword().equals(user.getPassword())) {
        return new Gson().toJson(u);
      }
    }
    return new Gson().toJson(null);
  }

}