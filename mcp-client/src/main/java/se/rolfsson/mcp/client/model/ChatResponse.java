package se.rolfsson.mcp.client.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChatResponse(
    String model,
    LocalDateTime createdAt,
    Message message,
    String doneReason,
    boolean done,
    @JsonDeserialize(using = NanosecondsToSecondsDeserializer.class)
    Double totalDuration,
    @JsonDeserialize(using = NanosecondsToSecondsDeserializer.class)
    Double loadDuration,
    int promptEvalCount,
    @JsonDeserialize(using = NanosecondsToSecondsDeserializer.class)
    Double promptEvalDuration,
    int evalCount,
    @JsonDeserialize(using = NanosecondsToSecondsDeserializer.class)
    Double evalDuration) {

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record ToolCalls(
      CallToolRequest function) {

  }

}
