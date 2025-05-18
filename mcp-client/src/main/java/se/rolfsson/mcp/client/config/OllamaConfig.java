package se.rolfsson.mcp.client.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "ollama")
public record OllamaConfig(
    @NotNull
    String model) {

}
