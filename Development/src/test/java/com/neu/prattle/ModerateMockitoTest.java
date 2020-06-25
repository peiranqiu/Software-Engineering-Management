package com.neu.prattle;

import com.neu.prattle.exceptions.NoPrivilegeException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;
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
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Mockito test class for user moderating groups.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class ModerateMockitoTest {

  private User user1 = new User("testModerator11", "Password1");
  private User user2 = new User("testModerator" + getRandom(), "Password" + getRandom());
  private User user3 = new User("testModerator33", "Password3");
  private User user4 = new User("testModerator44", "Password4");
  private Group group1 = new Group("testModerateGroup11");
  private Group group2 = new Group("testModerateGroup22");
  private ModerateService moderateService;

  @Mock
  private GroupService groupService;

  @Mock
  private UserService userService;

  @Mock
  private APIFactory api;

  @Before
  public void setUp() {
    userService = UserServiceImpl.getInstance();
    userService = mock(UserService.class);
    groupService = GroupServiceImpl.getInstance();
    groupService = mock(GroupService.class);
    moderateService = ModerateService.getInstance();
    api = APIFactory.getInstance();
    api = mock(APIFactory.class);
  }

  /**
   * Test a user creates a group and becomes a moderator and member of that group.
   */
  @Test
  public void test0UserCreateGroup() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user1);
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupService.addGroup(group1);

    user1.setModerator(true);
    when(userService.setModerator(any(User.class))).thenReturn(any(User.class));
    userService.setModerator(user1);
    when(api.addModerator(anyInt(), anyInt())).thenReturn(true);
    when(api.addMember(anyInt(), anyInt())).thenReturn(true);
    when(api.getModerators(anyInt())).thenReturn(new ArrayList<>());
    when(api.getMembers(anyInt())).thenReturn(new ArrayList<>());
    moderateService.setAPI(api);
    moderateService.setGroupService(groupService);
    moderateService.setUserService(userService);
    assertEquals(moderateService.addGroupModerator(group1, user1, user1), user1);


    List<User> list = new ArrayList<>();
    list.add(user1);
    when(api.getModerators(anyInt())).thenReturn(list);
    moderateService.setAPI(api);
    assertTrue(moderateService.addGroupMember(group1, user1, user1));

    when(api.getMembers(anyInt())).thenReturn(list);
    moderateService.setAPI(api);
    assertEquals(moderateService.getModerators(group1).get(0).getName(), user1.getName());
    assertEquals(moderateService.getMembers(group1).get(0).getName(), user1.getName());
  }

  /**
   * Test moderator add a user to the group.
   */
  @Test
  public void test1AddGroupMember() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user3);
    when(api.getMembers(anyInt())).thenReturn(new ArrayList<>());
    when(api.addMember(anyInt(), anyInt())).thenReturn(true);
    moderateService.setGroupService(groupService);
    moderateService.setUserService(userService);
    List<User> list = new ArrayList<>();
    list.add(user1);
    when(api.getModerators(anyInt())).thenReturn(list);
    moderateService.setAPI(api);
    assertTrue(moderateService.addGroupMember(group1, user1, user3));
    assertTrue(moderateService.addGroupMember(group1.getGroupId(), user1.getUserId()));
  }

  /**
   * Test get group list that the user has or moderates.
   */
  @Test
  public void test1GetUserGroup() {
    List<Group> list = new ArrayList<>();
    list.add(group1);
    when(api.getModerateGroups(anyInt())).thenReturn(list);
    when(api.getHasGroups(anyInt())).thenReturn(list);
    moderateService.setAPI(api);
    moderateService.setGroupService(groupService);
    moderateService.setUserService(userService);
    assertEquals(moderateService.getModerateGroups(user1).get(0).getName(), group1.getName());
    assertEquals(moderateService.getHasGroups(user3).get(0).getName(), group1.getName());
    assertEquals(moderateService.getHasGroups(user3.getUserId()).get(0).getName(), group1.getName());
  }


  /**
   * Test a moderator sets a user as moderator failed because the user to add is already the group
   * moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail1() {
    moderateService = helperAddModerator(true);
    moderateService.addGroupModerator(group1, user1, user1);
  }

  /**
   * Test a moderator sets a user as moderator failed because the user is not the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail2() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    moderateService = helperAddModerator(true);
    moderateService.addGroupModerator(group1, user1, user2);
  }

  /**
   * Test a user sets another user as moderator failed because the current user is not group
   * moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test2AddModeratorFail3() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    moderateService = helperAddModerator(true);
    moderateService.addGroupModerator(group1, user3, user2);
  }

  /**
   * Test moderator sets a member as group moderator.
   */
  @Test
  public void test2AddModeratorSuccess() {
    moderateService = helperAddModerator(false);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    assertEquals(moderateService.addGroupModerator(group1, user1, user3).getName(), user3.getName());
    when(api.addModerator(anyInt(),anyInt())).thenReturn(false);
    moderateService.setAPI(api);
    assertNull(moderateService.addGroupModerator(group1, user1, user3));
  }

  /**
   * Test moderator sets a member as group moderator.
   */
  @Test
  public void test2AddModeratorSuccess2() {
    moderateService = helperAddModerator(false);
    assertEquals(moderateService.addGroupModerator(group1.getGroupId(), user2.getUserId()).getUserId(), user2.getUserId());
    when(api.addModerator(anyInt(),anyInt())).thenReturn(false);
    moderateService.setAPI(api);
    assertNull(moderateService.addGroupModerator(group1.getGroupId(), user2.getUserId()));
  }

  /**
   * Test moderator downgrades another moderator as group member.
   */
  @Test
  public void test3SetModeratorAsMember() {
    moderateService = helperDeleteModerator(false);
    List<User> list = new ArrayList<>();
    list.add(user1);
    list.add(user3);
    when(api.getModerators(anyInt())).thenReturn(list);
    moderateService.setAPI(api);
    assertTrue(moderateService.deleteGroupModerator(group1, user1, user3));
    when(api.getModerateGroups(anyInt())).thenReturn(Arrays.asList(group1));
    moderateService.setAPI(api);
    assertTrue(moderateService.deleteGroupModerator(group1, user1, user3));
    assertTrue(moderateService.deleteGroupModerator(1,2));
  }

  /**
   * Test user downgrades another moderator as group member failed because the current user is not
   * group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test3SetModeratorAsMemberFail1() {
    moderateService = helperDeleteModerator(true);
    moderateService.deleteGroupModerator(group1, user3, user1);
  }

  /**
   * Test moderator downgrades another moderator as group member failed because the other user is
   * not group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail2() {
    moderateService = helperDeleteModerator(true);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    moderateService.deleteGroupModerator(group1, user1, user3);
  }

  /**
   * Test moderator downgrades a moderator to group member failed because the group has only one
   * moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test3SetModeratorAsMemberFail3() {
    moderateService = helperDeleteModerator(true);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    moderateService.deleteGroupModerator(group1, user1, user1);
  }

  /**
   * Test adds another user into a group failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test4AddGroupMemberFail1() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    moderateService = helperAddMember(true);
    moderateService.addGroupMember(group1, user3, user2);
  }

  /**
   * Test adds another user into a group failed because the other user is already in the group.
   */
  @Test(expected = IllegalStateException.class)
  public void test4AddGroupMemberFail2() {
    moderateService = helperAddMember(true);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user3));
    moderateService.setAPI(api);
    moderateService.addGroupMember(group1, user1, user3);
  }

  /**
   * Test delete member from group failed because the member is not in the group yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail1() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);
    moderateService = helperDeleteMember(true);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    assertFalse(moderateService.deleteGroupMember(group1, user1, user2));
  }

  /**
   * Test delete member from group failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test5DeleteMemberFail2() {

    moderateService = helperDeleteMember(true);
    assertFalse(moderateService.deleteGroupMember(group1, user3, user3));
  }

  /**
   * Test delete member from group failed because the user to be deleted is the group moderator.
   */
  @Test(expected = IllegalStateException.class)
  public void test5DeleteMemberFail3() {

    moderateService = helperDeleteMember(true);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    assertFalse(moderateService.deleteGroupMember(group1, user1, user1));
  }

  /**
   * Test create invitation.
   */
  @Test
  public void test6CreateInvitation() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user4);

    moderateService = helperCreateInvitation(false);
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user3));
    moderateService.setAPI(api);
    assertTrue(moderateService.createInvitation(group1.getGroupId(), user1.getUserId()));
    assertTrue(moderateService.createInvitation(group1, user3, user4, true));
    assertTrue(moderateService.createInvitation(group1, user3, user3, false));
  }

  /**
   * Test create invitation failed because the current user is already the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail0() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);

    moderateService = helperCreateInvitation(true);
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user2));
    moderateService.setAPI(api);
    assertTrue(moderateService.createInvitation(group1, user2, user2, true));


  }

  /**
   * Test create delete member invitation failed because the member to be deleted is not the group
   * member yet.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail1() {
    when(userService.addUser(any(User.class))).thenReturn(true);
    userService.addUser(user2);

    moderateService = helperCreateInvitation(true);
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user3));
    moderateService.setAPI(api);
    moderateService.createInvitation(group1, user3, user2, false);
  }

  /**
   * Test create add member invitation failed because the invitee is already the group member.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail2() {
    moderateService = helperCreateInvitation(true);
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user3, user1));
    moderateService.setAPI(api);
    moderateService.createInvitation(group1, user3, user1, true);
  }

  /**
   * Test create add member invitation failed because The current user is not a member of the group.
   */
  @Test(expected = IllegalStateException.class)
  public void test6CreateInvitationFail3() {

    moderateService = helperCreateInvitation(true);
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    moderateService.createInvitation(group1, user2, user2, true);
  }

  /**
   * Test moderator approves an invitation.
   */
  @Test
  public void test7ApproveInvitation() {
    moderateService = helperService();
    when(api.deleteInvitation(anyInt(), anyInt())).thenReturn(true);
    when(api.addMember(anyInt(), anyInt())).thenReturn(true);
    when(api.deleteMember(anyInt(), anyInt())).thenReturn(true);
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user3));
    when(api.getInvitations(anyInt())).thenReturn(new HashMap<>());
    moderateService.setAPI(api);
    assertEquals(moderateService.getMembers(group1.getGroupId()), Arrays.asList(user3));
    assertTrue(moderateService.approveInvitation(group1, user1, user2, true));
    assertTrue(moderateService.approveInvitation(group1.getGroupId(), user2.getUserId()));
    assertTrue(moderateService.approveInvitation(group1, user1, user3, false));
    assertTrue(moderateService.getGroupInvitations(group1.getGroupId()).isEmpty());

    when(api.deleteInvitation(anyInt(), anyInt())).thenReturn(false);
    moderateService.setAPI(api);
    assertFalse(moderateService.approveInvitation(group1.getGroupId(), user2.getUserId()));
    assertFalse(moderateService.approveInvitation(group1, user1, user2, true));
  }

  /**
   * Test approves an invitation failed because the current user is not group moderator.
   */
  @Test(expected = NoPrivilegeException.class)
  public void test7ApproveInvitationFail() {
    moderateService = helperService();
    moderateService.setAPI(api);
    moderateService.approveInvitation(group1, user3, user4, true);
  }

  /**
   * Test delete member from group.
   */
  @Test
  public void test8DeleteMemberSuccess() {
    moderateService = helperDeleteMember(false);
    when(api.getMembers(anyInt())).thenReturn(Arrays.asList(user3));
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    assertTrue(moderateService.deleteGroupMember(group1, user1, user3));
    assertTrue(moderateService.deleteGroupMember(1, 2));
  }

  /**
   * Test delete member from group.
   */
  @Test
  public void test9AddSubGroup() {
    moderateService = helperService();
    when(api.getModerators(anyInt())).thenReturn(Arrays.asList(user1));
    moderateService.setAPI(api);
    assertTrue(moderateService.addSubgroup(group1, user1, group2));
  }


  /**
   * Helper method to generate random number range from 1 to 10000.
   *
   * @return the generated number
   */
  private int getRandom() {
    return (int) (Math.random() * 10000 + 1);
  }

  /**
   * Helper method to configure moderate service for adding moderator.
   *
   * @return moderate service
   */
  private ModerateService helperAddModerator(boolean exp) {
    moderateService = helperService();
    if (Boolean.TRUE.equals(exp)) {
      when(api.addModerator(anyInt(), anyInt())).thenThrow(IllegalStateException.class);
    } else {
      when(api.addModerator(anyInt(), anyInt())).thenReturn(true);
    }
    moderateService.setAPI(api);
    return moderateService;
  }

  /**
   * Helper method to configure moderate service for deleting moderator.
   *
   * @return moderate service
   */
  private ModerateService helperDeleteModerator(boolean exp) {
    moderateService = helperService();
    if (Boolean.FALSE.equals(exp)) {
      when(api.deleteModerator(anyInt(), anyInt())).thenReturn(true);
      moderateService.setAPI(api);
    }
    moderateService.setAPI(api);
    return moderateService;
  }

  /**
   * Helper method to configure moderate service for adding member.
   *
   * @return moderate service
   */
  private ModerateService helperAddMember(boolean exp) {
    moderateService = helperService();
    if (Boolean.FALSE.equals(exp)) {
      when(api.addMember(anyInt(), anyInt())).thenReturn(true);
      moderateService.setAPI(api);
    }

    return moderateService;
  }

  /**
   * Helper method to configure moderate service for deleting member.
   *
   * @return moderate service
   */
  private ModerateService helperDeleteMember(boolean exp) {
    moderateService = helperService();
    if (Boolean.FALSE.equals(exp)) {
      when(api.deleteMember(anyInt(), anyInt())).thenReturn(true);
      moderateService.setAPI(api);
    }

    return moderateService;
  }

  /**
   * Helper method to configure moderate service for creating invitation.
   *
   * @return moderate service
   */
  private ModerateService helperCreateInvitation(boolean exp) {
    moderateService = helperService();
    if (Boolean.FALSE.equals(exp)) {
      when(api.createInvitation(anyInt(), anyInt(), anyBoolean())).thenReturn(true);
      moderateService.setAPI(api);
    }
    return moderateService;
  }

  /**
   * Helper method to config group and user service for moderate service.
   *
   * @return moderate service
   */
  private ModerateService helperService() {
    moderateService.setGroupService(groupService);
    when(userService.findUserById(anyInt())).thenReturn(user1);
    moderateService.setUserService(userService);
    return moderateService;
  }


}
