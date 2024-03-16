package work.alsace.buildercore.commands.weather

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class PWeatherCommand : CommandExecutor, TabCompleter {
    private val error: String =
        ChatColor.GRAY.toString() + "正确指令:\n§f/pweather <天气> §7- 设置你自己客户端的天气\n§f/pweather <天气> <玩家> §7- 设置指定玩家的客户端天气"

    override fun onCommand(cmdSender: CommandSender, cmd: Command, s: String, args: Array<String>): Boolean {
        if (cmdSender !is Player) {
            cmdSender.sendMessage(ChatColor.RED.toString() + "该命令只能由玩家执行")
            return false
        }

        if (!cmdSender.hasPermission("buildercore.commands.pweather")) {
            cmdSender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }

        val player: Player = cmdSender

        if (args.isEmpty() || args.size > 2) {
            player.sendMessage(error)
            return false
        }

        val weather: String = args[0].lowercase(Locale.getDefault())
        if (!weathers.contains(weather)) {
            player.sendMessage(ChatColor.RED.toString() + "请输入有效的天气类型: clear, rain")
            return false
        }

        if (args.size == 1) {
            return setWeather(player, weather)
        }

        val targetPlayer: Player? = player.server.getPlayer(args[1])

        return targetPlayer?.let { setWeather(it, weather) } == true
    }

    private fun setWeather(player: Player, weather: String): Boolean {
        when (weather) {
            "clear" -> {
                player.resetPlayerWeather()
                player.sendMessage(("已将" + player.name) + "的世界天气设置为 §f晴天")
                return true
            }

            "rain" -> {
                player.setPlayerWeather(org.bukkit.WeatherType.DOWNFALL)
                player.sendMessage(("已将" + player.name) + "的世界天气设置为 §f雨天")
                return true
            }

            else -> {
                player.sendMessage(error)
                return false
            }
        }
    }

    override fun onTabComplete(
        cmdSender: CommandSender,
        cmd: Command,
        s: String,
        args: Array<String>
    ): List<String> {
        if (args.size == 1 && cmdSender.hasPermission("buildercore.commands.pweather")) {
            return weathers
        }
        return emptyList()
    }

    companion object {
        private val weathers = mutableListOf("clear", "rain")
    }
}
