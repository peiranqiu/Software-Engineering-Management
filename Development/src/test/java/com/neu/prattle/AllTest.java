package com.neu.prattle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is a wrapper for all test classes.
 */
@RunWith(Suite.class)
@SuiteClasses({GroupEntityTest.class, UserControllerTest.class,
        MessageTest.class, ChatEndpointTest.class, GroupControllerTest.class,
        GroupServiceMockTest.class,GroupServiceTest.class, UserTest.class,
        UserServiceMockTest.class, UserServiceTest.class})
public class AllTest {
  //All tests
}