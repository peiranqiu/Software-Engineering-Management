package com.neu.prattle.service;

import com.neu.prattle.exceptions.PasswordInvalidException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNameInvalidException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;

import java.sql.SQLException;
import java.util.HashMap;
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

  private UserAPI api;

  /***
   * UserServiceImpl is a Singleton class.
   */
  private UserServiceImpl() {

    api = new UserAPI();
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
  public Optional<User> findUserByName(String name) {
    Optional<User> optional = Optional.empty();
    try {
      if (api.getUserByName(name) != null) {
        optional = Optional.of(api.getUserByName(name));
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return optional;
  }

  /**
   * Add a user
   *
   * @param user User object
   * @return if add successfully
   */

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
      api.addUser(user);
      return true;
    } catch (IllegalStateException e) {
      throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getName()));
    }
  }

  /***
   * Update user password.
   *
   * @param user The user to update
   * @return the updated user
   */
  @Override
  public User updateUser(User user, String field) {
    User u = null;
    try {
      User newUser = api.getUserByName(user.getName());
      if (newUser == null) {
        throw new UserNotFoundException(String.format("User %s not found.", user.getName()));
      }
      newUser.setPassword(user.getPassword());
      if(field.equals("password")) {
        String value = user.getPassword();
        u = api.updateUser(newUser, field, value);
      }
      else if(field.equals("avatar")) {
        String value = user.getAvatar();
        u = api.updateUser(newUser, field, value);
      }
    } catch (SQLException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
    return u;
  }

  /**
   * Set the user's role as or not as a moderator.
   */
  @Override
  public User setModerator(User user) {
    try {
      user = api.setModerator(user);
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
    HashMap<String, Boolean> usernameCheck = checkRequirement(user.getName());
    return !(!usernameCheck.get("low") || !usernameCheck.get("cap") || !usernameCheck.get("num") || user.getName().length() > 20 ||
            user.getName().length() < 4);
  }

  /**
   * Check if the password is valid based on the requirement.
   *
   * @param user user object
   * @return Returns true if password is valid
   */
  public boolean isValidPassword(User user) {
    HashMap<String, Boolean> passwordCheck = checkRequirement(user.getPassword());
    return !(!passwordCheck.get("low") || !passwordCheck.get("cap") || !passwordCheck.get("num") || user.getPassword().length() > 20 ||
            user.getPassword().length() < 4);
  }

  /**
   * Check if a given string meets all the requirement.
   *
   * @param string the string to be checked
   * @return Returns true if it is valid
   * @author CS5500 Teaching staff
   */
  private HashMap<String, Boolean> checkRequirement(String string) {
    char character;
    HashMap<String, Boolean> booleanHashMap = new HashMap<>();
    booleanHashMap.put("cap", false);
    booleanHashMap.put("low", false);
    booleanHashMap.put("num", false);
    for (int i = 0; i < string.length(); i++) {
      character = string.charAt(i);
      if (Character.isDigit(character)) {
        booleanHashMap.replace("num", true);
      } else if (Character.isUpperCase(character)) {
        booleanHashMap.replace("cap", true);
      } else if (Character.isLowerCase(character)) {
        booleanHashMap.replace("low", true);
      }
      if (Boolean.TRUE.equals(booleanHashMap.get("num")) && Boolean.TRUE.equals(booleanHashMap.get("cap")) && Boolean.TRUE.equals(booleanHashMap.get("low"))) {
        break;
      }
    }
    return booleanHashMap;
  }

}
