package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
     * @return this
     */
    public static UserService getInstance() {
        return userService;
    }

    @Override
    public Optional<User> findUserByName(String name) throws UserNotFoundException {
        final User user = new User(name);
        String query =
                "SELECT User_id FROM User u WHERE u.name ='" + name + "'";
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next()) {
                return Optional.of(user);
            }
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new UserNotFoundException("User with username: "+ name + " not found.");
        }
    }

    @Override
    public synchronized void addUser(User user) throws UserAlreadyPresentException {

        String query =
                "INSERT INTO User (name, password) VALUES ('" + user.getName() + "', '" + user.getPassword() + "');";
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getName()));
        }
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException {
        String query = "UPDATE User SET password = '" + user.getPassword() + "' WHERE User_id = " + user.get_id() + ";";
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (SQLException e) {
            throw new UserNotFoundException("User "+ user.getName() + " not found.");
        }
    }

    @Override
    public void userFollowUser(User follower, User followee) {

    }

}
