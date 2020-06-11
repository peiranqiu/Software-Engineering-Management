package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service. Services
 * including mainly user relationship with moderated groups.
 */
public class ModerateService {

  private static UserService userService;
  private static GroupService groupService;
  private static ModerateService moderateService;
  private ModerateAPI api;

  static {
    moderateService = new ModerateService();
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
  }

  /**
   * ModerateService is a Singleton class.
   */
  private ModerateService() {
    api = new ModerateAPI();
  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static ModerateService getInstance() {
    return moderateService;
  }

  /**
   * Add a moderator to a group
   * @param group the group
   * @param moderator the moderator who executes this operation
   * @param user the user to be added as moderator
   * @return the moderator if add success
   */
  public User addGroupModerator(Group group, User moderator, User user) {

    List<User> moderators = getModerators(group);
    if(moderators.size() > 0) {
      if(!moderator.getModerator() || moderators.contains(user)) {
        return null;
      }
    }
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    Optional<User> optionalUser = userService.findUserByName(user.getName());
    if(optionalGroup.isPresent() && optionalUser.isPresent()) {
      Group g = optionalGroup.get();
      User u = optionalUser.get();
      if(!u.getModerator()) {
        u.setModerator(true);
        userService.setModerator(u);
      }
      if(api.addModerator(g.getGroupId(), u.getUserId())) {
        return u;
      }
    }
    return null;
  }

  /**
   * Downgrade a moderator to member in a group
   * @param group the group
   * @param moderator the moderator who executes this operation
   * @param user the user to be deleted from moderator
   * @return true if downgrade success
   */
  public boolean deleteGroupModerator(Group group, User moderator, User user) {
    if(!moderator.getModerator()) {
      return false;
    }
    List<User> moderators = getModerators(group);
    if(moderators.size() == 1 || !moderators.contains(user)) {
      return false;
    }
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    Optional<User> optionalUser = userService.findUserByName(user.getName());
    if(optionalGroup.isPresent() && optionalUser.isPresent()) {
      Group g = optionalGroup.get();
      User u = optionalUser.get();
      if(getModerateGroups(u).size() == 1) {
        u.setModerator(false);
        userService.setModerator(u);
      }
      return api.deleteModerator(g.getGroupId(), u.getUserId());
    }
    return false;
  }

  /**
   * Delete a member from group.
   * @param group the group
   * @param moderator the moderator who executes this operation
   * @param user the user to delete
   * @return true if delete is successful
   */
  public boolean deleteGroupMember(Group group, User moderator, User user) {
    if(!moderator.getModerator()) {
      return false;
    }
    List<User> moderators = getModerators(group);
    if(moderators.contains(user)) {
      return false;
    }
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    Optional<User> optionalUser = userService.findUserByName(user.getName());
    if(optionalGroup.isPresent() && optionalUser.isPresent()) {
      Group g = optionalGroup.get();
      User u = optionalUser.get();
      return api.deleteMember(g.getGroupId(), u.getUserId());
    }
    return false;
  }

  /**
   * Add a member to a group
   * @param group the group
   * @param moderator the moderator
   * @param member the member
   * @return true if add member success
   */
  public boolean addGroupMember(Group group, User moderator, User member) {
    if(!moderator.getModerator()) {
      return false;
    }
    List<User> members = getMembers(group);
    if(members.contains(member)) {
      return false;
    }
    Optional<Group> optionalGroup = groupService.findGroupByName(group.getName());
    Optional<User> optionalMember = userService.findUserByName(member.getName());
    if(optionalGroup.isPresent() && optionalMember.isPresent()) {
      Group g = optionalGroup.get();
      User u = optionalMember.get();
      return api.addMember(g.getGroupId(), u.getUserId());
    }
    return false;
  }

  /**
   * Get moderators for a given group.
   * @param group the group
   * @return its moderator list
   */
  public List<User> getModerators(Group group) {
    Optional<Group> optional = groupService.findGroupByName(group.getName());
    if(optional.isPresent()) {
      int groupId = optional.get().getGroupId();
      return api.getModerators(groupId);
    }
    return new ArrayList<>();
  }

  /**
   * Get members for a given group
   * @param group the group
   * @return its member list
   */
  public List<User> getMembers(Group group) {
    Optional<Group> optional = groupService.findGroupByName(group.getName());
    if(optional.isPresent()) {
      int groupId = optional.get().getGroupId();
      return api.getMembers(groupId);
    }
    return new ArrayList<>();
  }

  /**
   * Get groups that the given user moderates
   * @param user the user
   * @return its moderated group list
   */
  public List<Group> getModerateGroups(User user) {
    Optional<User> optional = userService.findUserByName(user.getName());
    if(optional.isPresent()) {
      int userId = optional.get().getUserId();
      return api.getModerateGroups(userId);
    }
    return new ArrayList<>();
  }
}
