package dev.syndek.utilities;

import java.util.EnumSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public final class LiftSignListener implements Listener {
   
  @EventHandler
  private void onSignChange(SignChangeEvent event) {
    int i;
    String liftLine = event.getLine(1);
    Block block = event.getBlock();
    if (liftLine == null)
      return; 
    switch (liftLine.toLowerCase()) {
      case "[lift up]":
        event.setLine(1, "[Lift Up]");
        for (i = 1; i <= 255 - block.getY(); i++) {
          Sign relative = blockAsSign(block.getRelative(0, i, 0));
          if (relative != null && relative.getLine(1).equals("[Lift Down]"))
            event.getPlayer().sendMessage("§7Lift sign linked!"); 
        } 
        break;
      case "[lift down]":
        event.setLine(1, "[Lift Down]");
        for (i = 1; i <= block.getY(); i++) {
          Sign relative = blockAsSign(block.getRelative(0, -i, 0));
          if (relative != null && relative.getLine(1).equals("[Lift Up]"))
            event.getPlayer().sendMessage("§7Lift sign linked!"); 
        } 
        break;
    } 
  }
  
  @EventHandler
  private void onSignClick(PlayerInteractEvent event) {
    int i;
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
      return; 
    Block block = event.getClickedBlock();
    Sign sign = blockAsSign(block);
    if (sign == null)
      return; 
    switch (sign.getLine(1)) {
      case "[Lift Up]":
        for (i = 1; i <= 255 - block.getY(); i++) {
          Sign relative = blockAsSign(block.getRelative(0, i, 0));
          if (relative != null && relative.getLine(1).equals("[Lift Down]")) {
            event.getPlayer().teleport(getLiftDestination(event.getPlayer().getLocation(), relative.getLocation()));
            return;
          } 
        } 
        event.getPlayer().sendMessage("§cError: §7This Lift sign is not linked.");
        break;
      case "[Lift Down]":
        for (i = 1; i <= block.getY(); i++) {
          Sign relative = blockAsSign(block.getRelative(0, -i, 0));
          if (relative != null && relative.getLine(1).equals("[Lift Up]")) {
            event.getPlayer().teleport(getLiftDestination(event.getPlayer().getLocation(), relative.getLocation()));
            return;
          } 
        } 
        event.getPlayer().sendMessage("§cError: §7This Lift sign is not linked.");
        break;
    } 
  }
  
  private Sign blockAsSign(Block block) {
    if (block == null)
      return null;
    return Tag.SIGNS.isTagged(block.getType()) ? (Sign)block.getState() : null;
  }
  
  private Location getLiftDestination(Location original, Location destination) {
    return destination.add(0.5D, 0.0D, 0.5D).setDirection(original.getDirection());
  }
}
