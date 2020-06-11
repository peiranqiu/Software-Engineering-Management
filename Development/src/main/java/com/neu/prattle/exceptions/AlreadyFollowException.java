package com.neu.prattle.exceptions;

/***
 * An representation of an error which is thrown where a request has been made
 * for follow a user/group object that the user already followed.
 */
public class AlreadyFollowException extends RuntimeException {

  private static final long serialVersionUID = -4845176561270017896L;

  public AlreadyFollowException(String message) {
    super(message);
  }
}
