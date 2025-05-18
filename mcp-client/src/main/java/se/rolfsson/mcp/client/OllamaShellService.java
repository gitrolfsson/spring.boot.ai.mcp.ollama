package se.rolfsson.mcp.client;

import static se.rolfsson.mcp.client.Constants.BLUE;
import static se.rolfsson.mcp.client.Constants.ERROR_MESSAGE_PREFIX;
import static se.rolfsson.mcp.client.Constants.RESET;
import static se.rolfsson.mcp.client.Constants.USER;
import static se.rolfsson.mcp.client.Constants.YELLOW;
import static se.rolfsson.mcp.client.model.Message.addToolCallRequestMessage;
import static se.rolfsson.mcp.client.model.Message.addToolCallResponseMessage;
import static se.rolfsson.mcp.client.model.Message.addUserMessage;
import static se.rolfsson.mcp.client.utils.Utils.getObjectAsJson;
import static se.rolfsson.mcp.client.utils.Utils.getPrettyJsonString;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestClient;
import se.rolfsson.mcp.client.model.ChatRequest;
import se.rolfsson.mcp.client.model.ChatResponse;
import se.rolfsson.mcp.client.model.ChatResponse.ToolCalls;
import se.rolfsson.mcp.client.model.Message;

@Slf4j
@ShellComponent
@ShellCommandGroup("OllamaShellService")
@RequiredArgsConstructor
public class OllamaShellService {

  private final RestClient ollamaClient;
  private final List<McpSyncClient> mcpSyncClientList;
  private final ObjectMapper objectMapper;
  private final ChatRequest chatRequest;
  private final List<Message> messages;

  @PostConstruct
  public void init() {
    log.info(BLUE + "\nAvailable tools: {}" + RESET, getPrettyJsonString(objectMapper, chatRequest.tools()));
  }

  @ShellMethod(value = "Adds a user prompt to previous Ollama history. Format: add \"<your prompt here>\"", key = "add")
  public String add(String prompt) {
    return promptLLM(prompt);
  }

  @ShellMethod(value = "Run a prompt using Ollama. Format: run \"<your prompt here>\"", key = "run")
  public String run(@ShellOption(defaultValue = "Get alternative fact about Donald Trump?") String prompt) {
    messages.clear();
    return promptLLM(prompt);
  }

  private String promptLLM(String prompt) {
    addUserMessage(messages, prompt);

    ChatResponse response;
    boolean hasToolCalls;
    do {

      response = ollamaChatRequest();
      removeOldErrorPromptsFromMessages();

      hasToolCalls = response.message().toolCalls() != null;
      if (hasToolCalls) {
        makeToolCallRequests(response.message().toolCalls());
      }
    }
    while (hasToolCalls);
    return YELLOW + response.message().content() + RESET;
  }

  private ChatResponse ollamaChatRequest() {
    ChatRequest request = chatRequest.toBuilder().messages(messages).build();
    log.debug("\nOllamaChatRequest:\n{}", getPrettyJsonString(objectMapper, request));
    ChatResponse body = ollamaClient.post()
        .uri("/chat")
        .body(request)
        .retrieve()
        .body(ChatResponse.class);
    log.debug("\nOllamaChatResponse:\n{}", getPrettyJsonString(objectMapper, body));
    return body;
  }

  private void removeOldErrorPromptsFromMessages() {
    messages.removeIf(message -> message.role().equals(USER) && message.content().startsWith(ERROR_MESSAGE_PREFIX));
  }

  private void makeToolCallRequests(List<ToolCalls> toolCalls) {
    for (ToolCalls toolCall : toolCalls) {
      processToolCall(toolCall);
    }
  }

  private void processToolCall(ToolCalls toolCall) {
    String name = toolCall.function().name();
    Map<String, Object> arguments = toolCall.function().arguments();

    mcpSyncClientList.stream()
        .filter(client -> hasMatchingTool(client, name, arguments))
        .findFirst()
        .ifPresentOrElse(
            client -> mcpClientRequest(client, toolCall.function()),
            () -> addFunctionArgumentErrorPrompt(name, arguments));
  }

  private boolean hasMatchingTool(McpSyncClient client, String name, Map<String, Object> arguments) {
    return client.listTools().tools().stream()
        .anyMatch(tool -> tool.name().equals(name) &&
            tool.inputSchema().properties().keySet().containsAll(arguments.keySet()));
  }

  private void mcpClientRequest(McpSyncClient client, CallToolRequest request) {
    CallToolResult callToolResult = client.callTool(request);
    if (callToolResult.content().getFirst() instanceof TextContent content) {
      addToolCallRequestMessage(messages, request);
      addToolCallResponseMessage(messages, content.text());
    }
  }

  private void addFunctionArgumentErrorPrompt(String name, Map<String, Object> arguments) {
    List<String> argumentSchemaMessages = mcpSyncClientList.stream()
        .flatMap(client -> client.listTools().tools().stream())
        .filter(tool -> tool.name().equals(name))
        .map(tool -> """
            There is a matching function with name %s, but the arguments do not match. Please provide %d properties according to the schema: %s
            """.formatted(tool.name(), tool.inputSchema().properties().size(),
                          getObjectAsJson(objectMapper, tool.inputSchema().properties())))
        .toList();
    String message = ERROR_MESSAGE_PREFIX + name + " and arguments: " + arguments + " not found. "
        + String.join("\n", argumentSchemaMessages);
    addUserMessage(messages, message);
  }
}
