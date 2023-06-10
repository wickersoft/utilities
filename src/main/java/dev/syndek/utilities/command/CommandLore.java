package dev.syndek.utilities.command;

import dev.syndek.utilities.UtilitiesPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CommandLore implements UtilitiesCommand {
  private static final HashMap<UUID, List<String>> CLIPBOARD = new HashMap<>();
  
  private static final List<String> TAB_COMPLETE_OPTIONS = Arrays.asList(new String[] { "add", "set", "del", "insert", "copy", "paste", "clear" });
  
  public void execute(Player player, String[] args) {
    boolean success;
    if (args.length == 0) {
      player.sendMessage("§7Command usage:");
      player.sendMessage("§c- add <text | text | ...> §8- §7Add lines of lore to an item.");
      player.sendMessage("§c- set <line> <text...> §8- §7Set a line of lore on an item.");
      player.sendMessage("§c- del <line> §8- §7Delete a line of lore on an item.");
      player.sendMessage("§c- insert <line> <text | text | ...> §8- §7Insert one or more lines of lore at a given line on an item.");
      player.sendMessage("§c- clear §8- §7Clear an item's lore.");
      player.sendMessage("§c- copy §8- §7Copy an item's lore.");
      player.sendMessage("§c- paste §8- §7Paste copied lore onto an item.");
      return;
    } 
    ItemStack item = player.getInventory().getItemInMainHand();
    if (item.getType() == Material.AIR) {
      player.sendMessage("§cYou must be holding an item to do this!");
      return;
    } 
    ItemMeta meta = item.getItemMeta();
    if (meta == null) {
      player.sendMessage("§cLore cannot be applied to this item!");
      return;
    } 
    List<String> lore = (meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
    switch (args[0].toLowerCase()) {
      case "add":
        success = add(lore, player, args);
        break;
      case "set":
        success = set(lore, player, args);
        break;
      case "del":
        success = del(lore, player, args);
        break;
      case "insert":
        success = insert(lore, player, args);
        break;
      case "copy":
        success = copy(lore, player);
        break;
      case "paste":
        success = paste(lore, player);
        break;
      case "clear":
        success = clear(lore);
        break;
      default:
        player.sendMessage("§cError: §7Unknown command §c" + args[0] + "§7. Type §c/lore §7for command help.");
        return;
    } 
    if (success) {
      meta.setLore(lore);
      item.setItemMeta(meta);
      player.sendMessage("§7Lore updated!");
    } 
  }
  
  public List<String> tabComplete(Player sender, String[] args) {
    return (args.length == 1) ? TAB_COMPLETE_OPTIONS : Collections.<String>emptyList();
  }
  
  private boolean add(List<String> lore, Player player, String[] args) {
    if (args.length == 1) {
      player.sendMessage("§cUsage: §7/lore add <text | text | ...>");
      return false;
    } 
    lore.addAll(Arrays.asList(UtilitiesPlugin.joinAndFormat(player, args, 1, "lore").split("\\|")));
    return true;
  }
  
  private boolean set(List<String> lore, Player player, String[] args) {
    if (args.length < 3) {
      player.sendMessage("§cUsage: §7/lore set <line> <text...>");
      return false;
    } 
    try {
      int lineNumber = Integer.parseInt(args[1]);
      if (lineNumber > 0 && lineNumber <= lore.size()) {
        lore.set(lineNumber - 1, UtilitiesPlugin.joinAndFormat(player, args, 2, "lore"));
        return true;
      } 
    } catch (NumberFormatException numberFormatException) {}
    player.sendMessage("§cInvalid line number: §7" + args[1]);
    return false;
  }
  
  private boolean del(List<String> lore, Player player, String[] args) {
    if (args.length < 2) {
      player.sendMessage("§cUsage: §7/lore del <line>");
      return false;
    } 
    try {
      int lineNumber = Integer.parseInt(args[1]);
      if (lineNumber > 0 && lineNumber <= lore.size()) {
        lore.remove(lineNumber - 1);
        return true;
      } 
    } catch (NumberFormatException numberFormatException) {}
    player.sendMessage("§cInvalid line number: §7" + args[1]);
    return false;
  }
  
  private boolean insert(List<String> lore, Player player, String[] args) {
    if (args.length < 3) {
      player.sendMessage("§cUsage: §7/lore insert <line> <text | text | ...>");
      return false;
    } 
    try {
      int lineNumber = Integer.parseInt(args[1]);
      if (lineNumber > 0 && lineNumber <= lore.size()) {
        String[] loreLines = UtilitiesPlugin.joinAndFormat(player, args, 2, "lore").split("\\|");
        LinkedList<String> newLore = new LinkedList<>();
        int i;
        for (i = 0; i < lineNumber; i++)
          newLore.add(lore.get(i)); 
        newLore.addAll(Arrays.asList(loreLines));
        for (i = lineNumber; i < lore.size(); i++)
          newLore.add(lore.get(i)); 
        lore.clear();
        lore.addAll(newLore);
        return true;
      } 
    } catch (NumberFormatException numberFormatException) {}
    player.sendMessage("§cInvalid line number: §7" + args[1]);
    return false;
  }
  
  private boolean copy(List<String> lore, Player player) {
    if (lore.isEmpty()) {
      player.sendMessage("§cError: §7There is no lore on this item.");
      return false;
    } 
    CLIPBOARD.put(player.getUniqueId(), lore);
    player.sendMessage("§7Lore copied!");
    return false;
  }
  
  private boolean paste(List<String> lore, Player player) {
    if (CLIPBOARD.containsKey(player.getUniqueId())) {
      lore.clear();
      lore.addAll(CLIPBOARD.get(player.getUniqueId()));
      return true;
    } 
    player.sendMessage("§cError: §7You have no lore copied.");
    return false;
  }
  
  private boolean clear(List<String> lore) {
    lore.clear();
    return true;
  }
}
