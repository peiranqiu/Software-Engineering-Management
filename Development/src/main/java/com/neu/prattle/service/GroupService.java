package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.service.api.FollowAPI;
import com.neu.prattle.service.api.GroupAPI;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GroupService {
  /***
   * Acts as an interface between the data layer and the
   * servlet controller.
   *
   * The controller is responsible for interfacing with this instance
   * to perform all the CRUD operations on user accounts.
   *
   *
   */
  /***
   * Returns an optional object which might be empty or wraps an object
   * if the System contains a {@link Group} object having the same name
   * as the parameter.
   *
   * @param name The name of group
   * @return Group object.
   */
  Optional<Group> findGroupByName(String name);

  /***
   * Tries to add a group in the system
   * @param group group object
   * @return true of successful, false otherwise.
   *
   */
  boolean addGroup(Group group);

  /***
   * method to set password for a group so that it can be private group
   * @param groupId groupId
   * @param password password
   */

  boolean setPasswordforGroup(int groupId, String password);


  /**
   * method to add subgroup into a group
   * @param groupId group id
   * @param subGroupId subgroup id
   * @throws SQLException if groupId or subgroupId not exist.
   */
  boolean addSubgroupIntoGroup(int groupId, int subGroupId);


  /**
   * a method to get sub groups of one group by group id
   * @param groupId
   * @return a list of groups
   * @throws SQLException
   */

  List<Group> getSubGroupList(int groupId);

  /**
   * get group by Id
   *
   * @param id group id
   * @return the group
   */
  Group getGroupById(int id);

  /**
   * Set the user api used by this service
   * @param api
   */
  void setAPI(GroupAPI api);

  /**
   * Set follow api to be used by this service
   * @param newFollowAPI
   */
  void setFollowAPI(FollowAPI newFollowAPI);

  /**
   * a method to get all groups in the database
   *
   * @return a list of groups
   */

  List<Group> getAllGroups();

}
