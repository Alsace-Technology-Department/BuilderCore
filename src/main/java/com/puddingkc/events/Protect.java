package com.puddingkc.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Protect implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.break")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c你没有权限破坏方块");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.place")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c你没有权限放置方块");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.interact")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§c你没有权限与方块和物品进行交互");
        }
    }
}
