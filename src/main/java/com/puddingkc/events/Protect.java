package com.puddingkc.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class Protect implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.break")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限破坏方块");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.place")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限放置方块");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        if (e.getClickedBlock().getState() instanceof Lectern) { return; }
        if (!e.getPlayer().hasPermission("alsace.protect.interact")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限与方块交互");
        }
    }

    @EventHandler
    public void onLectern(PlayerInteractEvent e){
        if (!e.getPlayer().hasPermission("alsace.protect.lectern") && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Objects.requireNonNull(e.getClickedBlock()).getState() instanceof Lectern) {
                Lectern lectern = (Lectern) e.getClickedBlock().getState();
                if (lectern.getInventory().getItem(0) == null) { return; }
                e.setCancelled(true);
                e.getPlayer().openBook(Objects.requireNonNull(lectern.getInventory().getItem(0)));
            }
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent e) {
        Player player = e.getPlayer();
        if (player != null && !player.hasPermission("alsace.protect.ignite")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限点燃方块");
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.sign")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限修改告示牌内容");
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        if (e.getPlayer() != null && !e.getPlayer().hasPermission("alsace.protect.item_frame")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限放置展示框");
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        Player player = null;
        if (e.getRemover() instanceof Player) {
            player = (Player)e.getRemover();
        } else if (e.getRemover() instanceof Projectile) {
            Projectile projectile = (Projectile)e.getRemover();
            if (projectile.getShooter() instanceof Player) {
                player = (Player)projectile.getShooter();
            }
        }
        if (player != null && !player.hasPermission("alsace.protect.item_frame")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "你没有权限破坏展示框");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Player player = null;
        if (e.getDamager() instanceof Player) {
            player = (Player) e.getDamager();
        } else if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player) {
                player = (Player) projectile.getShooter();
            }
        }
        if (player != null && e.getEntity().getType().toString().toUpperCase().contains("ITEM_FRAME") && !player.hasPermission("alsace.protect.item_frame")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "你没有权限破坏展示框");
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.item_frame") && e.getRightClicked().getType().toString().toUpperCase().contains("ITEM_FRAME")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限放置展示框");
        }
    }

    @EventHandler
    public void onDragonEgg(BlockFromToEvent e) {
        if (e.getBlock().getType() == Material.DRAGON_EGG) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractP(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL && !e.getPlayer().hasPermission("alsace.protect.farmland")) {
            Material blockType = e.getClickedBlock().getType();
            if (blockType == Material.FARMLAND) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.armor_stand")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限修改盔甲架");
        }
    }

    @EventHandler
    public void onArmorPlayerInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().hasPermission("alsace.protect.armor_stand") && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.ARMOR_STAND) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "你没有权限使用盔甲架");
        }
    }

    @EventHandler
    public void onArmorEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Player player = null;
        if (e.getDamager() instanceof Player) {
            player = (Player) e.getDamager();
        } else if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player) {
                player = (Player) projectile.getShooter();
            }
        }
        if (player != null && e.getEntity().getType().toString().toUpperCase().contains("ARMOR_STAND") && !player.hasPermission("alsace.protect.armor_stand")) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED + "你没有权限破坏盔甲架");
        }
    }
}
