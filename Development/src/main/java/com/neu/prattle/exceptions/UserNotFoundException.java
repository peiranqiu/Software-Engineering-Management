package com.neu.prattle.exceptions;

/***
 * An representation of an error which is thrown where a request has been made
 * for creation of a user object that already exists in the system.
 *
 */
public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -4845176561270017896L;

  public UserNotFoundException(String message) {
    super(message);
  }
}
