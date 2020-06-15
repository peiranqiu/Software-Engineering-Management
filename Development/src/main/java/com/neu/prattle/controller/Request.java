package com.neu.prattle.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Network request interface.
 */
public interface Request {
  /***
   *
   * @return the type of the network Request represented by the enum NetworkRequestType
   */
  @JsonProperty("networkRequestType")
  RequestType requestType();

  /***
   *
   * @return the payload carried by this network request.
   */
  @JsonProperty("payload")
  Payload payload();

  /***
   * An enum for the type of NetworkRequest
   */
  enum RequestType {
    CREATE_USER,
    GET_ALL_USER,
  }
}
