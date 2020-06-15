package com.neu.prattle.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

/**
 * A network response sent from server to client.
 */
@JsonDeserialize(as = ResponseImpl.class)
public interface Response {

  /***
   * Represents the status of this response
   * @return STATUS
   */
  @JsonProperty("status")
  STATUS status();

  /***
   * Represents the payload of this response.
   * @return Payload
   */
  @JsonProperty("payload")
  Payload payload();

  /***
   * Represents the status of this network response.
   */
  enum STATUS {
    SUCCESSFUL, FAILED;
  }
}

/***
 * Used by jackson library to deserialize
 */
class ResponseDeserializer extends StdDeserializer<Payload> {

  /**
   * Default private constructor used by Jackson.
   */
  public ResponseDeserializer() {
    this(null);
  }

  /**
   * Network Response Deserializer constructor for this class takes in a class to deserialize.
   * @param vc the class being deserialized.
   */
  public ResponseDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   * A deserializer for the NetworkResponse.
   * @param jsonParser the parser for json.
   * @param deserializationContext the context for a deserializer.
   * @return Payload that was created.
   * @throws IOException if there aren't any deserializers.
   */
  @Override
  public Payload deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String jsonString = node.get("jsonString").asText();
    return new PayloadImpl(jsonString);
  }
}