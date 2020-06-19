package com.neu.prattle.service;

import com.neu.prattle.exceptions.AlreadyFollowException;
import com.neu.prattle.exceptions.FollowNotFoundException;
import com.neu.prattle.exceptions.NoPrivilegeException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.api.FollowAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service. Services
 * including user follow user and user follow group.
 */
public class FollowService {

  private UserService userService;
  private GroupService groupService;
  private static FollowService followService;
  private FollowAPI api;

  static {
    followService = new FollowService();
  }

  /**
   * FollowService is a Singleton class.
   */
  private FollowService() {

    api = new FollowAPI();

    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
  }

  /**
   * Set follow api to be used by this service.
   * @param followAPI
   */
  public void setAPI(FollowAPI followAPI) {
    api = followAPI;
  }

  /**
   * Set userService to be used by this service.
   * @param service
   */
  public void setUserService(UserService service) {
    userService = service;
  }

  /**
   * Set groupService to be used by this service.
   * @param service
   */
  public void setGroupService(GroupService service) {
    groupService = service;
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
    boolean b = false;
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
      b = true;
    }
    return b;
  }

  public boolean followUser(int user1Id, int user2Id) {
    User user1 = userService.findUserById(user1Id);
    User user2 = userService.findUserById(user2Id);
    return followUser(user1, user2);
  }

  /**
   * User1 unfollows user2. Return true if successfully unfollowed.
   * @param user1 the follower
   * @param user2 the followee
   * @return true if unfollow is successful
   */
  public boolean unfollowUser(User user1, User user2) {
    boolean b = false;
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
      b = true;
    }
    return b;
  }

  public boolean unfollowUser(int user1Id, int user2Id) {
    User user1 = userService.findUserById(user1Id);
    User user2 = userService.findUserById(user2Id);
    return unfollowUser(user1, user2);
  }

  /**
   * Get a list of users that the specific user follows.
   * @param user the specific user
   * @return the list of users the user follows
   */
  public List<User> getFollowingUsers(User user) {
    List<User> list = new ArrayList<>();
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      list = api.getFollowingUsers(userId);
    }
    return list;
  }

  public List<User> getFollowingUsers(int userId) {
    return api.getFollowingUsers(userId);
  }

  /**
   * Get follower list for the given user.
   * @param user the given user
   * @return the list of the user's followers
   */
  public List<User> userGetFollowers(User user) {
    List<User> list = new ArrayList<>();
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      list = api.userGetFollowers(userId);
    }
    return list;
  }

  public List<User> userGetFollowers(int userId) {
    return api.userGetFollowers(userId);
  }

  /**
   * User follows group. Return true if successfully followed.
   * @param user the user
   * @param group the group
   * @return true if follow is successful
   */
  public boolean followGroup(User user, Group group) {
    boolean b = false;
    Optional<User> optionalUser = userService.findUserByName(user.getName());
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    if(optionalGroup.isPresent() && optionalUser.isPresent()) {
      User u = optionalUser.get();
      Group g = optionalGroup.get();
      if(g.getPassword() != null) {
        throw new NoPrivilegeException("The group is a private group and can not be followed.");
      }
      List<Group> list = followService.getFollowingGroups(u);
      if(list.contains(g)) {
        throw new AlreadyFollowException(String.format("User %s already followed group %s.", u.getName(), g.getName()));
      }
      api.userFollowGroup(u.getUserId(), g.getGroupId());
      b = true;
    }
    return b;
  }

  public boolean followGroup(int userId, int groupId) {
    User user = userService.findUserById(userId);
    Group group = groupService.getGroupById(groupId);
    return followGroup(user, group);
  }

  /**
   * User unfollows group. Return true if successfully unfollowed.
   * @param user the user
   * @param group the group
   * @return true if unfollow is successful
   */
  public boolean unfollowGroup(User user, Group group) {
    boolean b = false;
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
      b = true;
    }
    return b;
  }

  public boolean unfollowGroup(int userId, int groupId) {
    User user = userService.findUserById(userId);
    Group group = groupService.getGroupById(groupId);
    return unfollowGroup(user, group);
  }

  /**
   * Get a list of groups that the specific user follows.
   * @param user the specific user
   * @return the list of groups the user follows
   */
  public List<Group> getFollowingGroups(User user) {
    List<Group> list = new ArrayList<>();
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      list = api.getFollowingGroups(userId);
    }
    return list;
  }

  public List<Group> getFollowingGroups(int userId) {
    return api.getFollowingGroups(userId);
  }

  /**
   * Get follower list for the given group.
   * @param group the given group
   * @return the list of the group's followers
   */
  public List<User> groupGetFollowers(Group group) {
    List<User> list = new ArrayList<>();
    Optional<Group> optional = groupService.findGroupByName(group.getName());
    if(optional.isPresent()) {
      int groupId = optional.get().getGroupId();
      list = api.groupGetFollowers(groupId);
    }
    return list;
  }
}
