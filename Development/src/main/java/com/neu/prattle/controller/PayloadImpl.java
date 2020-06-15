package com.neu.prattle.controller;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Implementation of payload interface.
 */
public class PayloadImpl implements Payload {

  private String jsonString;

  /**
   * A Constructor for the Payload IMPL (Default).
   */
  public PayloadImpl()    {

  }

  /**
   * A Constructor to create a payload json.
   * @param jsonString the string being pased to the payload.
   */
  @JsonCreator
  public PayloadImpl(String jsonString)    {
    this.jsonString = jsonString;
  }

  /**
   * The string in the payload.
   * @return the string in the payload.
   */
  @Override
  public String jsonString() {
    return jsonString;
  }
}
