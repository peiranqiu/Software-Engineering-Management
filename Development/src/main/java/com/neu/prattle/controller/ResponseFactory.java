package com.neu.prattle.controller;

/**
 * Factory to create network responses.
 */
public class ResponseFactory {

  /**
   * Construct a successful response.
   * @return the response
   */
  public Response successfulResponse() {
    return new ResponseImpl(Response.STATUS.SUCCESSFUL, () -> "");
  }

  /**
   * Construct a successful response with payload.
   * @param payload the payload
   * @return the response
   */
  public Response successfulReponse(Payload payload) {
    return new ResponseImpl(Response.STATUS.SUCCESSFUL, payload);
  }

  /**
   * Construct a failed response.
   * @return the response
   */
  public Response failedResponse() {
    return new ResponseImpl(Response.STATUS.FAILED, () -> "");
  }

  /**
   * Construct a failed response with payload.
   * @param payload the payload
   * @return the response
   */
  public Response failedResponse(Payload payload) {
    return new ResponseImpl(Response.STATUS.FAILED, payload);
  }
}
