package dev.syndek.utilities.command;

import java.util.HashMap;
import org.bukkit.command.Command;

public final class UtilitiesCommandMap {
  private static final HashMap<String, UtilitiesCommand> COMMAND_MAP = new HashMap<>();
  
  static {
    COMMAND_MAP.put("lore", new CommandLore());
    COMMAND_MAP.put("name", new CommandName());
    COMMAND_MAP.put("sign", new CommandSign());
    COMMAND_MAP.put("slurp", new CommandSlurp());
    COMMAND_MAP.put("nightvision", new CommandNightVision());
  }
  
  public static UtilitiesCommand getCommand(Command command) {
    return COMMAND_MAP.get(command.getName().toLowerCase());
  }
}
