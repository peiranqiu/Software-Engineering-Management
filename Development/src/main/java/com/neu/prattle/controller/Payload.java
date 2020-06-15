package com.neu.prattle.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * A payload object class.
 */
@FunctionalInterface
@JsonDeserialize(using = ResponseDeserializer.class)
public interface Payload {
  /***
   * Stores the transferable entities in json format.
   * @return
   * @throws JsonProcessingException
   */
  @JsonProperty("jsonString")
  String jsonString() throws JsonProcessingException;
}

class JSONDeserializer extends StdDeserializer<Payload> {

  /**
   * Creates a JSONDeserializer
   */
  public JSONDeserializer()   {
    this(null);
  }

  /**
   * Creates a JSONDeserializer
   * @param vc the class being passed to parser into a JSON.
   */
  public JSONDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   * Deserializer for the Payload.
   * @param jsonParser parser for the json.
   * @param deserializationContext the context of the deserializer.
   * @return Payload that has been created.
   * @throws IOException if there are deserializers set to null..
   */
  @Override
  public Payload deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String jsonString = node.get("jsonString").asText();
    return new PayloadImpl(jsonString);
  }
}