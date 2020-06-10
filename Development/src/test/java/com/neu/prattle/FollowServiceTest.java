package com.neu.prattle;

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
   * Test get follower list for a given user.
   */
  @Test
  public void testUserGetFollowers() {
    userService.addUser(user1);
    userService.addUser(user2);
    followService.followUser(user1, user2);
    assertEquals(followService.userGetFollowers(user2).get(0).getName(), user1.getName());
  }

  /**
   * Test user follows and unfollows a group.
   */
  @Test
  public void testUserFollowGroup() {
    userService.addUser(user1);
    groupService.addGroup(group1);
    followService.followGroup(user1, group1);
    assertEquals(followService.getFollowingGroups(user1).get(0).getName(), group1.getName());
    followService.unfollowGroup(user1, group1);
    assertTrue(followService.getFollowingGroups(user1).isEmpty());
  }

  /**
   * Test get follower list for a given group.
   */
  @Test
  public void testGroupGetFollowers() {
    userService.addUser(user1);
    userService.addUser(user2);
    groupService.addGroup(group1);
    followService.followGroup(user1, group1);
    followService.followGroup(user2, group1);
    assertEquals(followService.groupGetFollowers(group1).get(0).getName(), user1.getName());
    assertEquals(followService.groupGetFollowers(group1).get(1).getName(), user2.getName());
  }

  /**
   * Generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }


}
