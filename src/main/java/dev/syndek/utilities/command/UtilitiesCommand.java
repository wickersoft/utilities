package dev.syndek.utilities.command;

import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

public interface UtilitiesCommand {
  void execute(Player paramPlayer, String[] paramArrayOfString);
  
  default List<String> tabComplete(Player player, String[] args) {
    return Collections.emptyList();
  }
}
