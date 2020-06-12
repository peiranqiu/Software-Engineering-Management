package com.neu.prattle;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * A junit test class for user service.
 */

public class UserControllerTest {
  UserController controller = new UserController();

  @Test
  public void test(){
    User a = new User("Abcdef1234");
    a.setPassword("Abc12345");

    assertEquals(200, controller.createUserAccount(a).getStatus());
    assertEquals(409, controller.createUserAccount(a).getStatus());
  }
}
