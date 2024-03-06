package work.alsace.alsacecore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static work.alsace.alsacecore.AlsaceCore.blockedCommands;

public class Misc implements Listener {

    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent event) {
        if (event.getBlock().getType().name().contains("CORAL")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        if (blockedCommands.contains(command.toLowerCase())) {
            if (event.getPlayer().hasPermission("alsace.blocked.bypass")) { return; }
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.WHITE + "Unknown command. Type \"/help\" for help.");
        }
    }
}
