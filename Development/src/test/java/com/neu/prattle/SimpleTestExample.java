package com.neu.prattle;


import static org.junit.Assert.assertTrue;

import java.util.Optional;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.neu.prattle.model.User;
import com.neu.prattle.service.jpa_service.UserJPAService;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


/**
 * A junit test class for user.
 */
public class SimpleTestExample {

	/*private static UserService as= UserServiceImpl.getInstance();


	@Test
	public void setUserTest(){
	   as.addUser(new User("Mike"));
	}
	
	// This method just tries to add 
	@Test
	public void getUserTest(){
		Optional<User> user = as.findUserByName("Mike");
		assertTrue(user.isPresent());
	}
	
	// Performance testing to benchmark our number of users that can be added 
	// in 1 sec	
	
	@Test(timeout = 1000)
	public void checkPrefTest(){
		for(int i=0; i < 1000; i++) {
			as.addUser(new User("Mike"+i));
		}
	}*/

	private UserJPAService userJPAService;

	@Mock
	private User user;
	private UserService userService;

	@Before
	public void setup() {
		user = new User("peiran");
		user.setPassword("12345");
		userService = UserServiceImpl.getInstance();
		userJPAService = mock(UserJPAService.class);

	}

	@Test
	public void testUserCreation() {
		when(userJPAService.createUser(any())).thenReturn(true);
		when(userJPAService.getUser(anyInt())).thenReturn(user);
		userService.setJPAService(userJPAService);
		assertTrue(userService.addUser(user));
	}





}
