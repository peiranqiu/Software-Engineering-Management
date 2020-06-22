package com.neu.prattle;

import com.neu.prattle.exceptions.PasswordInvalidException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserNameInvalidException;
import com.neu.prattle.exceptions.UserNotFoundException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import com.neu.prattle.service.api.APIFactory;
import com.neu.prattle.service.api.UserAPI;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A junit test class for user service using mockito.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class UserMockitoTest {

  private UserService userService;

  @Mock
  private APIFactory api;

  private User user1;
  private User user2;
  private User user3;

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    api = mock(APIFactory.class);

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
    when(api.create(any(User.class))).thenReturn(true);
    userService.setAPI(api);
    assertTrue(userService.addUser(user1));
  }

  /**
   * Test failure of user creation because of user already exist.
   */
  @Test(expected = UserAlreadyPresentException.class)
  public void testCreateUserAlreadyExist() {
    when(api.create(any(User.class))).thenThrow(UserAlreadyPresentException.class);
    userService.setAPI(api);
    userService.addUser(user1);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName() {
    userService.setAPI(api);
    user2.setName("123456nfkgfkgsdfhdjfdkekgfhsdfk");
    userService.addUser(user2);

    user2.setName("-1");
    userService.addUser(user2);

    user2.setName("GOODGOODGOOD123");
    userService.addUser(user2);

    user2.setName("goodgoodgood");
    userService.addUser(user2);

    user2.setName("GoodgoodGood");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword() {
    userService.setAPI(api);
    user2.setPassword("-1");
    userService.addUser(user2);

    user2.setPassword("-112v456nfkgfkgsdfhdjfdkekgfhsd3");
    userService.addUser(user2);

    user2.setPassword("GOODGOODGOOD123");
    userService.addUser(user2);

    user2.setPassword("goodgoodgood");
    userService.addUser(user2);

    user2.setPassword("GoodgoodGood");
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
      when(api.create(any(User.class))).thenReturn(true);
      userService.setAPI(api);
      assertTrue(userService.addUser(user));
    }
  }

  /**
   * Test find user with a given name.
   */
  @Test
  public void testFindUserByName() throws SQLException {
    when(api.getUser(anyString())).thenReturn(user1);
    userService.setAPI(api);
    assertEquals(userService.findUserByName("HarryPotter1").get(), user1);

    when(api.getUser(anyString())).thenReturn(null);
    userService.setAPI(api);
    assertFalse(userService.findUserByName("EllenDeGeneres").isPresent());
  }

  /**
   * Test find user with a given id.
   */
  @Test
  public void testFindUserById() throws SQLException {
    when(api.getUser(anyInt())).thenReturn(user1);
    userService.setAPI(api);
    assertEquals(userService.findUserById(user1.getUserId()), user1);

    when(api.getUser(anyInt())).thenReturn(null);
    userService.setAPI(api);
    assertNull(userService.findUserById(-1));
  }

  /**
   * Test get all users in db
   */
  @Test
  public void testGetAllUsers() throws SQLException {

    List<User> allUsers = Arrays.asList(user1, user2);
    when(api.getAllUsers()).thenReturn(allUsers);
    userService.setAPI(api);
    assertEquals(userService.getAllUsers().size(), allUsers.size());


    when(api.getAllUsers()).thenReturn(new ArrayList<>());
    userService.setAPI(api);
    assertTrue(userService.getAllUsers().isEmpty());
  }


  /**
   * Test get current information for the user.
   */
  @Test
  public void testRetrieveInformationForCurrentUser() {
    when(api.create(any(User.class))).thenReturn(true);
    userService.setAPI(api);
    assertTrue(userService.addUser(user3));
    assertEquals("Kale12345", user3.getName());
    assertEquals("Password12345", user3.getPassword());
  }

  /**
   * Test user password update.
   */
  @Test
  public void testUpdatePassword() throws SQLException {
    User newUser = new User("Hhhhhhh123", "Hhhhhhh123");
    when(api.create(any(User.class))).thenReturn(true);
    when(api.getUser(anyString())).thenReturn(newUser);
    when(api.updateUser(any(User.class), anyString(), anyString())).thenReturn(newUser);
    userService.setAPI(api);
    assertTrue(userService.addUser(newUser));

    newUser.setPassword("newPassword123");
    assertEquals(userService.updateUser(newUser, "password").getName(), newUser.getName());
  }

  /**
   * Test user avatar update.
   */
  @Test
  public void testUpdateAvatar() throws SQLException {
    User newUser = new User("Hhhhhhh323", "Hhhhhhh123");
    when(api.create(any(User.class))).thenReturn(true);
    when(api.getUser(anyString())).thenReturn(newUser);
    when(api.updateUser(any(User.class), anyString(), anyString())).thenReturn(newUser);
    userService.setAPI(api);
    assertTrue(userService.addUser(newUser));

    newUser.setAvatar("newPassword123");
    assertEquals(userService.updateUser(newUser, "avatar").getName(), newUser.getName());
  }

  /**
   * Test user password update failure.
   */
  @Test(expected = UserNotFoundException.class)
  public void testUpdatePasswordFail() throws SQLException {
    user2.setPassword("Emma12345");
    userService.setAPI(api);
    userService.updateUser(user2, "password");
  }

  @Test
  public void setModerator() throws SQLException {
    User u = new User("newUser123", "ererererf5F");
    u.setModerator(true);
    when(api.setModerator(any(User.class))).thenReturn(u);
    userService.setAPI(api);
    assertTrue(userService.setModerator(u).getModerator());
  }

  /**
   * Generate random number range from 1 to 10000.
   *
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }

  @Test
  public void testSQLException() throws SQLException {
    when(api.getUser(anyString())).thenThrow(SQLException.class);
    when(api.getUser(anyInt())).thenThrow(SQLException.class);
    when(api.getAllUsers()).thenThrow(SQLException.class);
    when(api.setModerator(any(User.class))).thenThrow(SQLException.class);
    when(api.setWatched(anyInt())).thenThrow(SQLException.class);
    userService.setAPI(api);
    assertFalse(userService.findUserByName("name").isPresent());
    userService.findUserById(1);
    userService.getAllUsers();
    userService.setModerator(new User("NewUser13", "NewPassword1"));
    userService.setWatched(user1.getUserId());
    userService.updateUser(new User("NewUser11", "NewPassword1"), "avatar");
  }

  @Test(expected = UserAlreadyPresentException.class)
  public void testIllegalStateException() throws SQLException {
    when(api.create(any(User.class))).thenThrow(IllegalStateException.class);
    userService.setAPI(api);
    userService.addUser(new User("User1111", "Password1111"));
  }

  @Test
  public void testWatched() throws SQLException {
    User newUser = new User("Hhhhhhh333", "Hhhhhhh133");
    when(api.create(any(User.class))).thenReturn(true);
    when(api.setWatched(anyInt())).thenReturn(newUser);
    userService.setAPI(api);
    assertTrue(userService.addUser(newUser));

    newUser.setWatched(true);
    assertTrue(userService.setWatched(newUser.getUserId()).getWatched());
  }
}
