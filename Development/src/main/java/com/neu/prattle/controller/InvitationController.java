package com.neu.prattle.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.ModerateService;

import org.springframework.stereotype.Controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
   * @param groupId the group id
   * @param userId the invitee id
   * @return the created invitation
   */
  @POST
  @Path("/{groupId}/add/{userId}")
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
  @Path("/{groupId}/delete/{userId}")
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
  @Path("/{groupId}/approve/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String approveInvitation(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
    if (moderateService.approveInvitation(groupId, userId)) {
      return new Gson().toJson("Invitation approved");
    }
    return new Gson().toJson("Invitation not approved. Please try again.");
  }


  /**
   * Get all invitations of the group
   * @param id group id
   * @return
   */
  @GET
  @Path("/{groupId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String getGroupInvitations(@PathParam("groupId") int id) {
    Map<User, Boolean> invitations = moderateService.getGroupInvitations(id);
    return new Gson().toJson(invitations.keySet());
  }
}
