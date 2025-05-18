package se.rolfsson.mcp.client.model;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class NanosecondsToSecondsDeserializer extends JsonDeserializer<Double> {
  @Override
  public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    long nanos = p.getLongValue();
    double seconds = nanos / 1_000_000_000.0;
    return Math.round(seconds * 100.0) / 100.0;
  }
}
