package com.neu.prattle;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
@RunWith(Suite.class)
<<<<<<< HEAD
@SuiteClasses({GroupEntityTest.class, GroupServiceTest.class, UserTests.class, UserServiceTests.class, UserServiceMockTests.class})
=======
@SuiteClasses({GroupEntityTest.class, GroupServiceMockTests.class, UserTests.class, UserMockTests.class})
>>>>>>> f9a804a3f20a67db0a0c324f7c55e43f80f54ff6
public class AllTests {
}
