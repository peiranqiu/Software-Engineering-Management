package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.jpa_service.UserJPAService;

import java.util.Optional;

/**
 * The class made to delegate tasks to the JPA service and send results back to Service.
 */
public class UserServiceImpl implements UserService {

    private static final UserService userServiceInstance;
    private UserJPAService userJPAService;

    /***
     * UserServiceImpl is a Singleton class.
     */
    private UserServiceImpl() {
        userJPAService = UserJPAService.getInstance();
    }

    static {
        userServiceInstance = new UserServiceImpl();
    }

    /**
     * Call this method to return an instance of this service.
     * @return this
     */
    public static UserService getInstance() {
        return userServiceInstance;
    }

    @Override
    public void setJPAService(UserJPAService userJPAService) {
        if(userJPAService == null) {
            this.userJPAService = UserJPAService.getInstance();
        } else {
            this.userJPAService = userJPAService;
        }
    }

    @Override
    public User loginUser(User user) throws UserNotFoundException {
        return userJPAService.loginUser(user);
    }

    @Override
    public Optional<User> findUserByName(String name) throws UserNotFoundException {
        return userJPAService.findUserByName(name);
    }

    @Override
    public boolean addUser(User user) throws UserAlreadyPresentException {
        return userJPAService.createUser(user);
    }


}
