package com.neu.prattle;


import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A junit test class for user.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupServiceTest {
  private GroupService groupService;

  @Mock
  private Group group1 = new Group("testGroup1");
  private Group group2 = new Group("testGroup2");
  private Group group3 = new Group("testGroup3");

  @Before
  public void setUp() {
    groupService = GroupServiceImpl.getInstance();
  }

  @Test
  public void test1() {
    assertTrue(groupService.addGroup(group1));
  }

  @Test(expected = GroupAlreadyPresentException.class)
  public void test2() {
    groupService.addGroup(group1);
  }

  @Test
  public void test3() {
    assertEquals(group1.getName(), groupService.findGroupByName("testGroup1").get().getName());
  }

  @Test
  public void test4() {
    assertFalse(groupService.findGroupByName("emptyName1").isPresent());
  }


  @Test
  public void test6(){
    groupService.addSubgroupIntoGroup(3,1);
    groupService.addSubgroupIntoGroup(3,2);
    assertEquals("testModerateGroup3",groupService.getSubGroupList(3).get(1).getName());
  }

  @Test
  public void testSetPasswordforGroup() {
    assertTrue(groupService.setPasswordforGroup(2, "test1234"));
  }



}
