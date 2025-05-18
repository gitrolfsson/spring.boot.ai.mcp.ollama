package se.rolfsson.mcp.client.config;

import io.modelcontextprotocol.client.McpSyncClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.rolfsson.mcp.client.model.Tool;
import se.rolfsson.mcp.client.model.ToolFunction;

@Configuration
public class MpcClientConfig {

  @Bean
  List<Tool> chatClientTools(List<McpSyncClient> mcpSyncClientList) {
    //new SyncMcpToolCallbackProvider(mcpSyncClientList)
    return mcpSyncClientList.stream()
        .flatMap(client -> client.listTools().tools().stream())
        .map(tool -> new Tool("function",
                              new ToolFunction(
                                  tool.name(),
                                  tool.description(),
                                  tool.inputSchema())))
        .toList();
  }
}
