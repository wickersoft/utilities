package dev.syndek.utilities.command;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class CommandNightVision implements UtilitiesCommand {
  private static final PotionEffect INFINITE_NIGHT_VISION = new PotionEffect(PotionEffectType.NIGHT_VISION, 2147483647, 0, false, false);
  
  public void execute(Player player, String[] args) {
    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
      player.removePotionEffect(PotionEffectType.NIGHT_VISION);
      player.sendMessage("§7Night vision §cdisabled§7.");
    } else {
      player.addPotionEffect(INFINITE_NIGHT_VISION);
      player.sendMessage("§7Night vision §aenabled§7.");
    } 
  }
}
