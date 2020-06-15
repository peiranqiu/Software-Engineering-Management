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
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/***
 * A Resource class responsible for handling CRUD operations on Group objects.
 *
 */
@Controller
@Path(value = "/group")
public class GroupController {

  private GroupService groupService = GroupServiceImpl.getInstance();
  private ModerateService moderateService = ModerateService.getInstance();
  private UserService userService = UserServiceImpl.getInstance();
  private FollowService followService = FollowService.getInstance();
  private static final GroupController groupController = new GroupController();

  /**
   * Singleton instance for user controller
   * @return a singleton instance
   */
  public static GroupController getInstance(){
    return groupController;
  }

  /***
   * Handles a HTTP POST request for group creation
   *
   * @param group -> The Group object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createGroup(Group group) {
    try {
      if(groupService.addGroup(group)) {
        return new Gson().toJson(group);
      }
    } catch (GroupAlreadyPresentException e) {
      return new Gson().toJson("Group Already Present");
    }
    return null;
  }

  /**
   * follow a group
   */
  @POST
  @Path("/{userId}/follow/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String followGroup(@PathParam("userId") int userId, @PathParam("groupId") int groupId){
    if(followService.followGroup(userId, groupId)) {
      return new Gson().toJson("Follow successful");
    }
    return new Gson().toJson("Follow failed");
  }

  /**
   * unollow a group
   */
  @DELETE
  @Path("/{userId}/unfollow/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String unfollowGroup(@PathParam("userId") int userId, @PathParam("groupId") int groupId){
    if(followService.unfollowGroup(userId, groupId)) {
      return new Gson().toJson("Unfollow successful");
    }
    return new Gson().toJson("Unfollow failed");
  }

  /**
   * add group moderator
   */
  @POST
  @Path("/moderator/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addModerator(JsonObject obj){
    Object user = obj.get("user");
    Object group = obj.get("group");
    Object moderator = obj.get("moderator");
    if(user instanceof User && moderator instanceof User && group instanceof Group) {
      User newModerator = moderateService.addGroupModerator((Group) group, (User) moderator, (User) user);
      return new Gson().toJson(newModerator);
    }
    return new Gson().toJson("Add moderator failed");
  }


  /**
   * remove group moderator
   */
  @DELETE
  @Path("/moderator/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteModerator(JsonObject obj){
    Object user = obj.get("user");
    Object group = obj.get("group");
    Object moderator = obj.get("moderator");
    if(user instanceof User && moderator instanceof User && group instanceof Group) {
      if(moderateService.deleteGroupModerator((Group) group, (User) moderator, (User) user)) {
        return new Gson().toJson("Delete moderator successful");
      }
    }
    return new Gson().toJson("Delete moderator failed");
  }


  /**
   * add group member
   */
  @POST
  @Path("/member/add")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addMember(JsonObject obj){
    Object user = obj.get("user");
    Object group = obj.get("group");
    Object moderator = obj.get("moderator");

    if(user instanceof User && moderator instanceof User && group instanceof Group) {
      if(moderateService.addGroupMember((Group) group, (User) moderator, (User) user)) {
        return new Gson().toJson("Add member successful");
      }
    }
    return new Gson().toJson("Add member failed");
  }

  /**
   * delete group member
   */
  @DELETE
  @Path("/member/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteMember(JsonObject obj){
    Object user = obj.get("user");
    Object group = obj.get("group");
    Object moderator = obj.get("moderator");
    if(user instanceof User && moderator instanceof User && group instanceof Group) {
      if(moderateService.deleteGroupMember((Group) group, (User) moderator, (User) user)) {
        return new Gson().toJson("Delete member successful");
      }
    }
    return new Gson().toJson("Delete member failed");
  }
}
