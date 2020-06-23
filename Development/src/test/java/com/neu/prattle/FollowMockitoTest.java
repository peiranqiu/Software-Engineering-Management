package com.neu.prattle;

import com.neu.prattle.exceptions.AlreadyFollowException;
import com.neu.prattle.exceptions.FollowNotFoundException;
import com.neu.prattle.exceptions.NoPrivilegeException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import com.neu.prattle.service.api.APIFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A mockito test class for follow operation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class FollowMockitoTest {

  private User user1 = new User("testFollowUser" + getRandom(), "Password1");
  private User user2 = new User("testFollowUser" + getRandom(), "Password2");
  private Group group1 = new Group("testFollowGroup" + getRandom());
  private FollowService followService;

  @Mock
  private APIFactory api;

  @Before
  public void setup() {
    followService = FollowService.getInstance();
    api = APIFactory.getInstance();
    api = mock(APIFactory.class);
  }

  /**
   * Test user1 follow and unfollow user2.
   */
  @Test
  public void testUserFollowUser() {
    List<User> list = new ArrayList<>();
    list.add(user2);

    when(api.getFollowedUsers(anyInt())).thenReturn(list);
    when(api.followUser(anyInt(), anyInt())).thenReturn(true);
    when(api.unfollowUser(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);

    assertTrue(followService.unfollowUser(user1.getUserId(), user2.getUserId()));
    assertTrue(followService.followUser(user1.getUserId(), user2.getUserId()));
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());

    when(api.getFollowedUsers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertTrue(followService.unfollowUser(user1, user2));

    when(api.getFollowedUsers(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
  }

  /**
   * Test user follow failure because of an already followed user.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowUserFail() {
    when(api.followUser(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertTrue(followService.followUser(user1, user2));

    when(api.followUser(anyInt(), anyInt())).thenThrow(AlreadyFollowException.class);
    followService.setAPI(api);
    followService.followUser(user1, user2);
  }

  /**
   * Test follow user when already followed
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowUserFail1() {

    when(api.followUser(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertTrue(followService.followUser(user1, user2));

    List<User> followings = new ArrayList<>();
    followings.add(user1);
    when(api.getFollowedUsers(anyInt())).thenReturn(followings);
    followService.setAPI(api);
    followService.followUser(user1, user1);
  }

  /**
   * Test user unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowUserFail() {
    followService.setAPI(api);
    followService.unfollowUser(user1, user2);
  }

  /**
   * Test get follower list for a given user.
   */
  @Test
  public void testUserGetFollowers() {
    when(api.userGetFollowers(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.userGetFollowers(user2).isEmpty());
    assertTrue(followService.userGetFollowers(user2.getUserId()).isEmpty());

    when(api.followUser(anyInt(), anyInt())).thenReturn(true);
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
    followService.setAPI(api);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
    assertTrue(followService.getFollowingUsers(user1.getUserId()).isEmpty());

    List<User> list = new ArrayList<>();
    list.add(user2);
    when(api.getFollowedUsers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());
  }

  /**
   * Test get following user list for a given user, no followings
   */
  @Test
  public void testGetFollowingUsers1() {
    followService.setAPI(api);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
    assertTrue(followService.getFollowingUsers(user1.getUserId()).isEmpty());
  }

  /**
   * Test user follows and unfollows a group.
   */
  @Test
  public void testFollowGroup() {
    when(api.followGroup(anyInt(), anyInt())).thenReturn(true);
    when(api.getFollowedGroups(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.followGroup(user1, group1));
    assertTrue(followService.followGroup(user1.getUserId(), group1.getGroupId()));

    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(api.getFollowedGroups(anyInt())).thenReturn(list);
    when(api.unfollowGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());
    assertTrue(followService.unfollowGroup(user1, group1));
    assertTrue(followService.unfollowGroup(user1.getUserId(), group1.getGroupId()));

    when(api.getFollowedGroups(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
  }

  /**
   * Test get following group list for a given user, no followings
   */
  @Test
  public void testGetFollowingGroups1() {
    followService.setAPI(api);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
    assertTrue(followService.getFollowingGroups(user1.getUserId()).isEmpty());
  }


  /**
   * Test group follow failure because of an already followed group.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowGroupFail1() {
    when(api.followGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertTrue(followService.followGroup(user1, group1));

    when(api.getFollowedGroups(anyInt())).thenReturn(Arrays.asList(group1));
    followService.setAPI(api);
    followService.followGroup(user1, group1);
  }

  /**
   * Test group follow failure because of the group is private.
   */
  @Test(expected = NoPrivilegeException.class)
  public void testFollowGroupFail2() {
    followService.setAPI(api);

    group1.setPassword("Password1");
    followService.followGroup(user1, group1);
  }

  /**
   * Test group unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowGroupFail1() {
    followService.setAPI(api);
    followService.unfollowGroup(user1, group1);
  }

  /**
   * Test get follower list for a given group.
   */
  @Test
  public void testGroupGetFollowers() {
    when(api.followGroup(anyInt(), anyInt())).thenReturn(true);
    followService.setAPI(api);
    assertTrue(followService.followGroup(user1, group1));
    assertTrue(followService.followGroup(user2, group1));

    List<User> list = new ArrayList<>();
    list.add(user1);
    list.add(user2);
    when(api.groupGetFollowers(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertEquals(followService.groupGetFollowers(group1).get(0).getName(), user1.getName());
    assertEquals(followService.groupGetFollowers(1).get(1).getName(), user2.getName());
  }

  /**
   * Test get following group list for a given user, no followings
   */
  @Test
  public void testGroupGetFolowers1() {

    followService.setAPI(api);
    assertTrue(followService.groupGetFollowers(group1).isEmpty());
  }

  /**
   * Test get followers list for a given user, no followers
   */
  @Test
  public void testGetFollower1() {
    followService.setAPI(api);
    assertTrue(followService.userGetFollowers(user1).isEmpty());
    assertTrue(followService.userGetFollowers(user1.getUserId()).isEmpty());
  }

  /**
   * Test get following group list for a given user.
   */
  @Test
  public void testGetFollowingGroups() {

    when(api.followGroup(anyInt(), anyInt())).thenReturn(true);
    when(api.getFollowedGroups(anyInt())).thenReturn(new ArrayList<>());
    followService.setAPI(api);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
    assertTrue(followService.getFollowingGroups(user1.getUserId()).isEmpty());
    assertTrue(followService.followGroup(user1, group1));

    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(api.getFollowedGroups(anyInt())).thenReturn(list);
    followService.setAPI(api);
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());
  }

  /**
   * Helper method to generate random number range from 1 to 10000.
   *
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }

}
