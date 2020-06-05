package com.neu.prattle.service;


import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupNotFoundException;
import com.neu.prattle.model.Group;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GroupServiceImpl implements GroupService {
  private Set<Group> groupSet = new HashSet<>();
  private GroupAPI api = new GroupAPI();

  /***
   * UserServiceImpl is a Singleton class.
   */

  private GroupServiceImpl() {

  }

  private static GroupService groupService;

  static {
    groupService = new GroupServiceImpl();
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
   * @param id The id of group
   * @return Optional object.
   */
  @Override
  public synchronized Optional<Group> findGroupById(Integer id) throws GroupNotFoundException {
    final Group group = new Group();
    group.setGroupId(id);
    if (api.getGroup(id))
      return Optional.of(group);
    else
      return Optional.empty();
  }

  /***
   * Tries to add a group in the system
   * @param group group object
   *
   */


  /***
   * Tries to add a group in the system
   * @param group group object
   *
   */
  @Override
  public void addGroup(Group group) {
    if (groupSet.contains(group))
      throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
    try {
      api.addGroup(group);
    } catch (IllegalStateException e) {
      throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
    }

  }
}
