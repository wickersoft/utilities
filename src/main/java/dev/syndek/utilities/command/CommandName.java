package dev.syndek.utilities.command;

import dev.syndek.utilities.UtilitiesPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CommandName implements UtilitiesCommand {
  public void execute(Player player, String[] args) {
    ItemStack item = player.getInventory().getItemInMainHand();
    if (item.getType() == Material.AIR) {
      player.sendMessage("§cError: §7You must be holding an item to do this.");
      return;
    } 
    ItemMeta meta = item.getItemMeta();
    if (meta == null) {
      player.sendMessage("§cError: §7A custom name cannot be applied to this item.");
      return;
    } 
    if (args.length == 0) {
      player.sendMessage("§cUsage: §7/name <reset | text...>");
      return;
    } 
    if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
      meta.setDisplayName(null);
    } else {
      meta.setDisplayName(UtilitiesPlugin.joinAndFormat(player, args, 0, "name"));
    } 
    item.setItemMeta(meta);
    player.sendMessage("§7Name updated!");
  }
}
