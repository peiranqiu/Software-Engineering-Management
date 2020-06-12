package com.neu.prattle.service;


import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupNotFoundException;
import com.neu.prattle.model.Group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupServiceImpl implements GroupService {
  private static GroupService groupService;
  static {
    groupService = new GroupServiceImpl();
  }

  private GroupAPI api = new GroupAPI();

  /***
   * UserServiceImpl is a Singleton class.
   */

  private GroupServiceImpl() {

  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */

  public static GroupService getInstance() {
    return groupService;
  }


  /***
   * Returns an optional object which might be empty or wraps an object
   * if the System contains a {@link Group} object having the same name
   * as the parameter.
   *
   * @param name The name of group
   * @return Optional object.
   */
  @Override
  public Optional<Group> findGroupByName(String name) {
    Optional<Group> optional = Optional.empty();
    try {
      if (api.getGroup(name) != null) {
        optional = Optional.of(api.getGroup(name));
      }
    } catch (SQLException e) {
      // do something
    }
    return optional;
  }


  /***
   * Tries to add a group in the system
   * @param group group object
   * @return true of successful, false otherwise.
   *
   */
  @Override
  public boolean addGroup(Group group) {
    try {
      api.addGroup(group);
      return true;
    } catch (IllegalStateException e) {
      throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
    }

  }

  /***
   * method to set password for a group so that it can be private group
   * @param groupId groupId
   * @param password password
   */
  @Override
  public boolean setPasswordforGroup(int groupId, String password) {
    try {
      api.setPasswordforGroup(groupId, password);
    } catch (SQLException e) {
      throw new GroupNotFoundException(String.format("Group not found"));
    }
    return true;
  }

  /**
   * method to add subgroup into a group
   *
   * @param groupId    group id
   * @param subGroupId subgroup id
   * @throws SQLException if groupId or subgroupId not exist.
   */
  @Override
  public boolean addSubgroupIntoGroup(int groupId, int subGroupId) {
    try {
      api.addSubgroupIntoGroup(groupId, subGroupId);
    } catch (SQLException e) {
      throw new GroupNotFoundException(String.format("Group not found"));
    }
    return true;
  }

  /**
   * a method to get sub groups of one group by group id
   *
   * @return a list of groups
   */
  @Override
  public List<Group> getSubGroupList(int groupId) {
    List<Group> groups = new ArrayList<>();
    try{
      groups=api.getSubGroupList(groupId);
    }catch (SQLException e) {
      throw new GroupNotFoundException(String.format("Group not found"));
    }
    return groups;
  }

  /**
   * get group by Id
   *
   * @param id group id
   * @return the group
   */
  @Override
  public Group getGroupById(int id) {
    Group group=null;
    try{
      group=api.getGroupById(id);
    }catch (SQLException e){}
    return group;
  }
}
