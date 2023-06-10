package dev.syndek.utilities;

import dev.syndek.utilities.command.UtilitiesCommandMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class UtilitiesPlugin extends JavaPlugin {
  public static String joinAndFormat(Player player, String[] args, int from, String permissionNode) {
    String string = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])args, from, args.length));
    if (player.hasPermission("utilities." + permissionNode + ".colour"))
      string = applyColour(string); 
    if (player.hasPermission("utilities." + permissionNode + ".format"))
      string = applyFormat(string); 
    return string;
  }
  
  private static String applyColour(String string) {
    char[] chars = string.toCharArray();
    for (int i = 0; i < chars.length - 1; i++) {
      if (chars[i] == '&' && "0123456789AaBbCcDdEeFfRr".indexOf(chars[i + 1]) > -1) {
        chars[i] = '§';
        chars[i + 1] = Character.toLowerCase(chars[i + 1]);
      } 
    } 
    return new String(chars);
  }
  
  private static String applyFormat(String string) {
    char[] chars = string.toCharArray();
    for (int i = 0; i < chars.length - 1; i++) {
      if (chars[i] == '&' && "KkLlMmNnOoRr".indexOf(chars[i + 1]) > -1) {
        chars[i] = '§';
        chars[i + 1] = Character.toLowerCase(chars[i + 1]);
      } 
    } 
    return new String(chars);
  }
  
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new LiftSignListener(), (Plugin)this);
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cError: §7You must be a player to do this.");
      return true;
    } 
    UtilitiesCommandMap.getCommand(command).execute((Player)sender, args);
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    return (sender instanceof Player) ? UtilitiesCommandMap.getCommand(command).tabComplete((Player)sender, args) : Collections.<String>emptyList();
  }
}
