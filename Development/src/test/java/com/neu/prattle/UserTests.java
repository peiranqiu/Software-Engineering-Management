package com.neu.prattle;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * A junit test class for user service.
 */
public class UserTests {

  private User user = new User("CS5500Team2");
  private User user1 = new User("CS5500Team3");
  private User user2 = new User("CS5500Team4");
  private User user3 = new User("CS5500Team5");
  private Group group1 = new Group();
  private Group group2 = new Group();

  /**
   * Test set and get user name.
   */
  @Test
  public void testUserId() {
    user = new User("CS5500Team1");
    user.setUserId(99999);
    assertEquals(user.getUserId(), 99999);
  }

  /**
   * Test set and get user avatar.
   */
  @Test
  public void testUserAvatar() {
    user.setAvatar("picture.png");
    assertEquals(user.getAvatar(), "picture.png");
  }

  /**
   * Test set and get user's following users list.
   */
  @Test
  public void testFollowUser() {
    List<User> followedUser = new ArrayList<>();
    followedUser.add(user2);
    followedUser.add(user3);
    followedUser.add(user1);
    user.setFollowedUser(followedUser);
    assertEquals(user.getFollowedUser(), followedUser);
  }

  /**
   * Test set and get user's follower list.
   */
  @Test
  public void testUsersFollower() {
    List<User> follower = new ArrayList<>();
    follower.add(user2);
    follower.add(user3);
    follower.add(user1);
    user.setFollower(follower);
    assertEquals(user.getFollower(), follower);
  }

  /**
   * Test set and get user's group list.
   */
  @Test
  public void testUsersGroup() {
    List<Group> groups = new ArrayList<>();
    groups.add(group1);
    groups.add(group2);
    user.setGroups(groups);
    assertEquals(user.getGroups(), groups);
  }

  /**
   * Test set and get user's followed group list.
   */
  @Test
  public void testUserFollowGroup() {
    List<Group> followedGroups = new ArrayList<>();
    followedGroups.add(group1);
    followedGroups.add(group2);
    user.setFollowedGroup(followedGroups);
    assertEquals(user.getFollowedGroup(), followedGroups);
  }

  /**
   * Test user hashcode.
   */
  @Test
  public void testUserHashcode() {
    assertEquals(user.hashCode(), Objects.hash(user.getName()));
  }

  /**
   * Test user equals incompatible object.
   */
  @Test
  public void testUserEquals() {
    assertFalse(user.equals(1));
  }

}
