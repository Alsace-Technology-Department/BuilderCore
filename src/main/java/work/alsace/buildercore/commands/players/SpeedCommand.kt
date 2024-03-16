package work.alsace.buildercore.commands.players

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class SpeedCommand : CommandExecutor, TabCompleter {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/speed <速度> §7- 设置你的飞行或步行速度(0.1~10)
         §f/speed <类型> <速度> [玩家] §7- 设置指定玩家的指定类型速度
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.speed")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            return try {
                val speed = strings[0].toFloat()
                if (speed > 10 || speed < 0.1) {
                    sender.sendMessage(error)
                    return false
                }
                if (sender.isFlying) {
                    sender.flySpeed = speed / 10f
                    sender.sendMessage(ChatColor.GRAY.toString() + "已将你的 §f飞行 §7速度设置为 §f" + speed)
                } else {
                    sender.walkSpeed = speed / 10f
                    sender.sendMessage(ChatColor.GRAY.toString() + "已将你的 §f步行 §7速度设置为 §f" + speed)
                }
                true
            } catch (e: NumberFormatException) {
                sender.sendMessage(error)
                false
            }
        }
        if (strings.size == 2 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.speed")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            return when (strings[0]) {
                "walk" -> {
                    try {
                        val speed = strings[1].toFloat()
                        if (speed > 15 || speed < 0.1) {
                            sender.sendMessage(error)
                            return false
                        }
                        sender.walkSpeed = speed / 10f
                        sender.sendMessage(ChatColor.GRAY.toString() + "已将你的 §f步行 §7速度设置为 §f" + speed)
                        true
                    } catch (e: NumberFormatException) {
                        sender.sendMessage(error)
                        false
                    }
                }

                "fly" -> {
                    try {
                        val speed = strings[1].toFloat()
                        if (speed > 10 || speed < 0.1) {
                            sender.sendMessage(error)
                            return false
                        }
                        sender.flySpeed = speed / 10f
                        sender.sendMessage(ChatColor.GRAY.toString() + "已将你的 §f飞行 §7速度设置为 §f" + speed)
                        true
                    } catch (e: NumberFormatException) {
                        sender.sendMessage(error)
                        false
                    }
                }

                else -> {
                    sender.sendMessage(error)
                    false
                }
            }
        }
        if (strings.size == 3) {
            if (!sender.hasPermission("buildercore.commands.speed.other")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val player = Bukkit.getPlayer(strings[2])
            if (player == null || !player.isOnline) {
                sender.sendMessage(ChatColor.RED.toString() + "指定的玩家不在线或不存在")
                return false
            }
            return when (strings[0]) {
                "walk" -> {
                    try {
                        val speed = strings[1].toFloat()
                        if (speed > 10 || speed < 0.1) {
                            sender.sendMessage(error)
                            return false
                        }
                        player.walkSpeed = speed / 10f
                        sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + player.name + " §7的 §f步行 §7速度设置为 §f" + speed)
                        player.sendMessage(ChatColor.GRAY.toString() + "已将你的 §f步行 §7速度设置为 §f" + speed)
                        true
                    } catch (e: NumberFormatException) {
                        sender.sendMessage(error)
                        false
                    }
                }

                "fly" -> {
                    try {
                        val speed = strings[1].toFloat()
                        if (speed > 10 || speed < 0.1) {
                            sender.sendMessage(error)
                            return false
                        }
                        player.flySpeed = speed / 10f
                        sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + player.name + " §7的 §f飞行 §7速度设置为 §f" + speed)
                        player.sendMessage(ChatColor.GRAY.toString() + "已将你的 §f飞行 §7速度设置为 §f" + speed)
                        true
                    } catch (e: NumberFormatException) {
                        sender.sendMessage(error)
                        false
                    }
                }

                else -> {
                    sender.sendMessage(error)
                    false
                }
            }
        }
        sender.sendMessage(error)
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        s: String,
        strings: Array<String>
    ): List<String> {
        if (strings.size == 1 && sender.hasPermission("buildercore.commands.speed")) {
            return types
        }
        if (strings.size == 2 && sender.hasPermission("buildercore.commands.speed")) {
            return speeds
        }
        if (strings.size == 3 && sender.hasPermission("buildercore.commands.speed.other")) {
            val list: MutableList<String> = ArrayList()
            for (p in Bukkit.getOnlinePlayers()) {
                list.add(p.name)
            }
            return list
        }
        return emptyList()
    }

    companion object {
        private val types: List<String> = mutableListOf("walk", "fly", "1", "1.5", "1.75", "2")
        private val speeds: List<String> = mutableListOf("1", "1.5", "1.75", "2")
    }
}
