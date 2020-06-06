package com.neu.prattle.service;


import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupNotFoundException;
import com.neu.prattle.model.Group;

import java.sql.SQLException;
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
  public synchronized Optional<Group> findGroupByName(String name) {
    final Group group = new Group(name);
    Optional<Group> optional = Optional.empty();
    try {
      if (api.getGroup(name))
        optional = Optional.of(group);
      else throw new GroupNotFoundException("there is no such group");
    } catch (SQLException e) {
      e.printStackTrace();
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
}
