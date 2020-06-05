package com.neu.prattle;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
@RunWith(Suite.class)
@SuiteClasses({GroupEntityTest.class, GroupServiceTest.class, UserTests.class})
public class AllTests {
}
