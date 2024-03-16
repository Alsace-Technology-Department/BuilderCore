package work.alsace.buildercore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import work.alsace.buildercore.BuilderCore;

public class Misc implements Listener {
    private final BuilderCore plugin;

    public Misc(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent event) {
        if (event.getBlock().getType().name().contains("CORAL")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        if (plugin.getBlockedCommands().contains(command.toLowerCase())) {
            if (event.getPlayer().hasPermission("buildercore.blocked.bypass")) {
                return;
            }
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.WHITE + "Unknown command. Type \"/help\" for help.");
        }
    }
}
