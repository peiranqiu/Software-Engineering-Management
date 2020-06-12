package com.neu.prattle;

import com.neu.prattle.exceptions.NoPrivilegeException;
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
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Junit test for user moderates groups.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class ModerateServiceTest {

  private GroupService groupService;
  private UserService userService;
  private ModerateService moderateService;
  private User user1 = new User("testModerator1", "Password1");
  private User user2 = new User("testModerator" + getRandom(),"Password" + getRandom());
  private User user3 = new User("testModerator3", "Password3");
  private User user4 = new User("testModerator4", "Password4");
  private Group group1 = new Group("testModerateGroup1");
  private Group group2 = new Group("testModerateGroup2");
  private Group group3 = new Group("testModerateGroup3");

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
  public void test0MemberCreateGroup() {
    userService.addUser(user1);
    groupService.addGroup(group1);
    User moderator = moderateService.addGroupModerator(group1, user1, user1);
    assertTrue(moderator.getModerator());
    assertTrue(moderateService.addGroupMember(group1, moderator, user1));
    assertEquals(moderateService.getModerators(group1).get(0).getName(), user1.getName());
    assertEquals(moderateService.getMembers(group1).get(0).getName(), user1.getName());
    assertTrue(moderateService.getModerateGroups(user2).isEmpty());
    assertTrue(moderateService.getHasGroups(user2).isEmpty());
    assertTrue(moderateService.getModerators(group2).isEmpty());
    assertTrue(moderateService.getMembers(group2).isEmpty());
  }

  /**
   * Test a moderator creates another group.
   */
  @Test
  public void test0ModeratorCreateGroup() {
    groupService.addGroup(group3);
    User moderator = moderateService.addGroupModerator(group3, user1, user1);
    assertTrue(moderator.getModerator());
    assertTrue(moderateService.addGroupMember(group3, moderator, user1));
    assertEquals(moderateService.getModerators(group3).get(0).getName(), user1.getName());
    assertEquals(moderateService.getMembers(group3).get(0).getName(), user1.getName());
  }

  /**
   * Test moderator add a user to the group.
   */
  @Test
  public void test1AddGroupMember() {
    userService.addUser(user3);
    assertTrue(moderateService.addGroupMember(group1, user1, user3));
  }

  /**
   * Test get group list that the user has or moderates.
   */
  @Test
  public void test1GetUserGroup() {
    assertEquals(moderateService.getModerateGroups(user1).get(0).getName(), group1.getName());
    assertEquals(moderateService.getHasGroups(user3).get(0).getName(), group1.getName());
  }

  /**
   * Test a moderator sets a user as moderator failed because the target group or user is not found.
   */
  @Test
  public void test2AddModeratorFail0() {
    assertNull(moderateService.addGroupModerator(group2, user1, user2));
    assertNull(moderateService.addGroupModerator(group1, user1, user2));
    assertNull(moderateService.addGroupModerator(group2, user1, user1));
  }

  /**
   * Test a moderator sets a user as moderator failed because the user is already the group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail1() {
    moderateService.addGroupModerator(group1, user1, user1);
  }

  /**
   * Test a moderator sets a user as moderator failed because the user is not the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail2() {
    userService.addUser(user2);
    moderateService.addGroupModerator(group1, user1, user2);
  }

  /**
   * Test a user sets another user as moderator failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test2AddModeratorFail3() {
    userService.addUser(user2);
    moderateService.addGroupModerator(group1, user3, user2);
  }

  /**
   * Test moderator sets a member as group moderator.
   */
  @Test
  public void test2AddModeratorSuccess() {
    assertEquals(moderateService.addGroupModerator(group1, user1, user3).getName(), user3.getName());
  }

  /**
   * Test moderator downgrades another moderator as group member.
   */
  @Test
  public void test3SetModeratorAsMember() {
    assertTrue(moderateService.addGroupMember(group3, user1, user3));
    assertEquals(moderateService.addGroupModerator(group3, user1, user3).getName(), user3.getName());
    assertTrue(moderateService.deleteGroupModerator(group3, user1, user3));
    assertTrue(moderateService.deleteGroupModerator(group1, user1, user3));
  }

  /**
   * Test user downgrades another moderator as group member failed because the target group or user is not found.
   */
  @Test
  public void test3SetModeratorAsMemberFail0() {
    assertFalse(moderateService.deleteGroupModerator(group2, user1, user2));
    assertFalse(moderateService.deleteGroupModerator(group1, user1, user2));
    assertFalse(moderateService.deleteGroupModerator(group2, user1, user1));
  }

  /**
   * Test user downgrades another moderator as group member failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test3SetModeratorAsMemberFail1() {
    moderateService.deleteGroupModerator(group1, user3, user3);
  }

  /**
   * Test moderator downgrades another moderator as group member failed because the other user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail2() {
    moderateService.deleteGroupModerator(group1, user1, user3);
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
   * Test adds another user into a group failed because the target group or user is not found.
   */
  @Test
  public void test4AddGroupMemberFail0() {
    assertFalse(moderateService.addGroupMember(group2, user1, user2));
    assertFalse(moderateService.addGroupMember(group1, user1, user2));
    userService.addUser(user2);
    assertFalse(moderateService.addGroupMember(group2, user1, user2));
  }

  /**
   * Test adds another user into a group failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
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
   * Test delete member from group failed because the target group or user is not found.
   */
  @Test
  public void test5DeleteMemberFail0() {
    assertFalse(moderateService.deleteGroupMember(group2, user1, user2));
    assertFalse(moderateService.deleteGroupMember(group2, user1, user1));
    assertFalse(moderateService.deleteGroupMember(group1, user1, user2));
  }

  /**
   * Test delete member from group failed because the member is not in the group yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail1() {
    userService.addUser(user2);
    moderateService.deleteGroupMember(group1, user1, user2);
  }

  /**
   * Test delete member from group failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
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
   * Test create invitation.
   */
  @Test
  public void test6CreateInvitation() {
    userService.addUser(user4);
    assertTrue(moderateService.createInvitation(group1, user3, user4, true));
  }

  /**
   * Test create invitation failed because the target group or user is not found.
   */
  @Test
  public void test6CreateInvitationFail0() {
    assertFalse(moderateService.createInvitation(group2, user1, new User("randomUser1", "passWord1"), true));
    assertFalse(moderateService.createInvitation(group1, user1, new User("randomUser2", "passWord1"), true));
    assertFalse(moderateService.createInvitation(group2, user1, user4, true));
  }

  /**
   * Test create invitation failed because the current user is not the group member yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail1() {
    userService.addUser(user2);
    moderateService.createInvitation(group1, user2, user2, true);
  }

  /**
   * Test create invitation failed because the invitee is already the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail2() {
    assertTrue(moderateService.createInvitation(group1, user3, user1, true));
  }

  /**
   * Test moderator approves an invitation.
   */
  @Test
  public void test7ApproveInvitation() {
    List<User> list = moderateService.getModerators(group1);
    User moderator = list.get(0);
    assertTrue(moderateService.approveInvitation(group1, moderator, user4, true));
  }

  /**
   * Test approves an invitation failed because the target group or user is not found.
   */
  @Test
  public void test7ApproveInvitationFail0() {
    assertFalse(moderateService.approveInvitation(group2, user1, user2, true));
    assertFalse(moderateService.approveInvitation(group1, user1, user2, true));
  }

  /**
   * Test approves an invitation failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test7ApproveInvitationFail1() {
    moderateService.approveInvitation(group1, user3, user4, true);
  }

  /**
   * Test delete member from group.
   */
  @Test
  public void test8DeleteMemberSuccess() {
    assertTrue(moderateService.deleteGroupMember(group1, user1, user3));
  }

  /**
   * Test delete member from group because the target group or user is not found.
   */
  @Test
  public void test8DeleteMemberFail() {
    assertFalse(moderateService.deleteGroupMember(group2, user1, user3));
    assertFalse(moderateService.deleteGroupMember(group2, user1, user2));
    assertFalse(moderateService.deleteGroupMember(group1, user1, user2));
  }

  /**
   * Test add a subgroup into a group failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test9_1AddSubgroupFail() {
    Optional<Group> optional3 = groupService.findGroupByName(group3.getName());
    Optional<Group> optional1 = groupService.findGroupByName(group1.getName());
    if(optional1.isPresent() && optional3.isPresent()) {
      Group g1 = optional1.get();
      Group g3 = optional3.get();
      moderateService.addSubgroup(g1, user3, g3);
    }
  }

  /**
   * Test add a subgroup into a group success.
   */
  @Test
  public void test9_1AddSubgroupSuccess() {
    Optional<Group> optional3 = groupService.findGroupByName(group3.getName());
    Optional<Group> optional1 = groupService.findGroupByName(group1.getName());
    if(optional1.isPresent() && optional3.isPresent()) {
      Group g1 = optional1.get();
      Group g3 = optional3.get();
      assertTrue(moderateService.addSubgroup(g1, user1, g3));
    }
  }

  /**
   * Test delete a subgroup from a group failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test9_2DeleteSubgroupFail() {
    Optional<Group> optional3 = groupService.findGroupByName(group3.getName());
    Optional<Group> optional1 = groupService.findGroupByName(group1.getName());
    if(optional1.isPresent() && optional3.isPresent()) {
      Group g1 = optional1.get();
      Group g3 = optional3.get();
      moderateService.addSubgroup(g1, user3, g3);
    }
  }


  /**
   * Test delete a subgroup from a group success.
   */
  @Test
  public void test9_2DeleteSubgroupSuccess() {
    Optional<Group> optional3 = groupService.findGroupByName(group3.getName());
    Optional<Group> optional1 = groupService.findGroupByName(group1.getName());
    if(optional1.isPresent() && optional3.isPresent()) {
      Group g1 = optional1.get();
      Group g3 = optional3.get();
      assertTrue(moderateService.addSubgroup(g1, user1, g3));
    }
  }

  /**
   * Test delete a group fail because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test9_3DeleteGroupFail() {
    Optional<Group> optional = groupService.findGroupByName(group3.getName());
    if(optional.isPresent()) {
      moderateService.deleteGroup(optional.get(), user3);
    }
  }

  /**
   * Test delete a group.
   */
  @Test
  public void test9_3DeleteGroupSuccess() {
    Optional<Group> optional = groupService.findGroupByName(group1.getName());
    if(optional.isPresent()) {
      Group group = optional.get();
      User moderator = moderateService.getModerators(group).get(0);
      assertTrue(moderateService.deleteGroup(group, moderator));
    }
  }

  /**
   * Helper method to generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }
}
