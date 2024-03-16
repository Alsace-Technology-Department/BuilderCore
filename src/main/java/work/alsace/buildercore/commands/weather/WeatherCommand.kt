package work.alsace.buildercore.commands.weather

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class WeatherCommand : CommandExecutor, TabCompleter {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/weather <天气> §7- 设置你当前世界的天气
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.weather")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val world: World = sender.world
            return when (strings[0]) {
                "clear" -> {
                    world.setStorm(false)
                    world.isThundering = false
                    sender.sendMessage("已将当前世界天气设置为 §f晴天")
                    true
                }

                "rain" -> {
                    world.setStorm(true)
                    world.isThundering = false
                    sender.sendMessage("已将当前世界天气设置为 §f雨天")
                    true
                }

                "thunder" -> {
                    world.setStorm(false)
                    world.isThundering = true
                    sender.sendMessage("已将当前世界天气设置为 §f雷暴")
                    true
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
        return if (strings.size == 1 && sender.hasPermission("buildercore.commands.weather")) {
            weathers
        } else emptyList()
    }

    companion object {
        private val weathers: List<String> = mutableListOf("clear", "rain", "thunder")
    }
}
