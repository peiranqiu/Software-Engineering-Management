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

    private static UserService userService;
    private Set<User> userSet = new HashSet<>();

    /***
     * UserServiceImpl is a Singleton class.
     */
    private UserServiceImpl() {

    }

    static {
        userService = new UserServiceImpl();
    }

    /**
     * Call this method to return an instance of this service.
     * @return this
     */
    public static UserService getInstance() {
        return userService;
    }

    @Override
    public Optional<User> findUserByName(String name) throws UserNotFoundException {
        final User user = new User(name);
        if (userSet.contains(user))
            return Optional.of(user);
        else
            return Optional.empty();
    }

    @Override
    public synchronized boolean addUser(User user) throws UserAlreadyPresentException {
        if (userSet.contains(user))
            throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getName()));

        userSet.add(user);
        return true;
    }


}
