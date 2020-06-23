package com.neu.prattle.controller;

import com.google.gson.Gson;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/***
 * A group controller class to handle http requests.
 *
 */
@Controller
@Path(value = "/group")
public class GroupController {
  private static final GroupController groupController = new GroupController();
  private GroupService groupService = GroupServiceImpl.getInstance();
  private ModerateService moderateService = ModerateService.getInstance();
  private FollowService followService = FollowService.getInstance();

  /**
   * Singleton instance for group controller
   *
   * @return a singleton instance
   */
  public static GroupController getInstance() {
    return groupController;
  }

  /**
   * Set group service for the group controller
   * @param service group service
   */
  public void setGroupService(GroupService service) {
    groupService = service;
  }

  /**
   * Set moderate service for the group controller
   * @param service moderate service
   */
  public void setModerateService(ModerateService service) {
    moderateService = service;
  }

  /**
   * Set follow service for the group controller
   * @param service follow service
   */
  public void setFollowService(FollowService service) {
    followService = service;
  }

  /***
   * create a group
   *
   * @param group the group
   * @return the created group
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createGroup(Group group) {
    if (groupService.addGroup(group)) {
      return new Gson().toJson(group);
    }
    return null;
  }

  /**
   * follow a group
   *
   * @param userId  the user id
   * @param groupId the group id
   */
  @POST
  @Path("/{userId}/follow/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String followGroup(@PathParam("userId") int userId, @PathParam("groupId") int groupId) {
    if (followService.followGroup(userId, groupId)) {
      return new Gson().toJson("Follow successful");
    }
    return new Gson().toJson("Follow failed");
  }

  /**
   * unfollow a group
   *
   * @param userId  the user id
   * @param groupId the group id
   */
  @DELETE
  @Path("/{userId}/unfollow/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String unfollowGroup(@PathParam("userId") int userId, @PathParam("groupId") int groupId) {
    if (followService.unfollowGroup(userId, groupId)) {
      return new Gson().toJson("Unfollow successful");
    }
    return new Gson().toJson("Unfollow failed");
  }

  /**
   * add group moderator
   *
   * @param userId  moderator id
   * @param groupId group id
   */
  @POST
  @Path("/{userId}/moderate/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addModerator(@PathParam("userId") int userId, @PathParam("groupId") int groupId) {

    User newModerator = moderateService.addGroupModerator(groupId, userId);
    if (newModerator != null) {
      return new Gson().toJson("Add moderator succeed");
    }
    return new Gson().toJson("Add moderator failed");
  }

  /**
   * add group member
   *
   * @param userId  user id
   * @param groupId group id
   */
  @POST
  @Path("/{userId}/member/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addMember(@PathParam("userId") int userId, @PathParam("groupId") int groupId) {
    if (moderateService.addGroupMember(groupId, userId)) {
      return new Gson().toJson("Add member succeed");
    }
    return new Gson().toJson("Add member failed");
  }


  /**
   * Get all groups in database
   *
   * @return all groups in database
   */
  @GET
  @Path("/getAllGroups")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getAllGroups() {
    return new Gson().toJson(groupService.getAllGroups());
  }

  /**
   * Get all subgroups in input group
   *
   * @return all subgroups in input group
   */
  @GET
  @Path("/{groupId}/getSubGroups")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getSubGroups(@PathParam("groupId") int id) {
    return new Gson().toJson(groupService.getSubGroupList(id));
  }

  /**
   * add subGroup into group
   *
   * @param groupId    super group id
   * @param subGroupId sub group id
   */
  @POST
  @Path("/{groupId}/add/{subGroupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addSubGroup(@PathParam("groupId") int groupId, @PathParam("subGroupId") int subGroupId) {
    if (groupService.addSubgroupIntoGroup(groupId, subGroupId)) {
      return new Gson().toJson("Adding subGroup successful");
    }
    return new Gson().toJson("Adding subGroup failed");
  }

  /**
   * Get group by name
   *
   * @return group that match name
   */
  @GET
  @Path("/{groupName}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getGroupbyName(@PathParam("groupName") String name) {
    Optional<Group> optional = groupService.findGroupByName(name);
    if(optional.isPresent()) {
      return new Gson().toJson(optional.get());
    }
    return new Gson().toJson(null);
  }

  /**
   * set Group Password
   *
   * @param groupId super group id
   */
  @POST
  @Path("/{groupId}/password")
  @Consumes(MediaType.APPLICATION_JSON)
  public String setGroupPass(@PathParam("groupId") int groupId, String password) {
    if (groupService.setPasswordforGroup(groupId, password)) {
      return new Gson().toJson("set password successful");
    }
    return new Gson().toJson("set password failed");
  }


  /**
   * Get group followers by group id
   *
   * @return all followes in input group
   */
  @GET
  @Path("/{groupId}/followers")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getGroupFollowers(@PathParam("groupId") int id) {
    Group group = groupService.getGroupById(id);
    List<User> list = followService.groupGetFollowers(group);
    return new Gson().toJson(list);
  }


  /**
   * Get group moderators by group id
   *
   * @return all moderators in input group
   */
  @GET
  @Path("/{groupId}/moderators")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getGroupModerators(@PathParam("groupId") int id) {
    Group group = groupService.getGroupById(id);
    List<User> list = moderateService.getModerators(group);
    return new Gson().toJson(list);
  }

  /**
   * Get group members by group id
   *
   * @return all members in input group
   */
  @GET
  @Path("/{groupId}/members")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getGroupMembers(@PathParam("groupId") int id) {
    Group group = groupService.getGroupById(id);
    List<User> list = moderateService.getMembers(group);
    return new Gson().toJson(list);
  }
  /**
   * Get all invitations of the group
   * @param id group id
   * @return
   */
  @GET
  @Path("/{groupId}/getAllInvitation")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getGroupInvitations(@PathParam("groupId") int id) {
    Map<User, Boolean> invitations = moderateService.getGroupInvitations(id);
    return new Gson().toJson(invitations.keySet());
  }

  /***
   * create an invitation
   *
   * @param groupId the group id
   * @param userId the invitee id
   * @return the created invitation
   */
  @POST
  @Path("/{groupId}/createInvitation/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createInvitation(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
    if (moderateService.createInvitation(groupId, userId)) {
      return new Gson().toJson("Invitation created successfully");
    }
    return new Gson().toJson("Creating invitation failed");
  }

  /***
   * delete an invitation
   *
   * @param groupId the group id
   * @param userId the invitee id
   */
  @DELETE
  @Path("/{groupId}/deleteInvitation/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteInvitation(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
    if (moderateService.deleteInvitation(groupId, userId)) {
      return new Gson().toJson("Invitation deleted successfully");
    }
    return new Gson().toJson("Deleting invitation failed");
  }

  /***
   * approve an invitation
   *
   * @param groupId the group id
   * @param userId the invitee id
   */
  @POST
  @Path("/{groupId}/approveInvitation/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String approveInvitation(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
    if (moderateService.approveInvitation(groupId, userId)) {
      return new Gson().toJson("Invitation approved");
    }
    return new Gson().toJson("Invitation not approved. Please try again.");
  }

}



