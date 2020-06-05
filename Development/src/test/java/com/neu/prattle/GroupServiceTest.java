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

/**
 * A junit test class for user.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupServiceTest {
  private GroupService groupService;

  @Mock
  private Group group1=new Group("testGroup1");
  private Group group2=new Group("testGroup2");

  @Before
  public void setUp(){
    groupService=GroupServiceImpl.getInstance();
  }

  @Test
  public void test1(){

    groupService.addGroup(group1);

  }

  @Test(expected = GroupAlreadyPresentException.class)
  public void test2(){
    groupService.addGroup(group1);
  }

  @Test
  public void test3(){
    assertEquals(group1.getName(), groupService.findGroupByName("testGroup1").get().getName());
  }

}
