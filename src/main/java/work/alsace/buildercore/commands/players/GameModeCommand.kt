package work.alsace.buildercore.commands.players

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class GameModeCommand : CommandExecutor, TabCompleter {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/gamemode <模式> §7- 设置你的游戏模式
         §f/gamemode <模式> [玩家] §7- 设置指定玩家的游戏模式
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
            if (!sender.hasPermission("buildercore.commands.gamemode.other")) {
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
            if (!targetPlayer.hasPermission("buildercore.commands.gamemode")) {
                targetPlayer.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
        }
        val gameMode = parseGameMode(strings[0])
        if (gameMode != null) {
            targetPlayer.gameMode = gameMode
        }
        targetPlayer.sendMessage(ChatColor.GRAY.toString() + "已将你的游戏模式设置为 §f" + gameMode)
        if (strings.size == 2) {
            sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + targetPlayer.name + " §7的游戏模式设置为 §f" + gameMode)
        }
        return true
    }

    private fun parseGameMode(input: String): GameMode? {
        return when (input.lowercase(Locale.getDefault())) {
            "creative", "1" -> GameMode.CREATIVE
            "survival", "0" -> GameMode.SURVIVAL
            "spectator", "3" -> GameMode.SPECTATOR
            "adventure", "2" -> GameMode.ADVENTURE
            else -> null
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        s: String,
        strings: Array<String>
    ): List<String> {
        if (strings.size == 1 && sender.hasPermission("buildercore.commands.gamemode")) {
            return modes
        }
        if (strings.size == 2 && sender.hasPermission("buildercore.commands.gamemode.other")) {
            val list: MutableList<String> = ArrayList()
            for (p in Bukkit.getOnlinePlayers()) {
                list.add(p.name)
            }
            return list
        }
        return emptyList()
    }

    companion object {
        private val modes: List<String> = mutableListOf("adventure", "creative", "spectator", "survival")
    }
}
