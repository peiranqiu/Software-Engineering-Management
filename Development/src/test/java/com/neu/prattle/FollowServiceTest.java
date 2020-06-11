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
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A junit test class for follow operation.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FollowServiceTest {
  private User user1 = new User("testFollowUser" + getRandom(), "Password1");
  private User user2 = new User("testFollowUser" + getRandom(),"Password2");
  private Group group1 = new Group("testFollowGroup" + getRandom());
  private UserService userService;
  private GroupService groupService;
  private FollowService followService;

  @Before
  public void setup() {
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
    followService = FollowService.getInstance();
  }

  /**
   * Test user1 follow and unfollow user2.
   */
  @Test
  public void testUserFollowUser() {
    userService.addUser(user1);
    userService.addUser(user2);
    followService.followUser(user1, user2);
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());
    followService.unfollowUser(user1, user2);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
  }

  /**
   * Test user follow failure because of an already followed user.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowUserFail() {
    userService.addUser(user1);
    assertFalse(followService.followUser(user1, user2));
    assertFalse(followService.followUser(user2, user1));
    assertFalse(followService.followUser(user2, new User("Random1", "Random1")));
    userService.addUser(user2);
    followService.followUser(user1, user2);
    followService.followUser(user1, user2);
  }

  /**
   * Test user unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowUserFail() {
    userService.addUser(user1);
    assertFalse(followService.unfollowUser(user1, user2));
    assertFalse(followService.unfollowUser(user2, user1));
    assertFalse(followService.unfollowUser(user2, new User("Random1", "Random1")));
    userService.addUser(user2);
    followService.unfollowUser(user1, user2);
  }

  /**
   * Test get follower list for a given user.
   */
  @Test
  public void testUserGetFollowers() {
    assertTrue(followService.userGetFollowers(user1).isEmpty());
    userService.addUser(user1);
    userService.addUser(user2);
    assertTrue(followService.userGetFollowers(user2).isEmpty());
    followService.followUser(user1, user2);
    assertEquals(followService.userGetFollowers(user2).get(0).getName(), user1.getName());
  }

  /**
   * Test get following user list for a given user.
   */
  @Test
  public void testGetFollowingUsers() {
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
    userService.addUser(user1);
    userService.addUser(user2);
    assertTrue(followService.getFollowingUsers(user1).isEmpty());
    followService.followUser(user1, user2);
    assertEquals(followService.getFollowingUsers(user1).get(0).getName(), user2.getName());
  }

  /**
   * Test user follows and unfollows a group.
   */
  @Test
  public void testFollowGroup() {
    groupService.addGroup(group1);
    assertFalse(followService.followGroup(user1, group1));

    userService.addUser(user1);
    assertTrue(followService.followGroup(user1, group1));
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());

    assertTrue(followService.unfollowGroup(user1, group1));
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
  }

  /**
   * Test group follow failure because the target group is not found.
   */
  @Test
  public void testFollowGroupFail0() {
    userService.addUser(user1);
    assertFalse(followService.followGroup(user1, group1));
  }

  /**
   * Test group follow failure because of an already followed group.
   */
  @Test(expected = AlreadyFollowException.class)
  public void testFollowGroupFail1() {
    groupService.addGroup(group1);
    assertFalse(followService.unfollowGroup(user1, group1));

    userService.addUser(user1);
    followService.followGroup(user1, group1);
    followService.followGroup(user1, group1);
  }

  /**
   * Test group unfollow failure because the target group is not found.
   */
  @Test
  public void testUnfollowGroupFail0() {
    userService.addUser(user1);
    assertFalse(followService.unfollowGroup(user1, group1));
  }

  /**
   * Test group unfollow failure because of a non existing follow.
   */
  @Test(expected = FollowNotFoundException.class)
  public void testUnfollowGroupFail1() {
    userService.addUser(user1);
    groupService.addGroup(group1);
    followService.unfollowGroup(user1, group1);
  }

  /**
   * Test get follower list for a given group.
   */
  @Test
  public void testGroupGetFollowers() {
    userService.addUser(user1);
    userService.addUser(user2);
    assertTrue(followService.groupGetFollowers(group1).isEmpty());
    groupService.addGroup(group1);
    followService.followGroup(user1, group1);
    followService.followGroup(user2, group1);
    assertEquals(followService.groupGetFollowers(group1).get(0).getName(), user1.getName());
    assertEquals(followService.groupGetFollowers(group1).get(1).getName(), user2.getName());
  }

  /**
   * Test get following group list for a given user.
   */
  @Test
  public void testGetFollowingGroups() {
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
    userService.addUser(user1);
    groupService.addGroup(group1);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
    followService.followGroup(user1, group1);
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