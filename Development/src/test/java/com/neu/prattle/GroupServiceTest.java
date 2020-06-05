package com.neu.prattle;

import com.neu.prattle.model.Group;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.junit.Test;

public class GroupServiceTest {
  @Test
  public void test1(){
    GroupService groupService=GroupServiceImpl.getInstance();
    Group group1=new Group();
    group1.setName("testGroupService");
    groupService.addGroup(group1);

  }
}
