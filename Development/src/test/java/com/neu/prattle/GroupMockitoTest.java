
package com.neu.prattle;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.api.GroupAPI;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.List;

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
public class GroupMockitoTest {

  private GroupService groupService;

  @Mock
  private GroupAPI api;

  private Group group1;
  private Group group2=new Group("testGroup2");
  private Group group3 = new Group("testGroup3");

  @Before
  public void setUp(){
    groupService = GroupServiceImpl.getInstance();
    api = mock(GroupAPI.class);
  }

  @Test
  public void testAddGroup(){
    when(api.addGroup(any(Group.class))).thenReturn(true);
    groupService.setAPI(api);
    assertTrue(groupService.addGroup(group2));
  }

  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupAlreadyExist(){
    when(api.addGroup(any(Group.class))).thenThrow(GroupAlreadyPresentException.class);
    groupService.setAPI(api);
    groupService.addGroup(group2);
  }

  @Test
  public void testFindGroup() throws SQLException {
    when(api.getGroup(anyString())).thenReturn(group2);
    groupService.setAPI(api);
    assertEquals(groupService.findGroupByName("testGroup2").get(), group2);

    when(api.getGroup(anyString())).thenReturn(null);
    groupService.setAPI(api);
    assertFalse(groupService.findGroupByName("testGroup3").isPresent());
  }

  @Test
  public void testgetSubGroupList() throws SQLException{
    List<Group> groupList = groupService.getSubGroupList(4);
    when(api.getSubGroupList(anyInt())).thenReturn(groupList);
    groupService.setAPI(api);
    assertEquals(groupList, groupService.getSubGroupList(4));
  }

  @Test
  public void testAddSubgroupIntoGroup() throws SQLException {
    when(api.addSubgroupIntoGroup(anyInt(), anyInt())).thenReturn(true);
    groupService.setAPI(api);
    assertTrue(groupService.addSubgroupIntoGroup(1, 4));
  }
}