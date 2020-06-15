package com.neu.prattle.controller;

/**
 * Implementation of the response interface.
 */
public class ResponseImpl implements Response {
  private STATUS status;
  private Payload payload;

  /***
   * Default constructor. Used by jackson for serialization/deserialization.
   */
  public ResponseImpl()    {
  }

  /**
   * A constructor that takes a status and a payload and creates a Network Response.
   * @param status the status of the Network Response.
   * @param payload the Payload of the Network Response.
   */
  public ResponseImpl(STATUS status, Payload payload)   {
    this.status = status;
    this.payload = payload;
  }

  /**
   * The status of a Network Response.
   * @return STATUS of the Network Response.
   */
  @Override
  public STATUS status() {
    return status;
  }

  /**
   * The Payload being loaded onto the Network Response.
   * @return The Payload being added.
   */
  @Override
  public Payload payload() {
    return payload;
  }
}
