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
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A junit test class for user service using mockito.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceMockTests {

  @Mock
  private UserService userService;

  @Mock
  private User u;

  private User user1;
  private User user2;
  private User user3;

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    userService = mock(UserService.class);
    u = mock(User.class);

    user1 = new User("HarryPotter" + getRandom());
    user1.setPassword("Password" + getRandom());

    user2 = new User("EmmaStone" + getRandom());
    user2.setPassword("Password" + getRandom());

    user3 = new User("Kale12345");
    user3.setPassword("Password12345");

  }

  /**
   * Test success of user creation.
   */
  @Test
  public void testCreateUser() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    assertTrue(userService.addUser(user1));
  }

  /**
   * Test failure of user creation because of user already exist.
   */
  @Test(expected = UserAlreadyPresentException.class)
  public void testCreateUserAlreadyExist() {
    when(userService.addUser(any(User.class))).thenThrow(UserAlreadyPresentException.class);
    userService.addUser(user1);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName() {
    when(userService.addUser(any(User.class))).thenThrow(UserNameInvalidException.class);
    user2.setName("123456nfkgfkgsdfhdjfdkekgfhsdfk");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(UserNameInvalidException.class);
    user2.setName("-1");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(UserNameInvalidException.class);
    user2.setName("GOODGOODGOOD123");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(UserNameInvalidException.class);
    user2.setName("goodgoodgood");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(UserNameInvalidException.class);
    user2.setName("GoodgoodGood");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword() {
    when(userService.addUser(any(User.class))).thenThrow(PasswordInvalidException.class);
    user2.setPassword("-1");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(PasswordInvalidException.class);
    user2.setPassword("-112v456nfkgfkgsdfhdjfdkekgfhsd3");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(PasswordInvalidException.class);
    user2.setPassword("GOODGOODGOOD123");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(PasswordInvalidException.class);
    user2.setPassword("goodgoodgood");
    userService.addUser(user2);

    when(userService.addUser(any(User.class))).thenThrow(PasswordInvalidException.class);
    user2.setPassword("GoodgoodGood");
    userService.addUser(user2);
  }

  /**
   * Test timeout for adding a large number of users.
   */
  @Test(timeout = 1000)
  public void testTimeout() {
    for (int i = 0; i < 1000; i++) {
      User user = new User("RobsUsername" + i);
      user.setPassword("RobsPassword" + i);
      when(userService.addUser(any(User.class))).thenReturn(true);
      userService.addUser(user);
    }
  }

  /**
   * Test find user with a given name.
   */
  @Test
  public void testFindUserByName() {
    Optional<User> optional = Optional.of(user1);
    when(userService.findUserByName(anyString())).thenReturn(optional);
    assertEquals(userService.findUserByName("HarryPotter1").get(), user1);

    when(userService.findUserByName(anyString())).thenReturn(Optional.empty());
    assertFalse(userService.findUserByName("EllenDeGeneres").isPresent());
  }

  /**
   * Test get current information for the user.
   */
  @Test
  public void testRetrieveInformationForCurrentUser(){
    userService.addUser(user3);
    assertEquals("Kale12345", user3.getName());
    assertEquals("Password12345", user3.getPassword());
  }

  /**
   * Test user password update.
   */
  @Test
  public void testUpdatePassword(){
    user1.setPassword("Harry12345");
    when(userService.updateUser(any(User.class))).thenReturn(user1);
    assertEquals(userService.updateUser(user1).getName(), user1.getName());
  }

  /**
   * Test user password update failure.
   */
  @Test(expected = UserNotFoundException.class)
  public void testUpdatePasswordFail(){
    user2.setPassword("Emma12345");
    when(userService.updateUser(any(User.class))).thenThrow(UserNotFoundException.class);
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

  /**
   * Generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }
}
