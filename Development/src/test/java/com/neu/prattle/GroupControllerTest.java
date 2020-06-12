package com.neu.prattle;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.model.Group;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * A junit test class for user service.
 */

public class GroupControllerTest {
  GroupController controller = new GroupController();

  @Test
  public void test(){
    Group a = new Group("Abcdef1234");

    assertEquals(200, controller.createGroupAccount(a).getStatus());
    assertEquals(409, controller.createGroupAccount(a).getStatus());
  }
}
