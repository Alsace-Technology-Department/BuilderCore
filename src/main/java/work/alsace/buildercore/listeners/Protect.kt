package work.alsace.buildercore.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Lectern
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.PlayerArmorStandManipulateEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

class Protect : Listener {
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (!e.player.hasPermission("buildercore.protect.break")) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限破坏方块")
        }
    }

    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        if (!e.player.hasPermission("buildercore.protect.place")) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限放置方块")
        }
    }

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        if (Objects.requireNonNull(e.clickedBlock)?.state is Lectern) {
            return
        }
        if (!e.player.hasPermission("buildercore.protect.interact")) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限与方块交互")
        }
    }

    @EventHandler
    fun onLectern(e: PlayerInteractEvent) {
        if (!e.player.hasPermission("buildercore.protect.lectern") && e.action == Action.RIGHT_CLICK_BLOCK) {
            if (Objects.requireNonNull(e.clickedBlock)?.state is Lectern) {
                val lectern = e.clickedBlock!!.state as Lectern
                if (lectern.inventory.getItem(0) == null) {
                    return
                }
                e.isCancelled = true
                Objects.requireNonNull(lectern.inventory.getItem(0))?.let { e.player.openBook(it) }
            }
        }
    }

    @EventHandler
    fun onIgnite(e: BlockIgniteEvent) {
        val player = e.player
        if (player != null && !player.hasPermission("buildercore.protect.ignite")) {
            e.isCancelled = true
            e.player!!.sendMessage(ChatColor.RED.toString() + "你没有权限点燃方块")
        }
    }

    @EventHandler
    fun onSignChange(e: SignChangeEvent) {
        if (!e.player.hasPermission("buildercore.protect.sign")) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限修改告示牌内容")
        }
    }

    @EventHandler
    fun onHangingPlace(e: HangingPlaceEvent) {
        if (e.player != null && !e.player!!.hasPermission("buildercore.protect.item_frame")) {
            e.isCancelled = true
            e.player!!.sendMessage(ChatColor.RED.toString() + "你没有权限放置展示框")
        }
    }

    @EventHandler
    fun onHangingBreakByEntity(e: HangingBreakByEntityEvent) {
        var player: Player? = null
        if (e.remover is Player) {
            player = e.remover as Player?
        } else if (e.remover is Projectile) {
            val projectile: Projectile = e.remover as Projectile
            if (projectile.shooter is Player) {
                player = projectile.shooter as Player?
            }
        }
        if (player != null && !player.hasPermission("buildercore.protect.item_frame")) {
            e.isCancelled = true
            player.sendMessage(ChatColor.RED.toString() + "你没有权限破坏展示框")
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        var player: Player? = null
        if (e.damager is Player) {
            player = e.damager as Player
        } else if (e.damager is Projectile) {
            val projectile: Projectile = e.damager as Projectile
            if (projectile.shooter is Player) {
                player = projectile.shooter as Player?
            }
        }
        if (player != null && e.entity.type.toString().uppercase(Locale.getDefault())
                .contains("ITEM_FRAME") && !player.hasPermission("buildercore.protect.item_frame")
        ) {
            e.isCancelled = true
            player.sendMessage(ChatColor.RED.toString() + "你没有权限破坏展示框")
        }
    }

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        if (!e.player.hasPermission("buildercore.protect.item_frame") && e.rightClicked.type.toString()
                .uppercase(Locale.getDefault()).contains("ITEM_FRAME")
        ) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限放置展示框")
        }
    }

    @EventHandler
    fun onDragonEgg(e: BlockFromToEvent) {
        if (e.block.type == Material.DRAGON_EGG) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerInteractP(e: PlayerInteractEvent) {
        if (e.action == Action.PHYSICAL && !e.player.hasPermission("buildercore.protect.farmland")) {
            val blockType = Objects.requireNonNull(e.clickedBlock)?.type
            if (blockType == Material.FARMLAND) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerArmorStandManipulate(e: PlayerArmorStandManipulateEvent) {
        if (!e.player.hasPermission("buildercore.protect.armor_stand")) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限修改盔甲架")
        }
    }

    @EventHandler
    fun onArmorPlayerInteract(e: PlayerInteractEvent) {
        if (!e.player.hasPermission("buildercore.protect.armor_stand") && e.player.inventory.itemInMainHand.type == Material.ARMOR_STAND) {
            e.isCancelled = true
            e.player.sendMessage(ChatColor.RED.toString() + "你没有权限使用盔甲架")
        }
    }

    @EventHandler
    fun onArmorEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        var player: Player? = null
        if (e.damager is Player) {
            player = e.damager as Player
        } else if (e.damager is Projectile) {
            val projectile: Projectile = e.damager as Projectile
            if (projectile.shooter is Player) {
                player = projectile.shooter as Player?
            }
        }
        if (player != null && e.entity.type.toString().uppercase(Locale.getDefault())
                .contains("ARMOR_STAND") && !player.hasPermission("buildercore.protect.armor_stand")
        ) {
            e.isCancelled = true
            player.sendMessage(ChatColor.RED.toString() + "你没有权限破坏盔甲架")
        }
    }
}
