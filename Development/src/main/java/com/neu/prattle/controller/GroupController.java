package com.neu.prattle.controller;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/***
 * A Resource class responsible for handling CRUD operations on Group objects.
 *
 */


@Controller
@Path(value = "/group")
public class GroupController {
  private static final GroupController groupControllerInstance = new GroupController();
  private GroupService groupService = GroupServiceImpl.getInstance();

  /***
   * Handles a HTTP POST request for group creation
   *
   * @param group -> The Group object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createGroupAccount(Group group) {
    try {
      groupService.addGroup(group);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }

    return Response.ok().build();
  }

}
