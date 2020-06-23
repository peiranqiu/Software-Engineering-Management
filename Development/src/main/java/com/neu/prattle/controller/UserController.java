package com.neu.prattle.controller;

import com.google.gson.Gson;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.ModerateService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;


/***
 * A user controller class to handle http requests.
 */

@Path(value = "/user")
public final class UserController {

  private static final UserController userController = new UserController();
  private UserService userService = UserServiceImpl.getInstance();
  private FollowService followService = FollowService.getInstance();
  private ModerateService moderateService = ModerateService.getInstance();

  /**
   * Singleton instance for user controller
   *
   * @return a singleton instance
   */
  public static UserController getInstance() {
    return userController;
  }


  /**
   * Set user service for the user controller
   * @param service user service
   */
  public void setUserService(UserService service) {
    userService = service;
  }

  /**
   * Set moderate service for the user controller
   * @param service moderate service
   */
  public void setModerateService(ModerateService service) {
    moderateService = service;
  }

  /**
   * Set follow service for the user controller
   * @param service follow service
   */
  public void setFollowService(FollowService service) {
    followService = service;
  }

  /***
   * Handles a HTTP POST request for user creation
   *
   * @param user the user
   * @return the created user
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createUserAccount(User user) {
    if (userService.addUser(user)) {
      return new Gson().toJson(user);
    }
    return null;
  }

  /**
   * Get all users
   *
   * @return all users
   */
  @GET
  @Path("/getAllUser")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getAllUsers() {
    return new Gson().toJson(userService.getAllUsers());
  }

  /**
   * user login
   *
   * @param user the user
   * @return login-ed user
   */
  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  public String login(User user) {
    Optional<User> optional = userService.findUserByName(user.getName());
    if (optional.isPresent()) {
      User u = optional.get();
      if (u.getPassword().equals(user.getPassword())) {
        return new Gson().toJson(u);
      }
    }
    return new Gson().toJson(null);
  }

  /**
   * Government watches a user
   *
   * @param id the user id
   * @return user with updated watch status
   */
  @POST
  @Path("/{userId}/watch")
  @Consumes(MediaType.APPLICATION_JSON)
  public String watchUser(@PathParam("userId") int id) {
    User u = userService.setWatched(id);
    return new Gson().toJson(u);
  }

  /**
   * Get followers of a user
   *
   * @param id the user id
   * @return followers.
   */
  @GET
  @Path("/{userId}/getFollower")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getFollower(@PathParam("userId") int id) {
    List<User> list = followService.userGetFollowers(id);
    return new Gson().toJson(list);
  }

  /**
   * Get followees of a user
   *
   * @param id the user id
   * @return followees.
   */
  @GET
  @Path("/{userId}/getFollowee")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getFollowedUser(@PathParam("userId") int id) {
    List<User> list = followService.getFollowingUsers(id);
    return new Gson().toJson(list);
  }

  /**
   * Get list of groups the user has
   *
   * @param id the user id
   * @return groups
   */
  @GET
  @Path("/{userId}/getHasGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getHasGroup(@PathParam("userId") int id) {
    List<Group> list = moderateService.getHasGroups(id);
    return new Gson().toJson(list);
  }

  /**
   * Get list of groups the user is following
   *
   * @param id the user id
   * @return groups
   */
  @GET
  @Path("/{userId}/getFollowedGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getFollowedGroup(@PathParam("userId") int id) {
    List<Group> list = followService.getFollowingGroups(id);
    return new Gson().toJson(list);
  }

  /**
   * follow a user
   *
   * @param followerId the follower id
   * @param followeeId the followee id
   */
  @POST
  @Path("/{followerId}/follow/{followeeId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String followUser(@PathParam("followerId") int followerId, @PathParam("followeeId") int followeeId) {
    if (followService.followUser(followerId, followeeId)) {
      return new Gson().toJson("Follow successful");
    }
    return new Gson().toJson("Follow failed");
  }

  /**
   * unollow a user
   *
   * @param followerId the follower id
   * @param followeeId the followee id
   */
  @DELETE
  @Path("/{followerId}/unfollow/{followeeId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String unfollowUser(@PathParam("followerId") int followerId, @PathParam("followeeId") int followeeId) {
    if (followService.unfollowUser(followerId, followeeId)) {
      return new Gson().toJson("Unfollow successful");
    }
    return new Gson().toJson("Unfollow failed");
  }


  /**
   * Send a message to a user
   *
   * @param msg the message to ben sent
   */
  @POST
  @Path("/send")
  @Consumes(MediaType.APPLICATION_JSON)
  public String sendToUser(Message msg) {
    return new Gson().toJson(msg.getContent());
  }

}
