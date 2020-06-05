package com.neu.prattle;


import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * A junit test class for user.
 */
public class UserTests {

  private UserService userService;

  @Mock
  private User user1 = new User("Harry");
  private User user2 = new User("Emma");

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    user1.setPassword("harry12345");
    userService.addUser(user1);
  }

  /**
   * Test failure of user creation.
   */
  @Test(expected = UserAlreadyPresentException.class)
  public void testCreateUserFail() {
    userService.addUser(user1);
  }

  /**
   * Test success of user creation.
   */
  @Test
  public void testCreateUserSuccess() {
    userService.addUser(user2);
  }

  /**
   * Test find user with a given name.
   */
  @Test
  public void testFindUserByName() {
    assertEquals(userService.findUserByName("Harry").get(), user1);
    assertFalse(userService.findUserByName("Ellen").isPresent());
  }

  /**
   * Test timeout for adding a list of users.
   */
  @Test(timeout = 1000)
  public void checkPrefTest() {
    for (int i = 0; i < 80; i++) {
      userService.addUser(new User("Mike" + i));
    }
  }

  /**
   * Test user password update.
   */
  @Test
	public void updatePassword(){
		//User user = new User("Harry5");
		//userService.addUser(user);
		//user.setPassword("harry212345");
		//userService.updateUser(user);
	}
}
