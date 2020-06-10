package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.List;
import java.util.logging.Logger;

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

  /**
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
  public List<User> getFollowingUsers(User user) {
    int userId = userService.findUserByName(user.getName()).get().getUserId();
    return api.getFollowingUsers(userId);
  }

  /**
   * Get follower list for the given user.
   * @param user the given user
   * @return the list of the user's followers
   */
  public List<User> userGetFollowers(User user) {
    int userId = userService.findUserByName(user.getName()).get().getUserId();
    return api.userGetFollowers(userId);
  }

  /**
   * User follows group. Return true if successfully followed.
   * @param user the user
   * @param group the group
   * @return true if follow is successful
   */
  public boolean followGroup(User user, Group group) {
    User u = userService.findUserByName(user.getName()).get();
    Group g = groupService.findGroupByName(group.getName()).get();
    api.userFollowGroup(u.getUserId(), g.getGroupId());
    return true;
  }

  /**
   * User unfollows group. Return true if successfully unfollowed.
   * @param user the user
   * @param group the group
   * @return true if unfollow is successful
   */
  public boolean unfollowGroup(User user, Group group) {
    User u = userService.findUserByName(user.getName()).get();
    Group g = groupService.findGroupByName(group.getName()).get();
    api.userUnfollowGroup(u.getUserId(), g.getGroupId());
    return true;
  }

  /**
   * Get a list of groups that the specific user follows.
   * @param user the specific user
   * @return the list of groups the user follows
   */
  public List<Group> getFollowingGroups(User user) {
    int userId = userService.findUserByName(user.getName()).get().getUserId();
    return api.getFollowingGroups(userId);
  }

  /**
   * Get follower list for the given group.
   * @param group the given group
   * @return the list of the group's followers
   */
  public List<User> groupGetFollowers(Group group) {
    int groupId = groupService.findGroupByName(group.getName()).get().getGroupId();
    return api.groupGetFollowers(groupId);
  }
}
