package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;

import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
   * unollow a group
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
   * @param obj the json object contains user group and moderator information
   */
  @POST
  @Path("/moderator/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addModerator(JsonObject obj) {
    User user = (User) ((Object) obj.get(USER));
    Group group = (Group) ((Object) obj.get(GROUP));
    User moderator = (User) ((Object) obj.get(MODERATOR));
    User newModerator = moderateService.addGroupModerator(group, moderator, user);
    if(newModerator != null) {
      return new Gson().toJson(newModerator);
    }
    return new Gson().toJson("Add moderator failed");
  }


  /**
   * remove group moderator
   *
   * @param obj the json object contains user group and moderator information
   */
  @DELETE
  @Path("/moderator/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteModerator(JsonObject obj) {
    User user = (User) ((Object) obj.get(USER));
    Group group = (Group) ((Object) obj.get(GROUP));
    User moderator = (User) ((Object) obj.get(MODERATOR));
    if (moderateService.deleteGroupModerator(group, moderator, user)) {
      return new Gson().toJson("Delete moderator successful");
    }
    return new Gson().toJson("Delete moderator failed");
  }


  /**
   * add group member
   *
   * @param obj the json object contains user group and moderator information
   */
  @POST
  @Path("/member/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addMember(JsonObject obj) {
    User user = (User) ((Object) obj.get(USER));
    Group group = (Group) ((Object) obj.get(GROUP));
    User moderator = (User) ((Object) obj.get(MODERATOR));
    if (moderateService.addGroupMember(group, moderator, user)) {
      return new Gson().toJson("Add member successful");
    }
    return new Gson().toJson("Add member failed");
  }

  /**
   * delete group member
   *
   * @param obj the json object contains user group and moderator information
   */
  @DELETE
  @Path("/member/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteMember(JsonObject obj) {
    User user = (User) ((Object) obj.get(USER));
    Group group = (Group) ((Object) obj.get(GROUP));
    User moderator = (User) ((Object) obj.get(MODERATOR));
    if (moderateService.deleteGroupMember(group, moderator, user)) {
      return new Gson().toJson("Delete member successful");
    }
    return new Gson().toJson("Delete member failed");
  }
}
