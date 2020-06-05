package com.neu.prattle.service;

import com.neu.prattle.model.Group;

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

}
