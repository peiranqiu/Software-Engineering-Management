package com.neu.prattle;

import com.neu.prattle.exceptions.AlreadyFollowException;
import com.neu.prattle.exceptions.FollowNotFoundException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A mockito test class for follow operation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class FollowMockTest {

  private User user1 = new User("testFollowUser" + getRandom(), "Password1");
  private User user2 = new User("testFollowUser" + getRandom(),"Password2");
  private Group group1 = new Group("testFollowGroup" + getRandom());

  @Mock
  private UserService userService;

  @Mock
  private GroupService groupService;

  @Mock
  private FollowService followService;

  @Before
  public void setup() {
    userService = UserServiceImpl.getInstance();
    userService = mock(UserService.class);
    groupService = GroupServiceImpl.getInstance();
    groupService = mock(GroupService.class);
    followService = FollowService.getInstance();
    followService = mock(FollowService.class);
  }

  /**
   * Test user1 follow and unfollow user2.
   */
  @Test
  public void testUserFollowUser() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    assertTrue(userService.addUser(user1));
    when(userService.addUser(any(User.class))).thenReturn(true);
    assertTrue(userService.addUser(user2));

    when(followService.followUser(any(User.class), any(User.class))).thenReturn(true);
    assertTrue(followService.followUser(user1, user2));

    List<User> list = new ArrayList<>();
    list.add(user2);
    when(followService.getFollowingUsers(any(User.class))).thenReturn(list);
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());

    when(followService.unfollowUser(any(User.class), any(User.class))).thenReturn(true);
    assertTrue(followService.unfollowUser(user1, user2));

    when(followService.getFollowingUsers(any(User.class))).thenReturn(new ArrayList<>());
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
  }

  /**
   * Test user follow failure because of an already followed user.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowUserFail() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    when(followService.followUser(any(User.class), any(User.class))).thenReturn(true);
    followService.followUser(user1, user2);
    when(followService.followUser(any(User.class), any(User.class))).thenThrow(AlreadyFollowException.class);
    followService.followUser(user1, user2);
  }

  /**
   * Test user unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowUserFail() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    when(followService.unfollowUser(any(User.class), any(User.class))).thenThrow(FollowNotFoundException.class);
    followService.unfollowUser(user1, user2);
  }

  /**
   * Test get follower list for a given user.
   */
  @Test
  public void testUserGetFollowers() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    when(followService.userGetFollowers(any(User.class))).thenReturn(new ArrayList<>());
    assertTrue(followService.userGetFollowers(user2).isEmpty());

    when(followService.followUser(any(User.class), any(User.class))).thenReturn(true);
    assertTrue(followService.followUser(user1, user2));

    List<User> list = new ArrayList<>();
    list.add(user1);
    when(followService.userGetFollowers(any(User.class))).thenReturn(list);
    assertEquals(followService.userGetFollowers(user2).get(0).getName(), user1.getName());
  }

  /**
   * Test get following user list for a given user.
   */
  @Test
  public void testGetFollowingUsers() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);

    when(followService.getFollowingUsers(any(User.class))).thenReturn(new ArrayList<>());
    assertTrue(followService.getFollowingUsers(user1).isEmpty());

    when(followService.followUser(any(User.class), any(User.class))).thenReturn(true);
    followService.followUser(user1, user2);

    List<User> list = new ArrayList<>();
    list.add(user2);
    when(followService.getFollowingUsers(any(User.class))).thenReturn(list);
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

    when(followService.followGroup(any(User.class), any(Group.class))).thenReturn(true);
    followService.followGroup(user1, group1);

    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(followService.getFollowingGroups(any(User.class))).thenReturn(list);
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());

    when(followService.unfollowGroup(any(User.class), any(Group.class))).thenReturn(true);
    assertTrue(followService.unfollowGroup(user1, group1));

    when(followService.getFollowingGroups(any(User.class))).thenReturn(new ArrayList<>());
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
    when(followService.followGroup(any(User.class), any(Group.class))).thenReturn(true);
    followService.followGroup(user1, group1);

    when(followService.followGroup(any(User.class), any(Group.class))).thenThrow(AlreadyFollowException.class);
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

    when(followService.unfollowGroup(any(User.class), any(Group.class))).thenThrow(FollowNotFoundException.class);
    followService.unfollowGroup(user1, group1);
  }

  /**
   * Test get follower list for a given group.
   */
  @Test
  public void testGroupGetFollowers() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);
    when(followService.followGroup(any(User.class), any(Group.class))).thenReturn(true);
    followService.followGroup(user1, group1);
    when(followService.followGroup(any(User.class), any(Group.class))).thenReturn(true);
    followService.followGroup(user2, group1);

    List<User> list = new ArrayList<>();
    list.add(user1);
    list.add(user2);
    when(followService.groupGetFollowers(any(Group.class))).thenReturn(list);
    assertEquals(followService.groupGetFollowers(group1).get(0).getName(), user1.getName());
    when(followService.groupGetFollowers(any(Group.class))).thenReturn(list);
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

    when(followService.getFollowingGroups(any(User.class))).thenReturn(new ArrayList<>());
    assertTrue(followService.getFollowingGroups(user1).isEmpty());

    when(followService.followGroup(any(User.class), any(Group.class))).thenReturn(true);
    assertTrue(followService.followGroup(user1, group1));

    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(followService.getFollowingGroups(any(User.class))).thenReturn(list);
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