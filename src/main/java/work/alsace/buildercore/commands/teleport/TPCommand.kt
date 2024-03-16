package work.alsace.buildercore.commands.teleport

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import work.alsace.buildercore.BuilderCore
import java.util.*
import java.util.stream.Collectors
import kotlin.math.floor

class TPCommand(private val plugin: BuilderCore) : TabExecutor {
    override fun onTabComplete(sender: CommandSender, cmd: Command, label: String, args: Array<String>): List<String>? {
        return if (args.size != 1) {
            ArrayList(0)
        } else {
            val prefix = args[0].lowercase(Locale.getDefault())
            getAccessPlayers(sender as Player).stream().map { obj: Player -> obj.name }
                .filter { s: String -> s.lowercase(Locale.getDefault()).startsWith(prefix) }
                .collect(Collectors.toList())
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val x: Double
        val y: Double
        val z: Double
        val loc: Location
        val player: Player?
        when (args.size) {
            0 -> {
                sender.sendMessage(ChatColor.RED.toString() + "参数不足，请补全参数")
                return true
            }

            1 -> {
                if (sender is Player) {
                    player = Bukkit.getPlayer(args[0])
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED.toString() + "玩家" + args[0] + "不在线")
                        return true
                    }
                    val name = player.name
                    if (sender.world == player.world) {
                        if (!sender.hasPermission("buildercore.commands.tp")) {
                            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                            return true
                        }
                        if (plugin.hasIgnored[name] != null && plugin.hasIgnored[name] == true) {
                            sender.sendMessage(ChatColor.RED.toString() + "玩家" + name + "已屏蔽强制传送")
                            return true
                        }
                        teleportAfterDelay(sender, player)
                        sender.sendMessage(ChatColor.GRAY.toString() + "正在传送至" + name)
                    } else if (sender.hasPermission("buildercore.commands.tp") && sender.hasPermission("multiverse.access." + player.world.name)) {
                        if (plugin.hasIgnored[name] != null && plugin.hasIgnored[name] == true) {
                            sender.sendMessage(ChatColor.RED.toString() + "玩家" + name + "已屏蔽强制传送")
                            return true
                        }
                        teleportAfterDelay(sender, player)
                    } else {
                        sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令或传送到目标世界权限")
                    }
                    return true
                }
                sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
                return true
            }

            2 -> {
                if (sender is Player) {
                    if (!sender.hasPermission("buildercore.commands.tp")) {
                        sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        return false
                    }
                    if (!sender.hasPermission("buildercore.commands.tp.other")) {
                        sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        return false
                    } else {
                        val commander = Bukkit.getPlayer(args[0])
                        player = Bukkit.getPlayer(args[1])
                        if (commander == null) {
                            sender.sendMessage(ChatColor.RED.toString() + "玩家" + args[0] + "不在线")
                        } else {
                            if (player == null) {
                                sender.sendMessage(ChatColor.RED.toString() + "玩家" + args[1] + "不在线")
                                return true
                            }
                            teleportAfterDelay(commander, player)
                            sender.sendMessage(ChatColor.GRAY.toString() + "已将" + commander.name + "传送至" + player.name)
                        }
                    }
                    return true
                }
            }

            3 -> {
                if (sender is Player) {
                    if (!sender.hasPermission("buildercore.commands.tp.location")) {
                        sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        return true
                    }
                    try {
                        x = getLocation(1, sender, args[0])
                        y = getLocation(2, sender, args[1])
                        z = getLocation(3, sender, args[2])
                    } catch (var14: Exception) {
                        sender.sendMessage(ChatColor.RED.toString() + "输入坐标有误，请重新输入")
                        return true
                    }
                    loc = sender.location
                    sender.teleport(Location(sender.world, x, y, z, loc.yaw, loc.pitch))
                    sender.sendMessage(
                        String.format(
                            ChatColor.GRAY.toString() + "已将%s传送至（%.2f, %.2f, %.2f）",
                            sender.getName(),
                            x,
                            y,
                            z
                        )
                    )
                    return true
                }
                sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
                return true
            }

            4 -> {
                if (sender is Player) {
                    if (!sender.hasPermission("buildercore.commands.tp.location")) {
                        sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        return true
                    }
                    val senders = if (args[0] == "@s") sender else Bukkit.getPlayer(args[0])!!
                    return if (senders.name != senders.name && !senders.hasPermission("buildercore.commands.tp.other")) {
                        senders.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        true
                    } else {
                        try {
                            x = getLocation(1, sender, args[1])
                            y = getLocation(2, sender, args[2])
                            z = getLocation(3, sender, args[3])
                        } catch (var13: Exception) {
                            senders.sendMessage(ChatColor.RED.toString() + "输入坐标有误，请重新输入")
                            return true
                        }
                        loc = senders.location
                        senders.teleport(Location(senders.world, x, y, z, loc.yaw, loc.pitch))
                        senders.sendMessage(
                            String.format(
                                ChatColor.GRAY.toString() + "已将%s传送至（%.2f, %.2f, %.2f）",
                                senders.name,
                                x,
                                y,
                                z
                            )
                        )
                        true
                    }
                }
            }

            else -> {
                sender.sendMessage(ChatColor.RED.toString() + "未知操作")
                return true
            }
        }
        return false
    }

    private fun getLocation(pos: Int, sender: CommandSender, str: String): Double {
        var result: Double
        if (str.startsWith("~")) {
            require(sender is Player) { "The command sender is not a player, but the location contains '~'" }
            result = when (pos) {
                1 -> {
                    sender.location.x
                }

                2 -> {
                    sender.location.y
                }

                else -> {
                    sender.location.z
                }
            }
            if (str.length > 1) {
                result += str.substring(1, str.length - 1).toDouble()
            } else {
                result = floor(result) + 0.5
            }
        } else {
            result = str.toDouble()
        }
        return result
    }

    private fun getAccessPlayers(sender: Player): List<Player> {
        val players: MutableList<Player> = ArrayList()
        Bukkit.getOnlinePlayers().forEach { player: Player ->
            player.world
            if (sender.hasPermission("multiverse.access." + player.world.name)) players.add(player)
        }
        return players
    }

    private fun teleportAfterDelay(fromPlayer: Player, toPlayer: Player) {
        fromPlayer.sendMessage(ChatColor.GRAY.toString() + "正在传送至" + ChatColor.WHITE + toPlayer.name)
        toPlayer.sendMessage(fromPlayer.name + ChatColor.GRAY + " 正在传送到你身边...")
        object : BukkitRunnable() {
            override fun run() {
                fromPlayer.teleport(toPlayer)
                fromPlayer.sendMessage(ChatColor.GRAY.toString() + "你已成功传送到 " + toPlayer.name + " 身边！")
            }
        }.runTaskLater(plugin, 20L) // 1s
    }
}
