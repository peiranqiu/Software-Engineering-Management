package com.neu.prattle.service;

import com.neu.prattle.Repositories.GroupRepo;
import com.neu.prattle.model.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
  @Autowired
  GroupRepo groupRepo;

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
  public Group findGroupById(Integer id) {
    Optional<Group> found = groupRepo.findById(id);
    return found.orElse(null);
  }

  /***
   * Tries to add a group in the system
   * @param group group object
   *
   */

  public void addGroup(Group group) {
    groupRepo.save(group);
  }
}
