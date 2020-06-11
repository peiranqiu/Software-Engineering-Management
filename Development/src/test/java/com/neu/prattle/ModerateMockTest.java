package com.neu.prattle;

import com.neu.prattle.exceptions.FollowNotFoundException;
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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Mockito test class for user moderating groups.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class ModerateMockTest {

  private GroupService groupService;
  private UserService userService;

  @Mock
  private ModerateService moderateService;

  private User user1 = new User("testModerator11", "Password1");
  private User user2 = new User("testModerator" + getRandom(),"Password" + getRandom());
  private User user3 = new User("testModerator33", "Password3");
  private User user4 = new User("testModerator44", "Password4");
  private Group group1 = new Group("testModerateGroup11");

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    groupService = GroupServiceImpl.getInstance();
    moderateService = ModerateService.getInstance();
    moderateService = mock(ModerateService.class);
  }

  /**
   * Test a user creates a group and becomes a moderator and member of that group.
   */
  @Test
  public void test0UserCreateGroup() {
    userService.addUser(user1);
    groupService.addGroup(group1);

    user1.setModerator(true);
    userService.setModerator(user1);
    when(moderateService.addGroupModerator(any(Group.class), any(User.class), any(User.class))).thenReturn(user1);
    moderateService.addGroupModerator(group1, user1, user1);

    when(moderateService.addGroupMember(any(Group.class), any(User.class), any(User.class))).thenReturn(true);
    assertTrue(moderateService.addGroupMember(group1, user1, user1));

    List<User> list = new ArrayList<>();
    list.add(user1);
    when(moderateService.getModerators(any(Group.class))).thenReturn(list);
    assertEquals(moderateService.getModerators(group1).get(0).getName(), user1.getName());

    when(moderateService.getMembers(any(Group.class))).thenReturn(list);
    assertEquals(moderateService.getMembers(group1).get(0).getName(), user1.getName());
  }

  /**
   * Test moderator add a user to the group.
   */
  @Test
  public void test1AddGroupMember() {
    userService.addUser(user3);
    when(moderateService.addGroupMember(any(Group.class), any(User.class), any(User.class))).thenReturn(true);
    assertTrue(moderateService.addGroupMember(group1, user1, user3));
  }

  /**
   * Test get group list that the user has or moderates.
   */
  @Test
  public void test1GetUserGroup() {
    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(moderateService.getModerateGroups(any(User.class))).thenReturn(list);
    assertEquals(moderateService.getModerateGroups(user1).get(0).getName(), group1.getName());

    when(moderateService.getHasGroups(any(User.class))).thenReturn(list);
    assertEquals(moderateService.getHasGroups(user3).get(0).getName(), group1.getName());
  }

  /**
   * Test a moderator sets a user as moderator failed because the user to add is already the group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail1() {
    when(moderateService.addGroupModerator(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.addGroupModerator(group1, user1, user1);
  }

  /**
   * Test a moderator sets a user as moderator failed because the user is not the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail2() {
    userService.addUser(user2);
    when(moderateService.addGroupModerator(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.addGroupModerator(group1, user1, user2);
  }

  /**
   * Test a user sets another user as moderator failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail3() {
    userService.addUser(user2);
    when(moderateService.addGroupModerator(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.addGroupModerator(group1, user3, user2);
  }

  /**
   * Test moderator sets a member as group moderator.
   */
  @Test
  public void test2AddModeratorSuccess() {
    when(moderateService.addGroupModerator(any(Group.class), any(User.class), any(User.class))).thenReturn(user3);
    assertEquals(moderateService.addGroupModerator(group1, user1, user3).getName(), user3.getName());
  }

  /**
   * Test moderator downgrades another moderator as group member.
   */
  @Test
  public void test3SetModeratorAsMember() {
    when(moderateService.deleteGroupModerator(any(Group.class), any(User.class), any(User.class))).thenReturn(true);
    assertTrue(moderateService.deleteGroupModerator(group1, user1, user3));
  }

  /**
   * Test user downgrades another moderator as group member failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail1() {
    when(moderateService.deleteGroupModerator(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.deleteGroupModerator(group1, user3, user1);
  }

  /**
   * Test moderator downgrades another moderator as group member failed because the other user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail2() {
    when(moderateService.deleteGroupModerator(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.deleteGroupModerator(group1, user1, user3);
  }

  /**
   * Test moderator downgrades a moderator to group member failed because the group has only one moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail3() {
    when(moderateService.deleteGroupModerator(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.deleteGroupModerator(group1, user1, user1);
  }

  /**
   * Test adds another user into a group failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test4AddGroupMemberFail1() {
    userService.addUser(user2);
    when(moderateService.addGroupMember(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.addGroupMember(group1, user3, user2);
  }

  /**
   * Test adds another user into a group failed because the other user is already in the group.
   */
  @Test(expected = IllegalStateException.class)
  public void test4AddGroupMemberFail2() {
    when(moderateService.addGroupMember(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.addGroupMember(group1, user1, user3);
  }

  /**
   * Test delete member from group failed because the member is not in the group yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail1() {
    when(moderateService.deleteGroupMember(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    userService.addUser(user2);
    assertFalse(moderateService.deleteGroupMember(group1, user1, user2));
  }

  /**
   * Test delete member from group failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail2() {
    when(moderateService.deleteGroupMember(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    assertFalse(moderateService.deleteGroupMember(group1, user3, user3));
  }

  /**
   * Test delete member from group failed because the user to be deleted is the group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail3() {
    when(moderateService.deleteGroupMember(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    assertFalse(moderateService.deleteGroupMember(group1, user1, user1));
  }

  /**
   * Test create invitation.
   */
  @Test
  public void test6CreateInvitation() {
    userService.addUser(user4);
    when(moderateService.createInvitation(any(Group.class), any(User.class), any(User.class))).thenReturn(true);
    assertTrue(moderateService.createInvitation(group1, user3, user4));
  }

  /**
   * Test create invitation failed because the current user is not the group member yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail1() {
    userService.addUser(user2);
    when(moderateService.createInvitation(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    assertTrue(moderateService.createInvitation(group1, user2, user2));
  }

  /**
   * Test create invitation failed because the invitee is already the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail2() {
    when(moderateService.createInvitation(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    assertTrue(moderateService.createInvitation(group1, user3, user1));
  }

  /**
   * Test moderator approves an invitation.
   */
  @Test
  public void test7ApproveInvitation() {
    when(moderateService.approveInvitation(any(Group.class), any(User.class), any(User.class))).thenReturn(true);
    assertTrue(moderateService.approveInvitation(group1, user1, user4));
  }

  /**
   * Test approves an invitation failed because the current user is not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test7ApproveInvitationFail() {
    when(moderateService.approveInvitation(any(Group.class), any(User.class), any(User.class))).thenThrow(IllegalStateException.class);
    moderateService.approveInvitation(group1, user3, user4);
  }

  /**
   * Test delete member from group.
   */
  @Test
  public void test8DeleteMemberSuccess() {
    when(moderateService.deleteGroupMember(any(Group.class), any(User.class), any(User.class))).thenReturn(true);
    assertTrue(moderateService.deleteGroupMember(group1, user1, user3));
  }

  /**
   * Helper method to generate random number range from 1 to 10000.
   * @return the generated number
   */
  public int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }
}
