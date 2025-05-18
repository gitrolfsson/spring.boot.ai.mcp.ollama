package se.rolfsson.mcp.client.config;

import static se.rolfsson.mcp.client.Constants.GREEN;
import static se.rolfsson.mcp.client.Constants.RESET;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLister {

  private final CommandCatalog commandCatalog;

  @EventListener(ApplicationStartedEvent.class)
  public void ListCommands() {
    System.out.println(GREEN + "Commands:");
    for (Map.Entry<String, CommandRegistration> entry : commandCatalog.getRegistrations().entrySet()) {
      String commandName = entry.getKey();
      CommandRegistration commandRegistration = entry.getValue();
      if (!commandRegistration.getGroup().equals("Built-In Commands")) {
        System.out.println(commandName + " - " + commandRegistration.getDescription());
      }
    }
    System.out.println("help - Help" + RESET);
  }
}
