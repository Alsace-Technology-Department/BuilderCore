package com.puddingkc.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class Misc implements Listener {

    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent event) {
        if (event.getBlock().getType().name().contains("CORAL")) {
            event.setCancelled(true);
        }
    }
}
