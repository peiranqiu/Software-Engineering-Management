package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.ModerateService;

import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/***
 * A controller class for invitation.
 */
@Controller
@Path(value = "/invitation")
public class InvitationController {

  private static final InvitationController invitationController = new InvitationController();
  private static final String GROUP = "group";
  private static final String INVITER = "inviter";
  private static final String INVITEE = "invitee";
  private ModerateService moderateService = ModerateService.getInstance();

  /**
   * Singleton instance for invitation controller
   *
   * @return a singleton instance
   */
  public static InvitationController getInstance() {
    return invitationController;
  }

  /***
   * create an invitation
   *
   * @param invitation the invitation
   * @return the created invitation
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public String createInvitation(JsonObject invitation) {
    Group group = (Group) ((Object) invitation.get(GROUP));
    User inviter = (User) ((Object) invitation.get(INVITER));
    User invitee = (User) ((Object) invitation.get(INVITEE));
    Boolean isInvite = (Boolean) ((Object) invitation.get("isInvite"));
    if (moderateService.createInvitation(group, inviter, invitee, isInvite)) {
      return new Gson().toJson("Invitation created successfully");
    }
    return new Gson().toJson("Creating invitation failed");
  }

  /***
   * delete an invitation
   *
   * @param invitation the invitation
   */
  @DELETE
  @Path("/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteInvitation(JsonObject invitation) {
    Group group = (Group) ((Object) invitation.get(GROUP));
    User inviter = (User) ((Object) invitation.get(INVITER));
    User invitee = (User) ((Object) invitation.get(INVITEE));
    if (moderateService.deleteInvitation(group, inviter, invitee)) {
      return new Gson().toJson("Invitation deleted successfully");
    }
    return new Gson().toJson("Deleting invitation failed");
  }

  /***
   * approve an invitation
   *
   * @param invitation the invitation
   */
  @POST
  @Path("/approve")
  @Consumes(MediaType.APPLICATION_JSON)
  public String approveInvitation(JsonObject invitation) {
    Group group = (Group) ((Object) invitation.get(GROUP));
    User inviter = (User) ((Object) invitation.get(INVITER));
    User invitee = (User) ((Object) invitation.get(INVITEE));
    Boolean isInvite = (Boolean) ((Object) invitation.get("isInvite"));
    if (moderateService.approveInvitation(group, inviter, invitee, isInvite)) {
      return new Gson().toJson("Invitation approved");
    }
    return new Gson().toJson("Invitation not approved. Please try again.");
  }
}
