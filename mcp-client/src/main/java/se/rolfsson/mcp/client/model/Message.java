package se.rolfsson.mcp.client.model;

import static se.rolfsson.mcp.client.Constants.BLUE;
import static se.rolfsson.mcp.client.Constants.GREEN;
import static se.rolfsson.mcp.client.Constants.RESET;
import static se.rolfsson.mcp.client.Constants.ROLE_ASSISTANT;
import static se.rolfsson.mcp.client.Constants.ROLE_TOOL;
import static se.rolfsson.mcp.client.Constants.USER;
import static se.rolfsson.mcp.client.utils.Utils.formatJsonString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import se.rolfsson.mcp.client.model.ChatResponse.ToolCalls;

@Slf4j
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Message(
    String role,
    String content,
    List<ToolCalls> toolCalls) {

  public static void addUserMessage(List<Message> messages, String content) {
    messages.add(Message.builder()
                     .role(USER)
                     .content(content)
                     .build());
    log.info(GREEN + "\n{}: {}\n", messages.getLast().role(), messages.getLast().content() + RESET);
  }

  public static void addToolCallRequestMessage(List<Message> messages, CallToolRequest toolCall) {
    messages.add(Message.builder()
                     .role(ROLE_ASSISTANT)
                     .toolCalls(List.of(new ToolCalls(toolCall)))
                     .build());
    log.debug(BLUE + "\nCalling tool:\nfunction: {}\narguments: {}\n" + RESET, toolCall.name(),
              formatJsonString(toolCall.arguments().toString()));
  }

  public static void addToolCallResponseMessage(List<Message> messages, String response) {
    messages.add(Message.builder()
                     .role(ROLE_TOOL)
                     .content(response)
                     .build());
    log.debug(BLUE + "\nTool response: {}\n" + RESET, formatJsonString(response));
  }
}
