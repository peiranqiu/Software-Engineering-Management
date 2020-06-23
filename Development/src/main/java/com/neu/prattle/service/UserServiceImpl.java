package com.neu.prattle.service;

import com.neu.prattle.exceptions.PasswordInvalidException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNameInvalidException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.api.APIFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public class UserServiceImpl implements UserService {
  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static UserService userService;

  static {
    userService = new UserServiceImpl();
  }

  private APIFactory api;

  /***
   * UserServiceImpl is a Singleton class.
   */
  private UserServiceImpl() {
    api = APIFactory.getInstance();
  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static UserService getInstance() {
    return userService;
  }

  /**
   * Set the api useed by user Service.
   *
   */
  @Override
  public void setAPI(APIFactory newAPIFactory) {
    api = newAPIFactory;
  }

  @Override
  public Optional<User> findUserByName(String name) {
    Optional<User> optional = Optional.empty();
    try {
      if (api.getUser(name) != null) {
        optional = Optional.of(api.getUser(name));
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return optional;
  }

  @Override
  public User findUserById(int id) {
    User user = new User();
    try {
      user = api.getUser(id);
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return user;
  }

  @Override
  public List<User> getAllUsers() {
    List<User> allUsers = new ArrayList<>();
    try {
      allUsers = api.getAllUsers();
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return allUsers;
  }

  @Override
  public boolean addUser(User user) {

    if (!isValidUsername(user)) {
      throw new UserNameInvalidException("Username must be between 4-20 letters long, and contain one capital letter, " +
              "one lowercase letter and one number.");
    }
    if (!isValidPassword(user)) {
      throw new PasswordInvalidException("Password must be between 4-20 letters long, and contain one capital letter, " +
              "one lowercase letter and one number.");
    }

    try {
      api.create(user);
      return true;
    } catch (IllegalStateException e) {
      throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getName()));
    }
  }

  @Override
  public User updateUser(User user, String field) {
    User u = null;
    try {
      User newUser = api.getUser(user.getName());
      if (newUser == null) {
        throw new UserNotFoundException(String.format("User %s not found.", user.getName()));
      }
      newUser.setPassword(user.getPassword());
      if (field.equals("password")) {
        u = api.updateUser(newUser, field, user.getPassword());
      } else if (field.equals("avatar")) {
        u = api.updateUser(newUser, field, user.getAvatar());
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return u;
  }

  @Override
  public User setModerator(User user) {
    try {
      user = api.setModerator(user);
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return user;
  }

  @Override
  public User setWatched(int userId) {
    User user = null;
    try {
      user = api.setWatched(userId);
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return user;
  }

  /**
   * Check if the username is valid based on the requirement.
   *
   * @param user User object
   * @return Returns true if username is valid
   */
  public boolean isValidUsername(User user) {
    return !(user.getName().length() > 20 || user.getName().length() < 4);
  }

  /**
   * Check if the password is valid based on the requirement.
   *
   * @param user user object
   * @return Returns true if password is valid
   */
  public boolean isValidPassword(User user) {
    return !(user.getPassword().length() > 20 || user.getPassword().length() < 4);
  }

}
