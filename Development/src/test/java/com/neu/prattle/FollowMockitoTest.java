package com.neu.prattle;

import com.neu.prattle.exceptions.AlreadyFollowException;
import com.neu.prattle.exceptions.FollowNotFoundException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.api.FollowAPI;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A mockito test class for follow operation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class FollowMockitoTest {

  private User user1 = new User("testFollowUser" + getRandom(), "Password1");
  private User user2 = new User("testFollowUser" + getRandom(),"Password2");
  private Group group1 = new Group("testFollowGroup" + getRandom());
  private FollowService followService;

  @Mock
  private UserService userService;

  @Mock
  private GroupService groupService;

  @Mock
  private FollowAPI api;

  @Before
  public void setup() {
    userService = UserServiceImpl.getInstance();
    userService = mock(UserService.class);
    groupService = GroupServiceImpl.getInstance();
    groupService = mock(GroupService.class);
    followService = FollowService.getInstance();
    api = mock(FollowAPI.class);
  }

  /**
   * Test user1 follow and unfollow user2.
   */
  @Test
  public void testUserFollowUser() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    assertTrue(userService.addUser(user1));
    assertTrue(userService.addUser(user2));

    List<User> list = new ArrayList<>();
    list.add(user2);

    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    followService.setUserService(userService);

    when(api.getFollowingUsers(anyInt())).thenReturn(list);
    when(api.userFollowUser(anyInt(), anyInt())).thenReturn(true);
    when(api.userUnfollowUser(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);

    assertTrue(followService.followUser(user1, user2));
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());

    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user2));
    followService.setUserService(userService);
    when(api.getFollowingUsers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertTrue(followService.unfollowUser(user1, user2));

    when(api.getFollowingUsers(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
  }

  /**
   * Test user follow failure because of an already followed user.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowUserFail() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    assertTrue(userService.addUser(user1));
    assertTrue(userService.addUser(user2));

    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user2));
    followService.setUserService(userService);
    when(api.userFollowUser(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertTrue(followService.followUser(user1, user2));

    when(api.userFollowUser(anyInt(), anyInt())).thenThrow(AlreadyFollowException.class);
    followService.setAPI(api);
    followService.followUser(user1, user2);
  }

  /**
   * Test user unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowUserFail() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    userService.addUser(user2);
    followService.setAPI(api);
    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user2));
    followService.setUserService(userService);
    followService.unfollowUser(user1, user2);
  }

  /**
   * Test get follower list for a given user.
   */
  @Test
  public void testUserGetFollowers() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    userService.addUser(user2);

    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user2));
    followService.setUserService(userService);
    when(api.userGetFollowers(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.userGetFollowers(user2).isEmpty());

    when(api.userFollowUser(anyInt(), anyInt())).thenReturn(true);
    List<User> list = new ArrayList<>();
    list.add(user1);
    when(api.userGetFollowers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertTrue(followService.followUser(user1, user2));
    assertEquals(followService.userGetFollowers(user2).get(0).getName(), user1.getName());
  }

  /**
   * Test get following user list for a given user.
   */
  @Test
  public void testGetFollowingUsers() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    userService.addUser(user2);

    followService.setAPI(api);
    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    followService.setUserService(userService);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());

    List<User> list = new ArrayList<>();
    list.add(user2);
    when(api.userFollowUser(anyInt(), anyInt())).thenReturn(true);
    when(api.getFollowingUsers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertTrue(followService.followUser(user1, user2));
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());
  }

  /**
   * Test user follows and unfollows a group.
   */
  @Test
  public void testFollowGroup() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);

    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(group1));
    when(api.userFollowGroup(anyInt(), anyInt())).thenReturn(true);
    when(api.getFollowingGroups(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    followService.setUserService(userService);
    followService.setGroupService(groupService);
    assertTrue(followService.followGroup(user1, group1));

    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(api.getFollowingGroups(anyInt())).thenReturn(list);
    when(api.userUnfollowGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());
    assertTrue(followService.unfollowGroup(user1, group1));

    when(api.getFollowingGroups(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
  }

  /**
   * Test group follow failure because of an already followed group.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowGroupFail() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);
    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(group1));
    when(api.userFollowGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    followService.setUserService(userService);
    followService.setGroupService(groupService);
    assertTrue(followService.followGroup(user1, group1));

    when(api.userFollowGroup(anyInt(), anyInt())).thenThrow(AlreadyFollowException.class);
    followService.followGroup(user1, group1);
  }

  /**
   * Test group unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowGroupFail() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);

    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(group1));
    followService.setAPI(api);
    followService.setUserService(userService);
    followService.setGroupService(groupService);
    followService.unfollowGroup(user1, group1);
  }

  /**
   * Test get follower list for a given group.
   */
  @Test
  public void testGroupGetFollowers() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    userService.addUser(user2);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);
    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(group1));
    when(api.userFollowGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    followService.setUserService(userService);
    followService.setGroupService(groupService);
    assertTrue(followService.followGroup(user1, group1));
    assertTrue(followService.followGroup(user2, group1));

    List<User> list = new ArrayList<>();
    list.add(user1);
    list.add(user2);
    when(api.groupGetFollowers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertEquals(followService.groupGetFollowers(group1).get(0).getName(), user1.getName());
    assertEquals(followService.groupGetFollowers(group1).get(1).getName(), user2.getName());
  }

  /**
   * Test get following group list for a given user.
   */
  @Test
  public void testGetFollowingGroups() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);


    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(group1));
    when(api.userFollowGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setGroupService(groupService);
    when(api.getFollowingGroups(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    when(userService.findUserByName(anyString())).thenReturn(Optional.of(user1));
    followService.setUserService(userService);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
    assertTrue(followService.followGroup(user1, group1));

    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(api.getFollowingGroups(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());
  }

  /**
   * Helper method to generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }

}