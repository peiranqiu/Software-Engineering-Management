package com.neu.prattle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({GroupEntityTest.class, UserControllerTest.class, GroupControllerTest.class, GroupServiceMockTests.class, UserTests.class, UserServiceMockTests.class, UserServiceTests.class})

public class AllTests {
}
