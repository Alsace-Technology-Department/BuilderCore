package work.alsace.buildercore.commands.time

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class PTimeCommand : CommandExecutor, TabCompleter {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/ptime <时间> §7- 设置你自己的客户端时间
         §f/ptime <时间> <玩家> §7- 设置指定玩家的客户端时间
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.size != 1 && strings.size != 2) {
            sender.sendMessage(error)
            return false
        }
        if (sender !is Player && strings.size == 1) {
            sender.sendMessage(ChatColor.RED.toString() + "控制台无法执行该命令")
            return false
        }
        val targetPlayer: Player?
        if (strings.size == 2) {
            if (!sender.hasPermission("buildercore.commands.ptime.other")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            targetPlayer = Bukkit.getPlayer(strings[1])
            if (targetPlayer == null || !targetPlayer.isOnline) {
                sender.sendMessage(ChatColor.RED.toString() + "指定的玩家不在线或不存在")
                return false
            }
        } else {
            targetPlayer = sender as Player
            if (!targetPlayer.hasPermission("buildercore.commands.ptime")) {
                targetPlayer.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
        }
        val time: Long
        time = when (strings[0].lowercase(Locale.getDefault())) {
            "day" -> 0
            "night" -> 14000
            "sunrise" -> 23000
            "morning" -> 1000
            "noon" -> 6000
            "afternoon" -> 9000
            "sunset" -> 12000
            "midnight" -> 18000
            "reset" -> {
                targetPlayer.resetPlayerTime()
                targetPlayer.sendMessage(ChatColor.GRAY.toString() + "已将你的客户端时间重置")
                if (strings.size == 2) {
                    sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + targetPlayer.name + " §7的客户端时间重置")
                }
                return true
            }

            else -> {
                try {
                    strings[0].toLong()
                } catch (e: NumberFormatException) {
                    sender.sendMessage(error)
                    return false
                }
            }
        }
        targetPlayer.setPlayerTime(time, true)
        targetPlayer.sendMessage(ChatColor.GRAY.toString() + "已将你的客户端时间设置为 §f" + time + " ticks")
        if (strings.size == 2) {
            sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + targetPlayer.name + " §7的客户端时间设置为 §f" + time + " ticks")
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        s: String,
        strings: Array<String>
    ): List<String> {
        if (strings.size == 1 && sender.hasPermission("buildercore.commands.ptime")) {
            return times
        }
        if (strings.size == 2 && sender.hasPermission("buildercore.commands.ptime.other")) {
            val list: MutableList<String> = ArrayList()
            for (p in Bukkit.getOnlinePlayers()) {
                list.add(p.name)
            }
            return list
        }
        return emptyList()
    }

    companion object {
        private val times: List<String> =
            mutableListOf("day", "night", "sunrise", "morning", "noon", "afternoon", "sunset", "midnight", "reset")
    }
}
