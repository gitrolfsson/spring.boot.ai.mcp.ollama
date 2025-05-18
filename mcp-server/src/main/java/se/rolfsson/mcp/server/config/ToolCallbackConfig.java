package se.rolfsson.mcp.server.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.rolfsson.mcp.server.service.TestFunctions;

@Configuration
public class ToolCallbackConfig {

  @Bean
  ToolCallbackProvider authorTools() {
    return MethodToolCallbackProvider
        .builder()
        .toolObjects(new TestFunctions())
        .build();
    //Optional: ToolCallbackProvider.from();
  }
}
