package work.alsace.buildercore.commands.weather

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class PWeatherCommand : CommandExecutor, TabCompleter {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/pweather <天气> §7- 设置你自己客户端的天气
         §f/pweather <天气> <玩家> §7- 设置指定玩家的客户端天气
         """.trimIndent()

    override fun onCommand(sender: CommandSender, cmd: Command, s: String, args: Array<String>): Boolean {
        //TODO 未完成
        return false
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, s: String, args: Array<String>): List<String>? {
        //TODO 未完成
        return null
    }

    companion object {
        private val weathers: List<String> = mutableListOf("clear", "rain", "thunder")
    }
}
