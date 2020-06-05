package com.neu.prattle;

import com.neu.prattle.exceptions.PasswordInvalidException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNameInvalidException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserAPI;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * A junit test class for user service.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTests {

  private UserService userService;

  private User user1;
  private User user2;

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    user1 = new User("HarryPotter1");
    user1.setPassword("User1Password");
    user2 = new User("EmmaStone2");
    user2.setPassword("User2Password");
  }

  /**
   * Test success of user creation.
   */
  @Test
  public void testCreateUser() {
    assertTrue(userService.addUser(user1));
  }

  /**
   * Test failure of user creation because of user already exist.
   */
  @Test(expected = UserAlreadyPresentException.class)
  public void testCreateUserAlreadyExist() {

    userService.addUser(user1);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName() {
    user2.setName("-1");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword() {
    user2.setPassword("-1");
    userService.addUser(user2);
  }

  /**
   * Test timeout for adding a large number of users.
   */
  @Test(timeout = 1000)
  public void testTimeout() {
    for (int i = 0; i < 100; i++) {
      User user = new User("RobsUsername" + i);
      user.setPassword("RobsPassword" + i);
      assertTrue(userService.addUser(user));
    }
  }

  /**
   * Test find user with a given name.
   */
  @Test
  public void testFindUserByName() {
    assertEquals(userService.findUserByName("HarryPotter1").get(), user1);
    assertFalse(userService.findUserByName("EllenDeGeneres").isPresent());
  }

  /**
   * Test get current information for the user.
   */
  @Test
  public void testRetrieveInformationForCurrentUser(){
    assertEquals("HarryPotter1", user1.getName());
    assertEquals("User1Password", user1.getPassword());
  }

  /**
   * Test user password update.
   */
  @Test
  public void testUpdatePassword(){
    user1.setPassword("Harry12345");
    assertEquals(userService.updateUser(user1).getName(), user1.getName());
  }

  /**
   * Test user password update failure.
   */
  @Test(expected = UserNotFoundException.class)
  public void testUpdatePasswordFail(){
    user2.setPassword("Emma12345");
    userService.updateUser(user2);
  }

  /**
   * Close an API connection after all the tasks.
   */
  @Test
  public void closeConnection(){
    UserAPI api = new UserAPI();
    api.closeConnection();
  }
}
