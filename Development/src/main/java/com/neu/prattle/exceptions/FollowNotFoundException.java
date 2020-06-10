package com.neu.prattle.exceptions;

/***
 * An representation of an error which is thrown where a request has been made
 * for unfollowing a user/group object that the does not exist.
 */
public class FollowNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -4845176561270017896L;

  public FollowNotFoundException(String message) {
    super(message);
  }
}
