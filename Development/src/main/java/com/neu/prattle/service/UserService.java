package com.neu.prattle.service;

import com.neu.prattle.model.User;

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
     * Create a new user.
     *
     * @param user User object
     * @return true if the user is created
     *
     */
    boolean addUser(User user);
}
