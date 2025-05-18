package se.rolfsson.mcp.client.model;

import java.util.List;
import lombok.Builder;

@Builder(toBuilder = true)
public record ChatRequest(
    String model,
    List<Message> messages,
    boolean stream,
    List<Tool> tools) {

}
