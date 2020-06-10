package com.neu.prattle.service;

import com.neu.prattle.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.text.html.Option;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service. Services
 * including user follow user and user follow group.
 */
public class FollowService {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static UserService userService;
  private static GroupService groupService;
  private static FollowService followService;
  private FollowAPI api;

  static {
    followService = new FollowService();
  }

  /***
   * FollowService is a Singleton class.
   */
  private FollowService() {
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
    api = new FollowAPI();
  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static FollowService getInstance() {
    return followService;
  }

  /**
   * User1 follows user2. Return true if successfully followed.
   * @param user1 the follower
   * @param user2 the followee
   * @return true if follow is successful
   */
  public boolean followUser(User user1, User user2) {
    User A = userService.findUserByName(user1.getName()).get();
    User B = userService.findUserByName(user2.getName()).get();
    api.UserFollowUser(A.getUserId(), B.getUserId());
    return true;
  }

  /**
   * User1 unfollows user2. Return true if successfully unfollowed.
   * @param user1 the follower
   * @param user2 the followee
   * @return true if unfollow is successful
   */
  public boolean unfollowUser(User user1, User user2) {
    User A = userService.findUserByName(user1.getName()).get();
    User B = userService.findUserByName(user2.getName()).get();
    api.UserUnfollowUser(A.getUserId(), B.getUserId());
    return true;
  }

  /**
   * Get a list of users that the specific user follows.
   * @param user the specific user
   * @return the list of users the user follows
   */
  public List<User> getFollowedUsers(User user) {
    int userId = userService.findUserByName(user.getName()).get().getUserId();
    return api.getFollowedUsers(userId);
  }

  /**
   * Get follower list for the given user.
   * @param user the given user
   * @return the list of the user's followers
   */
  public List<User> getFollowers(User user) {
    int userId = userService.findUserByName(user.getName()).get().getUserId();
    return api.getFollowers(userId);
  }



}
