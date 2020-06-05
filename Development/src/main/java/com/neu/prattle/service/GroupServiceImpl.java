package com.neu.prattle.service;


import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupNotFoundException;
import com.neu.prattle.model.Group;


public class GroupServiceImpl implements GroupService {
  private GroupAPI api = new GroupAPI();
  private static GroupService groupService;

  /***
   * UserServiceImpl is a Singleton class.
   */

  private GroupServiceImpl() {

  }

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
  public synchronized Group findGroupById(Integer id) throws GroupNotFoundException {
    final Group group = new Group();
    group.setGroupId(id);
    if (api.getGroup(id) != null)
      return group;
    else
      return null;
  }


  /***
   * Tries to add a group in the system
   * @param group group object
   *
   */
  @Override
  public void addGroup(Group group) throws GroupAlreadyPresentException {
    try {
      api.addGroup(group);
    } catch (IllegalStateException e) {
      throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
    }

  }
}
