package com.neu.prattle.exceptions;

public class NoPrivilegeException extends RuntimeException {

  private static final long serialVersionUID = -4845176561270017896L;

  public NoPrivilegeException(String message) {
    super(message);
  }
}