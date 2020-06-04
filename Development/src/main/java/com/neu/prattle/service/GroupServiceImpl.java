package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.Group;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GroupServiceImpl implements GroupService {

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

  private Set<Group> groupSet = new HashSet<>();


  /***
   * Returns an optional object which might be empty or wraps an object
   * if the System contains a {@link Group} object having the same name
   * as the parameter.
   *
   * @param id The id of group
   * @return Optional object.
   */
  @Override
  public Optional<Group> findGroupById(Integer id) {
    Group group = new Group(id);
    if (groupSet.contains(group)) {
      return Optional.of(group);
    }
    return Optional.empty();
  }

  /***
   * Tries to add a group in the system
   * @param group group object
   *
   */
  @Override
  public void addGroup(Group group) {

    if (groupSet.contains(group)) {
      throw new UserAlreadyPresentException(String.format("User already present with id: %d", group.getGroupId()));
    }
    groupSet.add(group);
  }
}
