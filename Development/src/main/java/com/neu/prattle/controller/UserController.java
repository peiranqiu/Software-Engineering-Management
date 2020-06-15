package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
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
import javax.ws.rs.core.MediaType;


/***
 * A Resource class responsible for handling CRUD operations on User objects.
 *
 */

@Path(value = "/user")
public final class UserController {

  private UserService userService = UserServiceImpl.getInstance();
  private FollowService followService = FollowService.getInstance();
  private ModerateService moderateService = ModerateService.getInstance();
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
  @Path("/getAllUser")
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

  /**
   * Get followers of a user
   * @return followers.
   */
  @GET
  @Path("/getFollower")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getFollower(User user){
    List<User> list = followService.userGetFollowers(user);
    return new Gson().toJson(list);
  }

  /**
   * Get followees of a user
   * @return followees.
   */
  @GET
  @Path("/getFollowee")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getFollowedUser(User user){
    List<User> list = followService.getFollowingUsers(user);
    return new Gson().toJson(list);
  }

  /**
   * Get list of groups the user has
   * @return groups
   */
  @GET
  @Path("/getHasGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getHasGroup(User user){
    List<Group> list = moderateService.getHasGroups(user);
    return new Gson().toJson(list);
  }

  /**
   * Get list of groups the user has
   * @return groups
   */
  @GET
  @Path("/getFollowedGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getFollowedGroup(User user){
    List<Group> list = followService.getFollowingGroups(user);
    return new Gson().toJson(list);
  }

  /**
   * follow a user
   */
  @POST
  @Path("/follow")
  @Consumes(MediaType.APPLICATION_JSON)
  public String followUser(JsonObject follow){
    Object follower = follow.get("follower");
    Object followee = follow.get("followee");
    if(follower instanceof User && followee instanceof User) {
      if(followService.followUser((User) follower, (User) followee)) {
        return new Gson().toJson("Follow successful");
      }
    }
    return new Gson().toJson("Follow failed");
  }

  /**
   * unollow a user
   */
  @DELETE
  @Path("/unfollow")
  @Consumes(MediaType.APPLICATION_JSON)
  public String unfollowUser(JsonObject follow){
    Object follower = follow.get("follower");
    Object followee = follow.get("followee");
    if(follower instanceof User && followee instanceof User) {
      if(followService.unfollowUser((User) follower, (User) followee)) {
        return new Gson().toJson("Unfollow successful");
      }
    }
    return new Gson().toJson("Unfollow failed");
  }
}