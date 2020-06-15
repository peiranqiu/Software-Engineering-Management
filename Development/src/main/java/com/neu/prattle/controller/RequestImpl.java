package com.neu.prattle.controller;

/**
 * implementation of network request interface.
 */
public class RequestImpl implements Request {

  private Payload payload;
  private RequestType requestType;

  /***
   * Default constructor
   */
  public RequestImpl() {

  }

  /***
   * Constructor
   * @param requestType The type of the networkRequest
   * @param payload The Payload
   */
  public RequestImpl(RequestType requestType, Payload payload) {
    this.requestType = requestType;
    this.payload = payload;
  }

  @Override
  public RequestType requestType() {
    return requestType;
  }

  @Override
  public Payload payload() {
    return payload;
  }
}
