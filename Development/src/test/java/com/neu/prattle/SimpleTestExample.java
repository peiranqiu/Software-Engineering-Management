//package com.neu.prattle;
//
//
//import static org.junit.Assert.assertTrue;
//
//import com.neu.prattle.service.UserService;
//import com.neu.prattle.service.UserServiceImpl;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.neu.prattle.model.User;
//import org.mockito.Mock;
//
//
//import static org.mockito.Mockito.*;
//
//
///**
// * A junit test class for user.
// */
//public class SimpleTestExample {
//
//	private UserService userService;
//
//	@Mock
//	private User user;
//
//	@Before
//	public void setUp() {
//		userService = UserServiceImpl.getInstance();
//	}
//
//	@Before
//	public void setup() {
//		user = new User("peiran");
//		user.setPassword("12345");
//		userService = UserServiceImpl.getInstance();
//
//	}
//
//	@Test
//	public void testUserCreation() {
//		//when(userService.addUser(any())).thenReturn(true);
//		//assertTrue(userService.addUser(user));
//	}
//
//
//}
