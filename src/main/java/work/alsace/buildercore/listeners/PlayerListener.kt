package work.alsace.buildercore.listeners

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import work.alsace.buildercore.BuilderCore
import work.alsace.buildercore.commands.builderTools.AdvanceFlyCommand
import work.alsace.buildercore.commands.teleport.BackCommand
import work.alsace.buildercore.service.HomeDataLoader
import work.alsace.buildercore.utils.NoClipUtil
import java.util.*
import java.util.regex.Pattern

class PlayerListener(private val plugin: BuilderCore) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null
        BlockListener.slabs.add(event.player)
        plugin.homeProfiles[event.player.uniqueId] = HomeDataLoader(event.player.uniqueId, plugin)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        plugin.hasIgnored.remove(event.player.name)
        plugin.homeProfiles.remove(event.player.uniqueId)
        if (NoClipUtil.noclip.contains(event.player) || AdvanceFlyCommand.enabledPlayers.contains(
                event.player
            ) || BlockListener.slabs.contains(event.player)
        ) {
            NoClipUtil.noclip.remove(event.player)
            AdvanceFlyCommand.enabledPlayers.remove(event.player)
            BlockListener.slabs.remove(event.player)
        }
        event.player.removeMetadata("afk", plugin)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (event.cause == TeleportCause.SPECTATE && !event.player.hasPermission("buildercore.event.spectate")) {
            event.isCancelled = true
        }

        // 检查/back命令是否已注册
        if (plugin.getCommand("back") != null) {
            val backCommand = Objects.requireNonNull(plugin.getCommand("back"))?.executor as BackCommand
            backCommand.addToHistory(event.player)
        }
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        plugin.homeProfiles.remove(event.player.uniqueId)
    }

    @EventHandler
    fun playerAnvil(event: PrepareAnvilEvent) {
        val item = event.result
        if (item != null) {
            val meta = item.itemMeta
            if (meta != null && meta.hasDisplayName()) {
                var name = meta.displayName
                name = ChatColor.translateAlternateColorCodes('&', name)
                meta.setDisplayName(translateHexColorCodes(name))
                item.setItemMeta(meta)
                event.result = item
            }
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerFoodLevelChange(event: FoodLevelChangeEvent) {
        if (event.entity is Player) {
            event.isCancelled = true
        }
    }

    private fun translateHexColorCodes(message: String): String {
        val hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})")
        val matcher = hexPattern.matcher(message)
        val buffer = StringBuilder(message.length + 32)
        while (matcher.find()) {
            val group = matcher.group(1)
            val var10002 = group[0]
            matcher.appendReplacement(
                buffer,
                "§x§" + var10002 + "§" + group[1] + "§" + group[2] + "§" + group[3] + "§" + group[4] + "§" + group[5]
            )
        }
        return matcher.appendTail(buffer).toString()
    }
}
