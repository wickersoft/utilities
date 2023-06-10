package dev.syndek.utilities.command;

import dev.syndek.utilities.UtilitiesPlugin;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public final class CommandSign implements UtilitiesCommand {
  private static final List<String> TAB_COMPLETE_OPTIONS = Arrays.asList(new String[] { "1", "2", "3", "4" });
  
  public void execute(Player player, String[] args) {
    BlockState state = player.getTargetBlock(null, 5).getState();
    if (!(state instanceof Sign)) {
      player.sendMessage("§cError: §7You must be targeting a sign to do this.");
      return;
    } 
    if (args.length < 2) {
      player.sendMessage("§cUsage: §7/sign <line> <text...>");
      return;
    } 
    try {
      int line = Integer.parseInt(args[0]);
      if (line >= 1 && line <= 4) {
        Sign sign = (Sign)state;
        sign.setLine(line - 1, UtilitiesPlugin.joinAndFormat(player, args, 1, "sign"));
        sign.update(true);
        return;
      } 
    } catch (NumberFormatException numberFormatException) {}
    player.sendMessage("§cInvalid line number: §7" + args[0]);
  }
  
  public List<String> tabComplete(Player player, String[] args) {
    return (args.length == 1) ? TAB_COMPLETE_OPTIONS : Collections.<String>emptyList();
  }
}
