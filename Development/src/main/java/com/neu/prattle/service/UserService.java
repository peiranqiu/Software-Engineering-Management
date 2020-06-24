package com.neu.prattle.service;

import com.neu.prattle.model.User;
import com.neu.prattle.service.api.APIFactory;

import java.util.List;
import java.util.Optional;

/***
 * Acts as an interface between the data layer and the
 * servlet controller.
 *
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on user accounts.
 *
 */
public interface UserService {

  /***
   * Returns the user associated with the username.
   *
   * @param name The name of the user
   * @return the user if found.
   */
  Optional<User> findUserByName(String name);

  /***
   * Returns the user associated with the user id.
   *
   * @param id The id of the user
   * @return the user if found.
   */
  User findUserById(int id);

  /***
   * Update user profile.
   *
   * @param user The user to update
   * @param field the specific field to update
   * @return the updated user
   */
  User updateUser(User user, String field);

  /***
   * Create a new user.
   *
   * @param user User object
   *
   */
  boolean addUser(User user);

  /**
   * Update the user's role as or not as a moderator.
   *
   * @param user the user to update
   * @return the updated user
   */
  User setModerator(User user);

  /**
   * Update the user's government watched control.
   *
   * @param userId the user id
   * @return the updated user
   */
  User setWatched(int userId);

  /**
   * Get all users in the database.
   *
   * @return all users
   */
  List<User> getAllUsers();

  /**
   * Set api to use
   */
  void setAPI(APIFactory apiFactory);
}
