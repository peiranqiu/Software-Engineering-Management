package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
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
import javax.ws.rs.core.MediaType;

/***
 * A controller class for invitation.
 *
 */
@Controller
@Path(value = "/invitation")
public class InvitationController {

  private GroupService groupService = GroupServiceImpl.getInstance();
  private ModerateService moderateService = ModerateService.getInstance();
  private UserService userService = UserServiceImpl.getInstance();
  private static final InvitationController invitationController = new InvitationController();

  /**
   * Singleton instance for invitation controller
   * @return a singleton instance
   */
  public static InvitationController getInstance(){
    return invitationController;
  }

  /***
   * Handles a HTTP POST request for invitation creation
   *
   * @param invitation -> The invitation object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createInvitation(JsonObject invitation) {
    Object group = invitation.get("group");
    Object inviter = invitation.get("inviter");
    Object invitee = invitation.get("invitee");
    Object isInvite = invitation.get("isInvite");
    if(group instanceof Group && inviter instanceof User
            && invitee instanceof User && isInvite instanceof Boolean) {
      if(moderateService.createInvitation((Group) group, (User) inviter, (User) invitee, (Boolean) isInvite)) {
        return new Gson().toJson("Invitation created successfully");
      }
    }
    return new Gson().toJson("Creating invitation failed");
  }

  /***
   * Handles a HTTP POST request for invitation deletion
   *
   * @param invitation -> The invitation object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @DELETE
  @Path("/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteInvitation(JsonObject invitation) {
    Object group = invitation.get("group");
    Object inviter = invitation.get("inviter");
    Object invitee = invitation.get("invitee");
    if(group instanceof Group && inviter instanceof User && invitee instanceof User) {
      if(moderateService.deleteInvitation((Group) group, (User) inviter, (User) invitee)) {
        return new Gson().toJson("Invitation deleted successfully");
      }
    }
    return new Gson().toJson("Deleting invitation failed");
  }

  /***
   * Handles a HTTP POST request to approve an invitation
   *
   * @param invitation -> The invitation object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @POST
  @Path("/approve")
  @Consumes(MediaType.APPLICATION_JSON)
  public String approveInvitation(JsonObject invitation) {
    Object group = invitation.get("group");
    Object inviter = invitation.get("inviter");
    Object invitee = invitation.get("invitee");
    Object isInvite = invitation.get("isInvite");
    if(group instanceof Group && inviter instanceof User
            && invitee instanceof User && isInvite instanceof Boolean) {
      if(moderateService.approveInvitation((Group) group, (User) inviter, (User) invitee, (Boolean) isInvite)) {
        return new Gson().toJson("Invitation approved");
      }
    }
    return new Gson().toJson("Invitation not approved. Please try again.");
  }
}
