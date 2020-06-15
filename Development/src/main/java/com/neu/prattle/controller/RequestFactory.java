package com.neu.prattle.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.model.User;

/**
 * Factory to create network requests.
 */
public class RequestFactory {

  /***
   * Creates a NetworkRequest for creating a user.
   * @param userName The username set by the user
   * @param password The password set by the user
   * @return NetworkRequest
   */
  public Request createUserRequest(String userName, String password) {
    return new RequestImpl(Request.RequestType.CREATE_USER,
            () -> {
              User user = new User();
              user.setName(userName);
              user.setPassword(password);

              return new ObjectMapper().writeValueAsString(user);
            });
  }

}
