package com.neu.prattle.service;

import com.neu.prattle.exceptions.AlreadyFollowException;
import com.neu.prattle.exceptions.FollowNotFoundException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service. Services
 * including user follow user and user follow group.
 */
public class FollowService {

  private static UserService userService;
  private static GroupService groupService;
  private static FollowService followService;
  private FollowAPI api;

  static {
    followService = new FollowService();
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
  }

  /**
   * FollowService is a Singleton class.
   */
  private FollowService() {
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
    Optional<User> optionalA = userService.findUserByName(user1.getName());
    Optional<User> optionalB = userService.findUserByName(user2.getName());
    if(optionalA.isPresent() && optionalB.isPresent()) {
      User userA = optionalA.get();
      User userB = optionalB.get();
      List<User> list = followService.getFollowingUsers(user1);
      if(list.contains(userB)) {
        throw new AlreadyFollowException(String.format("User %s already followed user %s.", user1.getName(), user2.getName()));
      }
      api.userFollowUser(userA.getUserId(), userB.getUserId());
      return true;
    }
    return false;
  }

  /**
   * User1 unfollows user2. Return true if successfully unfollowed.
   * @param user1 the follower
   * @param user2 the followee
   * @return true if unfollow is successful
   */
  public boolean unfollowUser(User user1, User user2) {
    Optional<User> optionalA = userService.findUserByName(user1.getName());
    Optional<User> optionalB = userService.findUserByName(user2.getName());
    if(optionalA.isPresent() && optionalB.isPresent()) {
      User userA = optionalA.get();
      User userB = optionalB.get();
      List<User> list = followService.getFollowingUsers(user1);
      if(!list.contains(userB)) {
        throw new FollowNotFoundException(String.format("User %s has not followed user %s.", user1.getName(), user2.getName()));
      }
      api.userUnfollowUser(userA.getUserId(), userB.getUserId());
      return true;
    }
    return false;
  }

  /**
   * Get a list of users that the specific user follows.
   * @param user the specific user
   * @return the list of users the user follows
   */
  public List<User> getFollowingUsers(User user) {
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      return api.getFollowingUsers(userId);
    }
    return new ArrayList<>();
  }

  /**
   * Get follower list for the given user.
   * @param user the given user
   * @return the list of the user's followers
   */
  public List<User> userGetFollowers(User user) {
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      return api.userGetFollowers(userId);
    }
    return new ArrayList<>();
  }

  /**
   * User follows group. Return true if successfully followed.
   * @param user the user
   * @param group the group
   * @return true if follow is successful
   */
  public boolean followGroup(User user, Group group) {
    Optional<User> optionalUser = userService.findUserByName(user.getName());
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    if(optionalGroup.isPresent() && optionalUser.isPresent()) {
      User u = optionalUser.get();
      Group g = optionalGroup.get();
      List<Group> list = followService.getFollowingGroups(u);
      if(list.contains(g)) {
        throw new AlreadyFollowException(String.format("User %s already followed group %s.", u.getName(), g.getName()));
      }
      api.userFollowGroup(u.getUserId(), g.getGroupId());
      return true;
    }
    return false;
  }

  /**
   * User unfollows group. Return true if successfully unfollowed.
   * @param user the user
   * @param group the group
   * @return true if unfollow is successful
   */
  public boolean unfollowGroup(User user, Group group) {
    Optional<User> optionalUser = userService.findUserByName(user.getName());
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    if(optionalGroup.isPresent() && optionalUser.isPresent()) {
      User u = optionalUser.get();
      Group g = optionalGroup.get();
      List<Group> list = followService.getFollowingGroups(u);
      if(!list.contains(g)) {
        throw new FollowNotFoundException(String.format("User %s has not followed group %s.", u.getName(), g.getName()));
      }
      api.userUnfollowGroup(u.getUserId(), g.getGroupId());
      return true;
    }
    return false;
  }

  /**
   * Get a list of groups that the specific user follows.
   * @param user the specific user
   * @return the list of groups the user follows
   */
  public List<Group> getFollowingGroups(User user) {
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      return api.getFollowingGroups(userId);
    }
    return new ArrayList<>();
  }

  /**
   * Get follower list for the given group.
   * @param group the given group
   * @return the list of the group's followers
   */
  public List<User> groupGetFollowers(Group group) {
    Optional<Group> optional = groupService.findGroupByName(group.getName());
    if(optional.isPresent()) {
      int groupId = optional.get().getGroupId();
      return api.groupGetFollowers(groupId);
    }
    return new ArrayList<>();
  }
}
