package com.neu.prattle.controller;

import com.google.gson.Gson;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.springframework.stereotype.Controller;

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
 * A group controller class to handle http requests.
 *
 */
@Controller
@Path(value = "/group")
public class GroupController {
  private UserService userService = UserServiceImpl.getInstance();
  private GroupService groupService = GroupServiceImpl.getInstance();
  private ModerateService moderateService = ModerateService.getInstance();
  private FollowService followService = FollowService.getInstance();
  private static final GroupController groupController = new GroupController();

  private static final String GROUP = "group";
  private static final String MODERATOR = "moderator";
  private static final String USER = "user";

  /**
   * Singleton instance for group controller
   *
   * @return a singleton instance
   */
  public static GroupController getInstance() {
    return groupController;
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
    try {
      if (groupService.addGroup(group)) {
        return new Gson().toJson(group);
      }
    } catch (GroupAlreadyPresentException e) {
      return new Gson().toJson("Group Already Present");
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
   * @param userId moderator id
   * @param groupId group id
   * @return
   */
  @POST
  @Path("/{userId}/moderate/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addModerator(@PathParam("userId") int userId, @PathParam("groupId") int groupId) {

    User newModerator = moderateService.addGroupModerator(groupId, userId);
    if(newModerator != null) {
      return new Gson().toJson("Add moderator succeed");
    }
    return new Gson().toJson("Add moderator failed");
  }

  /**
   * add group member
   * @param userId user id
   * @param groupId group id
   * @return
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
   * @return all groups in database
   */
  @GET
  @Path("/getAllGroups")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getAllGroups(){
    List<Group> list = groupService.getAllGroups();
    return new Gson().toJson(list);
  }

  /**
   * Get all subgroups in input group
   * @return all subgroups in input group
   */
  @GET
  @Path("/{groupId}/getSubGroups")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getSubGroups(@PathParam("groupId") int id){
    List<Group> list = groupService.getSubGroupList(id);
    return new Gson().toJson(list);
  }

  /**
   * add subGroup into group
   * @param groupId super group id
   * @param subGroupId sub group id
   * @return
   */
  @POST
  @Path("/{groupId}/add/{subGroupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addSubGroup(@PathParam("groupId") int groupId, @PathParam("subGroupId") int subGroupId) {
    if (groupService.addSubgroupIntoGroup(groupId,subGroupId)) {
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
    Optional group = groupService.findGroupByName(name);
    return new Gson().toJson(group);
  }

}
