package com.neu.prattle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({GroupEntityTest.class, UserControllerTest.class, GroupControllerTest.class, GroupServiceMockTests.class, UserTests.class, UserServiceTests.class, UserServiceMockTests.class})


public class AllTests {
}
