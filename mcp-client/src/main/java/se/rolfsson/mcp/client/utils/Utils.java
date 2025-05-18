package se.rolfsson.mcp.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class Utils {

  public static String formatJsonString(String json) {
    try {
      return new JSONObject(json).toString();
    } catch (org.json.JSONException e) {
      log.error("Failed to convert object to pretty JSON string", e);
      return json;
    }
  }

  public static String getPrettyJsonString(ObjectMapper objectMapper, Object object) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert messages to pretty JSON string", e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public static String getObjectAsJson(ObjectMapper objectMapper, Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert object to pretty JSON string", e);
      throw new RuntimeException(e.getMessage());
    }
  }
}
