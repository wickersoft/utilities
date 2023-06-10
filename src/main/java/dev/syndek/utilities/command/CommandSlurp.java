package dev.syndek.utilities.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class CommandSlurp implements UtilitiesCommand {
  public void execute(Player player, String[] args) {
    List<ItemStack> itemStacks = new ArrayList<>();
    int items = 0;
    int experience = 0;
    for (Entity entity : player.getNearbyEntities(16.0D, 16.0D, 16.0D)) {
      if (entity instanceof ExperienceOrb) {
        ExperienceOrb experienceOrb = (ExperienceOrb)entity;
        experienceOrb.teleport((Entity)player);
        experience += experienceOrb.getExperience();
        continue;
      } 
      if (entity instanceof Item) {
        ItemStack stack = ((Item)entity).getItemStack();
        itemStacks.add(stack);
        items += stack.getAmount();
        entity.remove();
      } 
    } 
    Map<Integer, ItemStack> failed = player.getInventory().addItem(itemStacks.<ItemStack>toArray(new ItemStack[0]));
    for (ItemStack stack : failed.values())
      player.getWorld().dropItem(player.getLocation(), stack); 
    if (items > 0 && experience == 0) {
      player.sendMessage("§7Slurped §a" + items + " §7item" + ((items == 1) ? "s" : "") + "!");
    } else if (items == 0 && experience > 0) {
      player.sendMessage("§7Slurped §a" + experience + " §7experience!");
    } else if (items > 0 && experience > 0) {
      player.sendMessage("§7Slurped §a" + items + "§7item" + ((items == 1) ? "s" : "") + " and §a" + experience + " §7experience!");
    } else {
      player.sendMessage("§cError: §7Nothing to slurp.");
    } 
  }
}
