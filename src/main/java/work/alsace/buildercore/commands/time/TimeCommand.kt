package work.alsace.buildercore.commands.time

import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class TimeCommand : CommandExecutor, TabCompleter {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/time <时间> §7- 设置你当前世界的时间
         §f/time <时间> <世界> §7- 设置指定世界的时间
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.time")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val world: World = sender.world
            val timeTicks = getTimeFromKeyword(strings[0])
            if (timeTicks == -1L) {
                sender.sendMessage(error)
                return false
            }
            world.time = timeTicks
            sender.sendMessage(ChatColor.GRAY.toString() + "已将当前世界时间设置为 §f" + timeTicks + " ticks")
            return true
        }
        if (strings.size == 2) {
            val player = sender as Player
            when (strings[0]) {
                "set" -> {
                    if (!player.hasPermission("buildercore.commands.time")) {
                        player.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        return false
                    }
                    val timeTicks = getTimeFromKeyword(strings[1])
                    if (timeTicks == -1L) {
                        sender.sendMessage(error)
                        return false
                    }
                    player.world.time = timeTicks
                    player.sendMessage(ChatColor.GRAY.toString() + "已将当前世界时间设置为 §f" + strings[1] + " ticks")
                    return true
                }

                "add" -> {
                    if (!player.hasPermission("buildercore.commands.time")) {
                        player.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                        return false
                    }
                    if (!StringUtils.isNumeric(strings[1])) {
                        sender.sendMessage(error)
                        return false
                    }
                    val `var` = strings[1].toLong()
                    val var1 = player.world.time
                    player.world.time = `var` + var1
                    player.sendMessage(ChatColor.GRAY.toString() + "已将当前世界时间增加 §f" + `var` + " ticks")
                    return true
                }
            }
            if (!sender.hasPermission("buildercore.commands.time.other")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val world = Bukkit.getWorld(strings[1])
            if (world == null) {
                sender.sendMessage(ChatColor.RED.toString() + "指定的世界不存在")
                return false
            }
            val timeTicks = getTimeFromKeyword(strings[0])
            if (timeTicks == -1L) {
                sender.sendMessage(error)
                return false
            }
            world.time = timeTicks
            sender.sendMessage(ChatColor.GRAY.toString() + "已将世界 §f" + strings[1] + " §7的时间设置为 §f" + timeTicks + " ticks")
            return true
        }
        sender.sendMessage(error)
        return false
    }

    private fun getTimeFromKeyword(keyword: String): Long {
        return when (keyword.lowercase(Locale.getDefault())) {
            "sunrise" -> {
                21000
            }

            "day" -> {
                0
            }

            "morning" -> {
                3000
            }

            "noon" -> {
                6000
            }

            "afternoon" -> {
                9000
            }

            "sunset" -> {
                12000
            }

            "night" -> {
                15000
            }

            "midnight" -> {
                18000
            }

            else -> {
                try {
                    keyword.toLong()
                } catch (e: NumberFormatException) {
                    -1
                }
            }
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        s: String,
        strings: Array<String>
    ): List<String> {
        if (strings.size == 1 && sender.hasPermission("buildercore.commands.time")) {
            return times0
        }
        if (strings.size == 2 && strings[0] == "set" && sender.hasPermission("buildercore.commands.time")) {
            return times1
        }
        if (strings.size == 2 && strings[0] == "add" && sender.hasPermission("buildercore.commands.time")) {
            val list: MutableList<String> = ArrayList()
            list.add("1000")
            return list
        }
        if (strings.size == 2 && sender.hasPermission("buildercore.commands.time.other")) {
            val list: MutableList<String> = ArrayList()
            for (w in Bukkit.getWorlds()) {
                list.add(w.name)
            }
            return list
        }
        return emptyList()
    }

    companion object {
        private val times1: List<String> =
            mutableListOf("sunrise", "day", "morning", "noon", "afternoon", "sunset", "night", "midnight", "5000")
        private val times0: List<String> = mutableListOf(
            "sunrise",
            "day",
            "morning",
            "noon",
            "afternoon",
            "sunset",
            "night",
            "midnight",
            "5000",
            "set",
            "add"
        )
    }
}
