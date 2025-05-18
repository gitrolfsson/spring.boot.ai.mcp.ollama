package se.rolfsson.mcp.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import se.rolfsson.mcp.client.model.ChatRequest;
import se.rolfsson.mcp.client.model.Tool;

@Configuration
public class Configs {

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule());
  }

  @Bean
  RestClient ollamaClient(RestClient.Builder restClient) {
    return restClient
        .baseUrl("http://localhost:11434/api")
        .build();
  }

  @Bean
  ChatRequest chatRequest(OllamaConfig ollamaConfig, List<Tool> tools) {
    return ChatRequest.builder()
        .model(ollamaConfig.model())
        .stream(false)
        .tools(tools)
        .build();
  }
}
