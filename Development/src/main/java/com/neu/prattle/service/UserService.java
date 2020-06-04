package com.neu.prattle.service;

import com.neu.prattle.model.User;
import com.neu.prattle.service.jpa_service.UserJPAService;

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
     * Login a particular user.
     *
     * @param user The user to login
     * @return the user that is logged in
     */
    User loginUser(User user);

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

    /**
     * Set the JPA Service for this user service.
     * @param userJPAService
     */
    public void setJPAService(UserJPAService userJPAService);
}
