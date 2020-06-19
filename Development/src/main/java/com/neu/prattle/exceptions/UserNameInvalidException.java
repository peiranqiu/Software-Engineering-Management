package com.neu.prattle.exceptions;

/***
 * An representation of an error which is thrown where a request has been made
 * for creation of a user object with invalid username.
 *
 */
public class UserNameInvalidException extends IllegalArgumentException {

  private static final long serialVersionUID = -4845176561270017896L;

  public UserNameInvalidException(String message) {
    super(message);
  }
}
