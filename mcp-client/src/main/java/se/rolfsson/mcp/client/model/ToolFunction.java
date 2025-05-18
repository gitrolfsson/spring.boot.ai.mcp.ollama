package se.rolfsson.mcp.client.model;

import io.modelcontextprotocol.spec.McpSchema.JsonSchema;

public record ToolFunction(
    String name,
    String description,
    JsonSchema parameters) {}
