package com.neu.prattle;

import com.google.gson.Gson;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.FollowService;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.ModerateService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Group controller test suite.
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {
  private GroupController groupController;
  private Gson gson;
  private Group group1 = new Group("Group1");

  @Mock
  private GroupService groupService;

  @Mock
  private FollowService followService;

  @Mock
  private ModerateService moderateService;

  @Before
  public void setUp() {
    groupController = GroupController.getInstance();
    gson = new Gson();
    groupService = GroupServiceImpl.getInstance();
    groupService = mock(GroupService.class);
    followService = FollowService.getInstance();
    followService = mock(FollowService.class);
    moderateService = ModerateService.getInstance();
    moderateService = mock(ModerateService.class);
  }

  /**
   * Test create group.
   */
  @Test
  public void testCreateGroup() {
    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    groupController.setGroupService(groupService);
    assertEquals(groupController.createGroup(group1), gson.toJson(group1));

    when(groupService.addGroup(any(Group.class))).thenReturn(false);
    groupController.setGroupService(groupService);
    assertNull(groupController.createGroup(group1));
  }

  /**
   * Test follow group.
   */
  @Test
  public void testFollowGroup() {
    when(followService.followGroup(anyInt(), anyInt())).thenReturn(true);
    groupController.setFollowService(followService);
    assertEquals(groupController.followGroup(1, 2), gson.toJson("Follow successful"));

    when(followService.followGroup(anyInt(), anyInt())).thenReturn(false);
    groupController.setFollowService(followService);
    assertEquals(groupController.followGroup(1, 2), gson.toJson("Follow failed"));
  }

  /**
   * Test unfollow group.
   */
  @Test
  public void testUnfollowGroup() {
    when(followService.unfollowGroup(anyInt(), anyInt())).thenReturn(true);
    groupController.setFollowService(followService);
    assertEquals(groupController.unfollowGroup(1, 2), gson.toJson("Unfollow successful"));

    when(followService.unfollowGroup(anyInt(), anyInt())).thenReturn(false);
    groupController.setFollowService(followService);
    assertEquals(groupController.unfollowGroup(1, 2), gson.toJson("Unfollow failed"));
  }

  /**
   * Test add moderator
   */
  @Test
  public void testAddModerator() {
    when(moderateService.addGroupModerator(anyInt(), anyInt())).thenReturn(new User());
    groupController.setModerateService(moderateService);
    assertEquals(groupController.addModerator(1, 1), gson.toJson("Add moderator succeed"));

    when(moderateService.addGroupModerator(anyInt(), anyInt())).thenReturn(null);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.addModerator(1, 1), gson.toJson("Add moderator failed"));
  }

  /**
   * Test delete moderator
   */
  @Test
  public void testDeleteModerator() {
    when(moderateService.deleteGroupModerator(anyInt(), anyInt())).thenReturn(true);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.deleteModerator(1, 1), gson.toJson("Delete moderator succeed"));

    when(moderateService.deleteGroupModerator(anyInt(), anyInt())).thenReturn(false);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.deleteModerator(1, 1), gson.toJson("Delete moderator failed"));
  }

  /**
   * Test add member
   */
  @Test
  public void testAddMember() {
    when(moderateService.addGroupMember(anyInt(), anyInt())).thenReturn(true);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.addMember(1, 1), gson.toJson("Add member succeed"));

    when(moderateService.addGroupMember(anyInt(), anyInt())).thenReturn(false);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.addMember(1, 1), gson.toJson("Add member failed"));
  }

  /**
   * Test delete member
   */
  @Test
  public void testDeleteMember() {
    when(moderateService.deleteGroupMember(anyInt(), anyInt())).thenReturn(true);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.deleteMember(1, 1), gson.toJson("Delete member succeed"));

    when(moderateService.deleteGroupMember(anyInt(), anyInt())).thenReturn(false);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.deleteMember(1, 1), gson.toJson("Delete member failed"));
  }

  /**
   * Test get all groups
   */
  @Test
  public void testGetAllGroups() {
    when(groupService.getAllGroups()).thenReturn(Arrays.asList(group1));
    groupController.setGroupService(groupService);
    assertEquals(groupController.getAllGroups(), gson.toJson(Arrays.asList(group1)));
  }

  /**
   * Test get sub groups
   */
  @Test
  public void testGetSubGroups() {
    when(groupService.getSubGroupList(anyInt())).thenReturn(Arrays.asList(group1));
    groupController.setGroupService(groupService);
    assertEquals(groupController.getSubGroups(1), gson.toJson(Arrays.asList(group1)));
  }

  /**
   * Test get sub groups
   */
  @Test
  public void testAddSubGroup() {
    when(groupService.addSubgroupIntoGroup(anyInt(), anyInt())).thenReturn(true);
    groupController.setGroupService(groupService);
    assertEquals(groupController.addSubGroup(1, 1), gson.toJson("Adding subGroup successful"));

    when(groupService.addSubgroupIntoGroup(anyInt(), anyInt())).thenReturn(false);
    groupController.setGroupService(groupService);
    assertEquals(groupController.addSubGroup(1, 1), gson.toJson("Adding subGroup failed"));
  }

  /**
   * Test get group by name
   */
  @Test
  public void testGetGroupByName() {
    when(groupService.findGroupByName(anyString())).thenReturn(Optional.of(new Group()));
    groupController.setGroupService(groupService);
    assertEquals(groupController.getGroupbyName("name"), gson.toJson(new Group()));

    when(groupService.findGroupByName(anyString())).thenReturn(Optional.empty());
    groupController.setGroupService(groupService);
    assertEquals(groupController.getGroupbyName("name"), gson.toJson(null));
  }

  /**
   * Test set group password
   */
  @Test
  public void testSetGroupPassword() {
    when(groupService.setPasswordforGroup(anyInt(), anyString())).thenReturn(true);
    groupController.setGroupService(groupService);
    assertEquals(groupController.setGroupPass(1,"name"), gson.toJson("set password successful"));

    when(groupService.setPasswordforGroup(anyInt(), anyString())).thenReturn(false);
    groupController.setGroupService(groupService);
    assertEquals(groupController.setGroupPass(1,"name"), gson.toJson("set password failed"));
  }

  /**
   * Test get group followers
   */
  @Test
  public void testGetGroupFollowers() {
    when(groupService.getGroupById(anyInt())).thenReturn(new Group());
    when(followService.groupGetFollowers(any(Group.class))).thenReturn(new ArrayList<>());
    groupController.setGroupService(groupService);
    groupController.setFollowService(followService);
    assertEquals(groupController.getGroupFollowers(1), gson.toJson(new ArrayList<>()));
  }

  /**
   * Test get group moderators
   */
  @Test
  public void testGetGroupModerators() {
    when(groupService.getGroupById(anyInt())).thenReturn(new Group());
    when(moderateService.getModerators(any(Group.class))).thenReturn(new ArrayList<>());
    groupController.setGroupService(groupService);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.getGroupModerators(1), gson.toJson(new ArrayList<>()));
  }

  /**
   * Test get group members
   */
  @Test
  public void testGetGroupMembers() {
    when(groupService.getGroupById(anyInt())).thenReturn(new Group());
    when(moderateService.getMembers(any(Group.class))).thenReturn(new ArrayList<>());
    groupController.setGroupService(groupService);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.getGroupMembers(1), gson.toJson(new ArrayList<>()));
  }

  /**
   * Get group invitations
   */
  @Test
  public void testGetGroupInvitations() {
    when(moderateService.getGroupInvitations(anyInt())).thenReturn(new HashMap<>());
    groupController.setModerateService(moderateService);
    assertEquals(groupController.getGroupInvitations(1), gson.toJson(new HashSet<>()));
  }

  /**
   * Test create invitation
   */
  @Test
  public void testCreateInvitation() {
    when(moderateService.createInvitation(anyInt(), anyInt())).thenReturn(true);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.createInvitation(1, 1), gson.toJson("Invitation created successfully"));

    when(moderateService.createInvitation(anyInt(), anyInt())).thenReturn(false);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.createInvitation(1, 1), gson.toJson("Creating invitation failed"));
  }

  /**
   * Test delete invitation
   */
  @Test
  public void testDeleteInvitation() {
    when(moderateService.deleteInvitation(anyInt(), anyInt())).thenReturn(true);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.deleteInvitation(1, 1), gson.toJson("Invitation deleted successfully"));

    when(moderateService.deleteInvitation(anyInt(), anyInt())).thenReturn(false);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.deleteInvitation(1, 1), gson.toJson("Deleting invitation failed"));
  }

  /**
   * Test approve invitation
   */
  @Test
  public void testApproveInvitation() {
    when(moderateService.approveInvitation(anyInt(), anyInt())).thenReturn(true);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.approveInvitation(1, 1), gson.toJson("Invitation approved"));

    when(moderateService.approveInvitation(anyInt(), anyInt())).thenReturn(false);
    groupController.setModerateService(moderateService);
    assertEquals(groupController.approveInvitation(1, 1), gson.toJson("Invitation not approved. Please try again."));
  }
}
