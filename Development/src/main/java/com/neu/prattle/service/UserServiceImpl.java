package com.neu.prattle.service;

import com.neu.prattle.exceptions.PasswordInvalidException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNameInvalidException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;

import java.util.HashMap;
import java.util.Optional;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public class UserServiceImpl implements UserService {

  private UserAPI api;
  private static UserService userService;

  /***
   * UserServiceImpl is a Singleton class.
   */
  private UserServiceImpl() {
    api = new UserAPI();
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
    if (api.getUsers(name) != null)
      return Optional.of(user);
    else
      return Optional.empty();
  }

  @Override
  public synchronized void addUser(User user) throws UserAlreadyPresentException {

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
  public User updateUser(User user) throws UserNotFoundException {
    User newUser = api.getUsers(user.getName());
    if (newUser == null) {
      throw new UserNotFoundException(String.format("User %s not found.", user.getName()));
    }
    newUser.setPassword(user.getPassword());
    String field = "password";
    String value = user.getPassword();
    return api.updateUser(newUser, field, value);
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
    return !(user.getPassword().length() < 4 || user.getPassword().length() > 20
            || !passwordCheck.get("low") || !passwordCheck.get("cap") || !passwordCheck.get("num"));
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
      if (booleanHashMap.get("num") && booleanHashMap.get("cap") && booleanHashMap.get("low")) {
        break;
      }
    }
    return booleanHashMap;
  }

}
