package com.puddingkc.events;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockEvent implements Listener {
    public static Set<Player> slabs = new HashSet<>();

    public BlockEvent() {
    }

    @EventHandler
    public void onBlockPhysics(BlockFadeEvent event) {
        if (event.getBlock().getType().name().contains("CORAL")) {
            event.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onPlayerInteractLight(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().name().equalsIgnoreCase("light")) {
                Block block = e.getClickedBlock();
                Levelled levelled = (Levelled) block.getBlockData();
                int level = levelled.getLevel();
                if (level >= 0 && level < 15) {
                    levelled.setLevel(level + 1);
                } else if (level == 15) {
                    levelled.setLevel(0);
                }

                e.setCancelled(true);
                block.setBlockData(levelled, true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.isCancelled() && slabs.contains(e.getPlayer()) && e.getPlayer().hasPermission("alsace.commands.slab")) {
            Material type = e.getPlayer().getInventory().getItemInMainHand().getType();
            if (type.toString().toLowerCase().contains("slab")) {
                if (e.isCancelled()) {
                    return;
                }

                if (e.getBlock().getType().toString().toLowerCase().contains("slab")) {
                    Slab blockData;
                    if (this.isTop(e.getPlayer(), e.getBlock())) {
                        blockData = (Slab) e.getBlock().getBlockData();
                        if (blockData.getType().equals(Type.DOUBLE)) {
                            blockData.setType(Type.BOTTOM);
                            e.getBlock().setBlockData(blockData, true);
                            e.setCancelled(true);
                        }
                    } else {
                        blockData = (Slab) e.getBlock().getBlockData();
                        if (blockData.getType().equals(Type.DOUBLE)) {
                            blockData.setType(Type.TOP);
                            e.getBlock().setBlockData(blockData, true);
                            e.setCancelled(true);
                        }
                    }
                }
            }

        }
    }

    private boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();

        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0) {
            start.add(player.getLocation().getDirection().multiply(0.05));
        }

        return start.getY() % 1.0 > 0.5;
    }
}
