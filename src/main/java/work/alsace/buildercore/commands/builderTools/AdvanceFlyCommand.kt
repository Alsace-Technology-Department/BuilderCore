package work.alsace.buildercore.commands.builderTools

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
import kotlin.math.abs

class AdvanceFlyCommand : CommandExecutor, Listener {
    private val lastVelocity = HashMap<String, Double>()
    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (sender !is Player) {
            return false
        }
        if (!sender.hasPermission("buildercore.commands.advfly")) {
            return false
        }
        if (enabledPlayers.contains(sender)) {
            enabledPlayers.remove(sender)
            if (sender.gameMode == GameMode.SPECTATOR) {
                sender.gameMode = GameMode.CREATIVE
            }
            sender.sendMessage(ChatColor.GRAY.toString() + "已禁用进阶飞行模式")
        } else {
            enabledPlayers.add(sender)
            sender.sendMessage(ChatColor.GRAY.toString() + "已启用进阶飞行模式")
        }
        return true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onMove(e: PlayerMoveEvent) {
        if (e.player.isFlying) {
            if (!enabledPlayers.contains(e.player)) {
                return
            }
            val speed = Objects.requireNonNull(e.to)?.clone()?.add(
                0.0, -e.to!!
                    .y, 0.0
            )?.let {
                e.from.clone().add(0.0, -e.from.y, 0.0).distance(
                    it
                )
            }
            if (abs(e.from.yaw - e.to!!.yaw) > 2.5) {
                return
            }
            if (abs(e.from.pitch - e.to!!.pitch) > 2.5) {
                return
            }
            if (lastVelocity.containsKey(e.player.name)) {
                val lastSpeed = lastVelocity[e.player.name]
                if (speed != null) {
                    if (speed * 1.3 < lastSpeed!!) {
                        if (slower.contains(e.player.name)) {
                            if (slower2.contains(e.player.name)) {
                                val v = e.player.velocity.clone()
                                v.setX(0)
                                v.setZ(0)
                                e.player.velocity = v
                                lastVelocity[e.player.name] = 0.0
                                slower.remove(e.player.name)
                                slower2.remove(e.player.name)
                            } else {
                                slower2.add(e.player.name)
                            }
                        } else {
                            slower.add(e.player.name)
                        }
                    } else if (speed > lastSpeed) {
                        lastVelocity[e.player.name] = speed
                        slower.remove(e.player.name)
                        slower2.remove(e.player.name)
                    }
                }
            } else {
                if (speed != null) {
                    lastVelocity[e.player.name] = speed
                    slower.remove(e.player.name)
                }
            }
        }
    }

    companion object {
        private val slower: MutableList<String> = ArrayList()
        private val slower2: MutableList<String> = ArrayList()
        var enabledPlayers: MutableSet<Player> = HashSet()
    }
}
