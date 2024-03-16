package work.alsace.buildercore.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import work.alsace.buildercore.BuilderCore
import java.util.*

class AFKListener(private val plugin: BuilderCore) : Listener {
    private val lastActivity: MutableMap<UUID, Long> = HashMap()

    init {
        object : BukkitRunnable() {
            override fun run() {
                checkAFKPlayers()
            }
        }.runTaskTimer(plugin, (20 * 60).toLong(), (20 * 60).toLong())
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        lastActivity[player.uniqueId] = System.currentTimeMillis()
        cancelAFK(player)
    }

    @EventHandler
    fun beforePlayerJoin(event: AsyncPlayerPreLoginEvent) {
        val uuid = event.uniqueId
        lastActivity[uuid] = System.currentTimeMillis()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        lastActivity[player.uniqueId] = System.currentTimeMillis()
        cancelAFK(player)
    }

    private fun checkAFKPlayers() {
        val afkThreshold = plugin.config.getLong("afk.threshold", 300)
        val currentTime = System.currentTimeMillis()
        for (player in Bukkit.getOnlinePlayers()) {
            val playerId = player.uniqueId
            val lastActiveTime = lastActivity.getOrDefault(playerId, currentTime)
            if (currentTime - lastActiveTime > afkThreshold * 1000) {
                if (!player.hasPermission("buildercore.afk.bypass")) {
                    handleAFK(player)
                }
            } else {
                if (player.hasMetadata("afk")) {
                    player.sendMessage(ChatColor.GRAY.toString() + "你已取消暂离状态")
                    player.removeMetadata("afk", plugin)
                    Bukkit.broadcastMessage("§7玩家 §f" + player.name + " §7回来了")
                }
            }
        }
    }

    private fun handleAFK(player: Player) {
        if (!player.hasMetadata("afk")) {
            player.sendMessage(ChatColor.GRAY.toString() + "你已进入暂离状态")
            player.setMetadata("afk", FixedMetadataValue(plugin, true))
            Bukkit.broadcastMessage("§7玩家 §f" + player.name + " §7暂时离开了")
        }
    }

    private fun cancelAFK(player: Player) {
        if (player.hasMetadata("afk")) {
            player.sendMessage(ChatColor.GRAY.toString() + "你已取消暂离状态")
            player.removeMetadata("afk", plugin)
            Bukkit.broadcastMessage("§7玩家 §f" + player.name + " §7回来了")
        }
    }
}
