package com.neu.prattle.service;

import com.neu.prattle.exceptions.NoPrivilegeException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.api.APIFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service. Services
 * including mainly user relationship with moderated groups.
 */
public class ModerateService {

  private static ModerateService moderateService;

  static {
    moderateService = new ModerateService();
  }

  private UserService userService;
  private GroupService groupService;
  private APIFactory api;

  /**
   * ModerateService is a Singleton class.
   */
  private ModerateService() {

    api = APIFactory.getInstance();
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
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
   * Set moderate api to be used by this service.
   */
  public void setAPI(APIFactory apiFactory) {
    api = apiFactory;
  }

  /**
   * Set userService to be used by this service.
   */
  public void setUserService(UserService service) {
    userService = service;
  }

  /**
   * Set groupService to be used by this service.
   */
  public void setGroupService(GroupService service) {
    groupService = service;
  }

  /**
   * Add a moderator to a group
   *
   * @param group     the group
   * @param moderator the moderator who executes this operation
   * @param user      the user to be added as moderator
   * @return the moderator if add success
   */
  public User addGroupModerator(Group group, User moderator, User user) {
    User result = null;
    List<User> moderators = getModerators(group);
    if (!moderators.isEmpty()) {
      checkModerator(group, moderator);
    }
    if (Boolean.FALSE.equals(user.getModerator())) {
      user.setModerator(true);
      userService.setModerator(user);
    }
    if (api.addModerator(group.getGroupId(), user.getUserId())) {
      result = user;
    }
    return result;
  }

  public User addGroupModerator(Integer groupId, Integer userId) {
    User result = null;
    User user = userService.findUserById(userId);
    user.setModerator(true);
    userService.setModerator(user);
    if (api.addModerator(groupId, userId)) {
      result = user;
    }
    return result;
  }


  /**
   * Downgrade a moderator to member in a group
   *
   * @param group     the group
   * @param moderator the moderator who executes this operation
   * @param user      the user to be deleted from moderator
   * @return true if downgrade success
   */
  public boolean deleteGroupModerator(Group group, User moderator, User user) {
    checkModerator(group, moderator);
    List<User> moderators = getModerators(group);
    if (moderators.size() == 1 && moderators.contains(user)) {
      throw new IllegalStateException("Cannot delete this moderator because this is the only moderator of the group.");
    }
    if (!moderators.contains(user)) {
      throw new IllegalStateException("The user to be removed is not a group moderator yet.");
    }
    if (getModerateGroups(user).size() == 1) {
      user.setModerator(false);
      userService.setModerator(user);
    }
    return api.deleteModerator(group.getGroupId(), user.getUserId());
  }

  /**
   * Delete a member from group.
   *
   * @param group     the group
   * @param moderator the moderator who executes this operation
   * @param user      the user to delete
   * @return true if delete is successful
   */
  public boolean deleteGroupMember(Group group, User moderator, User user) {
    checkModerator(group, moderator);
    List<User> moderators = getModerators(group);
    if (moderators.contains(user)) {
      throw new IllegalStateException("Cannot delete a group moderator.");
    }
    List<User> members = getMembers(group);
    if (!members.contains(user)) {
      throw new IllegalStateException("The user is not a member of the group yet.");
    }
    return api.deleteMember(group.getGroupId(), user.getUserId());
  }

  /**
   * Add a member to a group
   *
   * @param group     the group
   * @param moderator the moderator
   * @param member    the member
   * @return true if add member success
   */
  public boolean addGroupMember(Group group, User moderator, User member) {
    checkModerator(group, moderator);
    List<User> members = getMembers(group);
    if (members.contains(member)) {
      throw new IllegalStateException("The user to be added is already the group member.");
    }
    return api.addMember(group.getGroupId(), member.getUserId());
  }

  public boolean addGroupMember(Integer groupId, Integer userId) {
    return api.addMember(groupId, userId);

  }

  /**
   * Get moderators for a given group.
   *
   * @param group the group
   * @return its moderator list
   */
  public List<User> getModerators(Group group) {
    int groupId = group.getGroupId();
    return api.getModerators(groupId);
  }

  /**
   * Get members for a given group
   *
   * @param group the group
   * @return its member list
   */
  public List<User> getMembers(Group group) {
    int groupId = group.getGroupId();
    return api.getMembers(groupId);
  }

  public List<User> getMembers(int groupId) {
    return api.getMembers(groupId);
  }

  /**
   * Get groups that the given user moderates
   *
   * @param user the user
   * @return its moderated group list
   */
  public List<Group> getModerateGroups(User user) {
    int userId = user.getUserId();
    return api.getModerateGroups(userId);
  }

  /**
   * Get groups that the user is currently in
   *
   * @param user the user
   * @return the group list
   */
  public List<Group> getHasGroups(User user) {
    int userId = user.getUserId();
    return api.getHasGroups(userId);
  }

  public List<Group> getHasGroups(int userId) {
    return api.getHasGroups(userId);
  }

  /**
   * Member invites/deletes a user to a group by creating an invitation for the group moderator to
   * approve.
   *
   * @param group    the group
   * @param inviter  the inviter
   * @param invitee  the invitee
   * @param isInvite true if this is an add member invitation, false if this is a delete member
   *                 request
   * @return true if invitation created successfully
   */
  public boolean createInvitation(Group group, User inviter, User invitee, boolean isInvite) {
    List<User> members = getMembers(group);
    String operation = Boolean.TRUE.equals(isInvite) ? "invite" : "remove";
    if (!members.contains(inviter)) {
      throw new IllegalStateException("The current user is not a member of the group and cannot " + operation + " members.");
    }
    if (Boolean.TRUE.equals(isInvite) && members.contains(invitee)) {
      throw new IllegalStateException("The user to be added is already the group member.");
    }
    if (Boolean.FALSE.equals(isInvite) && !members.contains(invitee)) {
      throw new IllegalStateException("The user to be deleted is currently not the group member.");
    }
    return api.createInvitation(group.getGroupId(), invitee.getUserId(), isInvite);
  }

  public boolean createInvitation(int groupId, int inviteeId) {
    return api.createInvitation(groupId, inviteeId, true);
  }


  /**
   * Group moderator does not approve the invitation and deletes it.
   *
   * @param group     the group
   * @param moderator the moderator that executes this operation
   * @param invitee   the invitee
   * @return true if delete success
   */
  public boolean deleteInvitation(Group group, User moderator, User invitee) {
    checkModerator(group, moderator);
    return api.deleteInvitation(group.getGroupId(), invitee.getUserId());
  }

  public boolean deleteInvitation(int groupId, int inviteeId) {
    return api.deleteInvitation(groupId, inviteeId);
  }


  /**
   * Group moderator approves invitation and adds the invitee into group.
   *
   * @param group     the group
   * @param moderator the moderator executing this operation
   * @param invitee   the invitee
   * @return true if approve success
   */
  public boolean approveInvitation(Group group, User moderator, User invitee, boolean isInvite) {
    boolean b = false;
    if (deleteInvitation(group, moderator, invitee)) {
      if (Boolean.TRUE.equals(isInvite)) {
        b = addGroupMember(group, moderator, invitee);
      } else {
        b = deleteGroupMember(group, moderator, invitee);
      }
    }
    return b;
  }

  public boolean approveInvitation(int groupId, int inviteeId) {
    boolean b = false;
    if(deleteInvitation(groupId, inviteeId)) {
      b = addGroupMember(groupId, inviteeId);
    }
    return b;
  }

  /**
   * Get invitations of the group
   * @param groupId the group id
   * @return invitations of the group
   */
  public Map<User, Boolean> getGroupInvitations(int groupId) {
    return api.getInvitations(groupId);
  }


  /**
   * A user adds a subgroup into a group.
   *
   * @param group     the group
   * @param moderator the current user
   * @param subGroup  the subgroup
   * @return true if add success
   */
  public boolean addSubgroup(Group group, User moderator, Group subGroup) {
    checkModerator(group, moderator);
    groupService.addSubgroupIntoGroup(group.getGroupId(), subGroup.getGroupId());
    return true;
  }

  /**
   * Helper method to check if current user is moderator of the group.
   *
   * @param group     the group
   * @param moderator the user to be checked
   */
  public void checkModerator(Group group, User moderator) {

    List<User> moderators = getModerators(group);
    if (!moderators.contains(moderator)) {
      throw new NoPrivilegeException("The current user is not moderator of the group and cannot execute this operation.");
    }
  }

}
