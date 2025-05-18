# Spring AI MCP Server Client with Ollama

This is a demo project showcasing the integration of **Spring AI** with the **Model-Context-Protocol (MCP)** architecture, using *
*Ollama** for AI model management. The project demonstrates how to build a client-server application for interacting with AI
models using Spring Boot.

## Features

- **Spring AI Integration**: Leverages the `spring-ai` library for seamless AI model interactions.
- **MCP Protocol**: Implements the Model-Context-Protocol for managing AI tools and their functions.
- **Ollama Configuration**: Supports dynamic configuration of AI models via `application.yml`.
- **Spring Boot**: Built on the robust Spring Boot framework for rapid development.
- **Tool Management**: Dynamically retrieves and maps tools and their functions from the MCP server.

## Prerequisites

- **Java 21** or higher
- **Maven** for dependency management
- A running instance of an MCP server
- Ollama installed and configured

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
```

Build and run the server

```bash
mvn -pl mcp-server spring-boot:run
```

Build and run the client

```bash
mvn -pl mcp-client spring-boot:run
```

Test using the shell command <run> (will use default question) or enter a custom question in quotation marks.

```bash
shell:>run "Enter a question"
```
