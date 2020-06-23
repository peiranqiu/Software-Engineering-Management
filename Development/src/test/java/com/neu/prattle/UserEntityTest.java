package com.neu.prattle;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import org.junit.Test;

import java.util.Objects;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * A junit test class for user service.
 */
public class UserEntityTest {

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
    user.setUserId(999999);
    assertEquals(999999, user.getUserId());
  }

  /**
   * Test set and get user avatar.
   */
  @Test
  public void testUserAvatar() {
    user.setAvatar("picture.png");
    assertEquals("picture.png", user.getAvatar());
  }

  @Test
  public void testPassword() {
    user.setPassword("passWord1");
    assertEquals("passWord1", user.getPassword());
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
