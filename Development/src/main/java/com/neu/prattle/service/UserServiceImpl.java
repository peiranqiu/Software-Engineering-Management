package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public class UserServiceImpl implements UserService {

  private final String URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=EST5EDT";
  private final String USER = "mydb";
  private final String PASSWORD = "CS5500team4";


  private UserAPI api = new UserAPI();
  private static UserService userService;
  private Set<User> userSet;

  /***
   * UserServiceImpl is a Singleton class.
   */
  private UserServiceImpl() {
    userSet = new HashSet<>();

  }

  static {
    userService = new UserServiceImpl();
  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static UserService getInstance() {
    return userService;
  }

  @Override
  public synchronized Optional<User> findUserByName(String name) {
    final User user = new User(name);
    if (api.getUsers(name))
      return Optional.of(user);
    else
      return Optional.empty();
  }

  @Override
  public synchronized void addUser(User user) throws UserAlreadyPresentException {
    try {
      api.addUser(user);
    } catch (IllegalStateException e) {
      throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getName()));
    }
  }

  @Override
  public void updateUser(User user) throws UserNotFoundException {

  }

  @Override
  public void userFollowUser(User follower, User followee) {

  }

}
