package com.neu.prattle;

import com.google.gson.Gson;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.ModerateService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User controller test suite.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
  private UserController userController;
  private Gson gson;
  private User user1 = new User("Username", "passowrd");

  @Mock
  private UserService userService;

  @Mock
  private FollowService followService;

  @Mock
  private ModerateService moderateService;

  @Before
  public void setUp() {
    userController = UserController.getInstance();
    gson = new Gson();
    userService = UserServiceImpl.getInstance();
    userService = mock(UserService.class);
    followService = FollowService.getInstance();
    followService = mock(FollowService.class);
    moderateService = ModerateService.getInstance();
    moderateService = mock(ModerateService.class);
  }

  /**
   * Test create user
   */
  @Test
  public void testCreateUser() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userController.setUserService(userService);
    assertEquals(userController.createUserAccount(user1), gson.toJson(user1));

    when(userService.addUser(any(User.class))).thenReturn(false);
    userController.setUserService(userService);
    assertEquals(userController.createUserAccount(user1), null);
  }

  /**
   * Test get all users
   */
  @Test
  public void testGetAllUsers() {
    when(userService.getAllUsers()).thenReturn(Arrays.asList(user1));
    userController.setUserService(userService);
    assertEquals(userController.getAllUsers(), gson.toJson(Arrays.asList(user1)));
  }

  /**
   * Test user login
   */
  @Test
  public void testLogin() {
    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    userController.setUserService(userService);
    assertEquals(userController.login(user1), gson.toJson(user1));

    when(userService.findUserByName(anyString())).thenReturn(Optional.empty());
    userController.setUserService(userService);
    assertEquals(userController.login(user1), gson.toJson(null));
  }

  /**
   * Test government watches user
   */
  @Test
  public void testWatchUser() {
    when(userService.setWatched(anyInt())).thenReturn(user1);
    userController.setUserService(userService);
    assertEquals(userController.watchUser(1), gson.toJson(user1));
  }

  /**
   * Test get follower
   */
  @Test
  public void testGetFollower() {
    when(followService.userGetFollowers(anyInt())).thenReturn(new ArrayList<>());
    userController.setFollowService(followService);
    assertEquals(userController.getFollower(1), gson.toJson(new ArrayList<>()));
  }

  /**
   * Test get followee
   */
  @Test
  public void testGetFollowee() {
    when(followService.getFollowingUsers(anyInt())).thenReturn(new ArrayList<>());
    userController.setFollowService(followService);
    assertEquals(userController.getFollowedUser(1), gson.toJson(new ArrayList<>()));
  }

  /**
   * Test get has groups
   */
  @Test
  public void testGetHasGroup() {
    when(moderateService.getHasGroups(anyInt())).thenReturn(new ArrayList<>());
    userController.setModerateService(moderateService);
    assertEquals(userController.getHasGroup(1), gson.toJson(new ArrayList<>()));
  }


  /**
   * Test get followed groups
   */
  @Test
  public void testGetFollowedGroup() {
    when(followService.getFollowingGroups(anyInt())).thenReturn(new ArrayList<>());
    userController.setFollowService(followService);
    assertEquals(userController.getFollowedGroup(1), gson.toJson(new ArrayList<>()));
  }

  /**
   * Test user follow user
   */
  @Test
  public void testFollowUser() {
    when(followService.followUser(anyInt(), anyInt())).thenReturn(true);
    userController.setFollowService(followService);
    assertEquals(userController.followUser(1, 1), gson.toJson("Follow successful"));

    when(followService.followUser(anyInt(), anyInt())).thenReturn(false);
    userController.setFollowService(followService);
    assertEquals(userController.followUser(1, 1), gson.toJson("Follow failed"));
  }


  /**
   * Test user unfollow user
   */
  @Test
  public void testUnfollowUser() {
    when(followService.unfollowUser(anyInt(), anyInt())).thenReturn(true);
    userController.setFollowService(followService);
    assertEquals(userController.unfollowUser(1, 1), gson.toJson("Unfollow successful"));

    when(followService.unfollowUser(anyInt(), anyInt())).thenReturn(false);
    userController.setFollowService(followService);
    assertEquals(userController.unfollowUser(1, 1), gson.toJson("Unfollow failed"));
  }

}
