package com.neu.prattle.service.api;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Factory class to operate on APIs.
 */
public class APIFactory {

  private static APIFactory instance = null;
  private UserAPI userAPI;
  private GroupAPI groupAPI;
  private FollowAPI followAPI;
  private ModerateAPI moderateAPI;

  /**
   * Construct an API factory.
   */
  public APIFactory() {
    userAPI = new UserAPI();
    groupAPI = new GroupAPI();
    followAPI = new FollowAPI();
    moderateAPI = new ModerateAPI();
  }

  /**
   * Get current instance of the class.
   */
  public static APIFactory getInstance() {
    if(instance == null) {
      instance = new APIFactory();
    }
    return instance;
  }

  /**
   * create a user.
   */
  public boolean create(User user) {
    return userAPI.addUser(user);
  }

  /**
   * create a group.
   */
  public boolean create(Group group) {
    return groupAPI.addGroup(group);
  }

  /**
   * create a user follow user
   */
  public boolean followUser(int user1Id, int user2Id) {
    return followAPI.userFollowUser(user1Id, user2Id);
  }

  /**
   * create a user follow group
   */
  public boolean followGroup(int userId, int groupId) {
    return followAPI.userFollowGroup(userId, groupId);
  }

  /**
   * delete a user follow user
   */
  public boolean unfollowUser(int user1Id, int user2Id) {
    return followAPI.userUnfollowUser(user1Id, user2Id);
  }

  /**
   * delete a user follow group
   */
  public boolean unfollowGroup(int userId, int groupId) {
    return followAPI.userUnfollowGroup(userId, groupId);
  }

  /**
   * Get all users
   */
  public List<User> getAllUsers() throws SQLException {
    return userAPI.getAllUsers();
  }

  /**
   * Get all groups
   */
  public List<Group> getAllGroups() throws SQLException {
    return groupAPI.getAllGroups();
  }

  /**
   * Get user by name.
   */
  public User getUser(String name) throws SQLException {
    return userAPI.getUserByName(name);
  }

  /**
   * Get user by id
   */
  public User getUser(int id) throws SQLException {
    return userAPI.getUserById(id);
  }

  /**
   * Get group by name.
   */
  public Group getGroup(String name) throws SQLException {
    return groupAPI.getGroup(name);
  }

  /**
   * Get user by id
   */
  public Group getGroup(int id) throws SQLException {
    return groupAPI.getGroupById(id);
  }

  /**
   * Update user
   */
  public User updateUser(User user, String field, String value) throws SQLException{
    return userAPI.updateUser(user, field, value);
  }

  /**
   * set moderator
   */
  public User setModerator(User user) throws SQLException{
    return userAPI.setModerator(user);
  }

  /**
   * Set is watched
   */
  public User setWatched(int userId) throws SQLException {
    return userAPI.setWatched(userId);
  }

  /**
   * set password for group
   */
  public boolean setGroupPassword(int groupId, String password) throws SQLException {
    return groupAPI.setPasswordforGroup(groupId, password);
  }

  /**
   * Get all subgroups
   */
  public List<Group> getAllSubgroups(int groupId) throws SQLException {
    return groupAPI.getSubGroupList(groupId);
  }

  /**
   * Add subgroup
   */
  public boolean addSubgroup(int groupId, int subgroupId) throws SQLException {
    return groupAPI.addSubgroupIntoGroup(groupId, subgroupId);
  }

  /**
   * User get followed users
   */
  public List<User> getFollowedUsers(int userId) {
    return followAPI.getFollowingUsers(userId);
  }

  /**
   * User get followed groups
   */
  public List<Group> getFollowedGroups(int userId) {
    return followAPI.getFollowingGroups(userId);
  }

  /**
   * User get followers
   */
  public List<User> userGetFollowers(int userId) {

    return followAPI.userGetFollowers(userId);
  }

  /**
   * Group get followers
   */
  public List<User> groupGetFollowers(int groupId) {
    return followAPI.groupGetFollowers(groupId);
  }

  /**
   * Add group moderator
   */
  public boolean addModerator(int groupId, int userId) {
    return moderateAPI.addModerator(groupId, userId);
  }

  /**
   * Add group member
   */
  public boolean addMember(int groupId, int userId) {
    return moderateAPI.addMember(groupId, userId);
  }

  /**
   * Remove group moderator
   */
  public boolean deleteModerator(int groupId, int userId) {
    return moderateAPI.deleteModerator(groupId, userId);
  }

  /**
   * Remove group member
   */
  public boolean deleteMember(int groupId, int userId) {
    return moderateAPI.deleteMember(groupId, userId);
  }

  /**
   * get all group moderators
   */
  public List<User> getModerators(int groupId) {
    return moderateAPI.getModerators(groupId);
  }

  /**
   * get all group members
   */
  public List<User> getMembers(int groupId) {
    return moderateAPI.getMembers(groupId);
  }

  /**
   * get groups in which the user is a moderator
   */
  public List<Group> getModerateGroups(int userId) {
    return moderateAPI.getModerateGroups(userId);
  }

  /**
   * get groups in which the user is a member
   */
  public List<Group> getHasGroups(int userId) {
    return moderateAPI.getHasGroups(userId);
  }

  /**
   * create invitation
   */
  public boolean createInvitation(int groupId, int userId, boolean isAdd) {
    return moderateAPI.createInvitation(groupId, userId, isAdd);
  }

  /**
   * delete invitation
   */
  public boolean deleteInvitation(int groupId, int userId) {
    return moderateAPI.deleteInvitation(groupId, userId);
  }

  /**
   * get invitations associated with a group
   */
  public Map<User, Boolean> getInvitations(int groupId) {
    return moderateAPI.getInvitationsOfGroup(groupId);
  }
}
