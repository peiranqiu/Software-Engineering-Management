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
        GroupServiceMockTests.class,GroupServiceTest.class, UserTests.class,
        UserServiceMockTests.class, UserServiceTests.class})
public class AllTests {
  //All tests
}