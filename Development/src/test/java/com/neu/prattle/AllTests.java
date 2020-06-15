package com.neu.prattle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is a wrapper for all test classes.
 */
@RunWith(Suite.class)
@SuiteClasses({GroupEntityTest.class, MessageMockitoTest.class, GroupMockitoTest.class,
        UserEntityTest.class, UserMockitoTest.class, FollowMockitoTest.class})
public class AllTests {
  //All tests
}