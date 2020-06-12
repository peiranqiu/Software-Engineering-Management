package com.neu.prattle;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupNotFoundException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class GroupServiceMockTest {
  @Mock
  private GroupService groupService;
  @Mock
  private Group group1;
  @Mock
  private Group group2=new Group("testGroup2");
  @Mock
  private Group group3 = new Group("testGroup3");

  @Before
  public void setUp(){
    groupService= GroupServiceImpl.getInstance();
    groupService=mock(GroupService.class);
    group1=mock(Group.class);
    group2 = mock(Group.class);
    group3 = mock(Group.class);

  }

  @Test
  public void testAddGroup(){

    when(groupService.addGroup(any(Group.class))).thenReturn(true);
    assertTrue(groupService.addGroup(group2));
  }

  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupAlreadyExist(){
    when(groupService.addGroup(any(Group.class))).thenThrow(GroupAlreadyPresentException.class);
    assertTrue(groupService.addGroup(group2));
  }

  @Test
  public void testFindGroup(){
    Optional<Group> optional = Optional.of(group2);
    when(groupService.findGroupByName(anyString())).thenReturn(optional);
    assertEquals(groupService.findGroupByName("testGroup2").get(), group2);

    when(groupService.findGroupByName(anyString())).thenReturn(Optional.empty());
    assertFalse(groupService.findGroupByName("testGroup3").isPresent());
  }

  @Test(expected = GroupNotFoundException.class)
  public void testGroupNotFound(){
    when(groupService.findGroupByName(anyString())).thenThrow(GroupNotFoundException.class);
    assertFalse(groupService.findGroupByName("^^&*(").isPresent());
  }

  @Test
  public void testSetPasswordforGroup() {
    when(groupService.setPasswordforGroup(anyInt(), anyString())).thenReturn(true);
    assertTrue(groupService.setPasswordforGroup(1, "test1234"));
  }

  @Test
  public void testgetSubGroupList() {
    List<Group> groupList = groupService.getSubGroupList(4);

    when(groupService.getSubGroupList(anyInt())).thenReturn(groupList);
    assertEquals(groupList, groupService.getSubGroupList(4));
  }

  @Test
  public void testAddSubgroupIntoGroup() {
    when(groupService.addSubgroupIntoGroup(anyInt(), anyInt())).thenReturn(true);
    assertTrue(groupService.addSubgroupIntoGroup(1, 4));
  }


}

