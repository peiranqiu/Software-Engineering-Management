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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * A junit test class for user service.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {

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
  public void testCreateUserInvalidName0() {
    user2.setName("1Gg");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName1() {
    user2.setName("1");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName2() {
    user2.setName("GfffG3545456547676546745645646");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName3() {
    user2.setName("adfddfdfd");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName4() {
    user2.setName("GFDFGDGRHGDFG");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName5() {
    user2.setName("GFDFGdfdsgsdf");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName6() {
    user2.setName("GFDFG3435");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid username.
   */
  @Test(expected = UserNameInvalidException.class)
  public void testCreateUserInvalidName7() {
    user2.setName("sdgfh3435");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword0() {
    user2.setPassword("1");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword1() {
    user2.setPassword("GfffG3545456547676546745645646");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword2() {
    user2.setPassword("1Gg");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword3() {
    user2.setPassword("adfddfdfd");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword4() {
    user2.setPassword("GFDFGDGRHGDFG");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword5() {
    user2.setPassword("GFDFGdfdsgsdf");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword6() {
    user2.setPassword("GFDFG3435");
    userService.addUser(user2);
  }

  /**
   * Test failure of user creation because of invalid password.
   */
  @Test(expected = PasswordInvalidException.class)
  public void testCreateUserInvalidPassword7() {
    user2.setPassword("sdgfh3435");
    userService.addUser(user2);
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
    assertEquals(userService.updateUser(user1, "password").getName(), user1.getName());
  }

  /**
   * Test user password update.
   */
  @Test
  public void testUpdateAvatar(){
    user1.setAvatar("picture.png");
    assertEquals(userService.updateUser(user1, "avatar").getName(), user1.getName());
    assertNull(userService.updateUser(user1, "random"));
  }

  /**
   * Test user password update failure.
   */
  @Test(expected = UserNotFoundException.class)
  public void testUpdatePasswordFail(){
    user2.setPassword("Emma12345");
    userService.updateUser(user2, "password");
  }


  /**
   * Test set the user as moderator.
   */
  @Test
  public void testSetModerator() {
    user1.setModerator(true);
    assertTrue(userService.setModerator(user1).getModerator());
  }

  /**
   * Close an API connection after all the tasks.
   */
  @Test
  public void closeConnection(){
    UserAPI api = new UserAPI();
    assertTrue(api.closeConnection());
  }
}
