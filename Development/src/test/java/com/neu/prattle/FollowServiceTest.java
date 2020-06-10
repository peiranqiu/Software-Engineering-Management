package com.neu.prattle;

import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FollowServiceTest {
  private User user1 = new User("CS5500Team" + getRandom(), "Password1");
  private User user2 = new User("CS5500Team" + getRandom(),"Password2");
  private UserService userService;
  private FollowService followService;

  @Before
  public void setup() {
    userService = UserServiceImpl.getInstance();
    followService = FollowService.getInstance();
  }

  /**
   * Test user1 follow user2.
   */
  @Test
  public void test0UserFollowUser() {
    userService.addUser(user1);
    userService.addUser(user2);
    followService.followUser(user1, user2);
  }

  /**
   * Test user1 unfollow user2.
   */
  @Test
  public void test1UserUnfollowUser() {
    userService.addUser(user1);
    userService.addUser(user2);
    followService.followUser(user1, user2);
    assertEquals(followService.getFollowedUsers(user1).size(), 1);
    followService.unfollowUser(user1, user2);
    assertEquals(followService.getFollowedUsers(user1).size(), 0);
  }

  /**
   * Test get followed user list for a given user.
   */
  @Test
  public void test2GetFollowedUsers() {
    userService.addUser(user1);
    userService.addUser(user2);
    followService.followUser(user1, user2);
    assertEquals(followService.getFollowedUsers(user1).get(0).getName(), user2.getName());
  }

  /**
   * Test get follower list for a given user.
   */
  @Test
  public void test3GetFollowers() {
    userService.addUser(user1);
    userService.addUser(user2);
    followService.followUser(user1, user2);
    assertEquals(followService.getFollowers(user2).get(0).getName(), user1.getName());
  }


  /**
   * Generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }


}
