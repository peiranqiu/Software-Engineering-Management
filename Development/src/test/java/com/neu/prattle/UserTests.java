package com.neu.prattle;


import com.neu.prattle.exceptions.PasswordInvalidException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNameInvalidException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;


/**
 * A junit test class for user.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@RunWith(MockitoJUnitRunner.class)
public class UserTests {

  private UserService userService;

  private User user1 = new User("HarryPotter1");
  private User user2 = new User("EmmaStone2");

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    user1.setPassword("User1Password");
  }

  /**
   * Test success of user creation.
   */
  @Test
  public void testCreateUser() {
    userService.addUser(user1);
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
    for (int i = 0; i < 1000; i++) {
      User user = new User("MikesUsername" + i);
      user.setPassword("MikesPassword" + i);
      userService.addUser(user);
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
}
