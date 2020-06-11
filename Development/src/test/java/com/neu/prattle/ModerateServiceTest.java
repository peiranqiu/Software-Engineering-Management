package com.neu.prattle;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Junit test for user moderates groups.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModerateServiceTest {

  private GroupService groupService;
  private UserService userService;
  private ModerateService moderateService;
  private User user1 = new User("testModerator1", "Password1");
  private User user2 = new User("testModerator" + getRandom(),"Password" + getRandom());
  private User user3 = new User("testModerator3", "Password3");
  private Group group1 = new Group("testModerateGroup1");

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
    moderateService = ModerateService.getInstance();
  }

  /**
   * Test a user creates a group and becomes a moderator and member of that group.
   */
  @Test
  public void test1UserCreateGroup() {
    userService.addUser(user1);
    groupService.addGroup(group1);
    User moderator = moderateService.addGroupModerator(group1, user1, user1);
    assertTrue(moderator.getModerator());
    assertTrue(moderateService.addGroupMember(group1, moderator, user1));
    assertEquals(moderateService.getModerators(group1).get(0).getName(), user1.getName());
    assertEquals(moderateService.getMembers(group1).get(0).getName(), user1.getName());
  }


  /**
   * Test moderator add a user to the group.
   */
  @Test
  public void test2AddGroupMember() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    userService.addUser(user3);
    assertTrue(moderateService.addGroupMember(group1, moderator, user3));
  }

  /**
   * Test a moderator sets a user as moderator failed because the user is already the group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail1() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    moderateService.addGroupModerator(group1, moderator, user1);
  }

  /**
   * Test a moderator sets a user as moderator failed because the user is not the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail2() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    userService.addUser(user2);
    moderateService.addGroupModerator(group1, moderator, user2);
  }

  /**
   * Test a user sets another user as moderator failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail3() {
    userService.addUser(user2);
    moderateService.addGroupModerator(group1, user3, user2);
  }

  /**
   * Test moderator sets a member as group moderator.
   */
  @Test
  public void test2AddModeratorSuccess() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    assertEquals(moderateService.addGroupModerator(group1, moderator, user3).getName(), user3.getName());
  }

  /**
   * Test moderator downgrades another moderator as group member.
   */
  @Test
  public void test3SetModeratorAsMember() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    assertTrue(moderateService.deleteGroupModerator(group1, moderator, user3));
  }

  /**
   * Test user downgrades another moderator as group member failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail1() {
    moderateService.deleteGroupModerator(group1, user3, user1);
  }

  /**
   * Test moderator downgrades another moderator as group member failed because the other user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail2() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    moderateService.deleteGroupModerator(group1, moderator, user3);
  }

  /**
   * Test moderator downgrades a moderator to group member failed because the group has only one moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail3() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    moderateService.deleteGroupModerator(group1, moderator, user1);
  }

  /**
   * Test adds another user into a group failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test4AddGroupMemberFail1() {
    userService.addUser(user2);
    moderateService.addGroupMember(group1, user3, user2);
  }

  /**
   * Test adds another user into a group failed because the other user is already in the group.
   */
  @Test(expected = IllegalStateException.class)
  public void test4AddGroupMemberFail2() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    moderateService.addGroupMember(group1, moderator, user3);
  }

  /**
   * Test delete member from group failed because the member is not in the group yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail1() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    userService.addUser(user2);
    assertFalse(moderateService.deleteGroupMember(group1, moderator, user2));
  }

  /**
   * Test delete member from group failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail2() {
    assertFalse(moderateService.deleteGroupMember(group1, user3, user3));
  }

  /**
   * Test delete member from group failed because the user to be deleted is the group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail3() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    assertFalse(moderateService.deleteGroupMember(group1, moderator, user1));
  }

  /**
   * Test delete member from group.
   */
  @Test
  public void test5DeleteMemberSuccess() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    assertTrue(moderateService.deleteGroupMember(group1, moderator, user3));
  }

  /**
   * Helper method to generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }
}
