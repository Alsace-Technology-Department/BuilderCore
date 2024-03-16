package work.alsace.buildercore.commands.alias

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ScaleCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f//scale [数值] §7- 快捷执行创世神deform指令
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.aliases")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val size: Double = try {
                args[0].toDouble()
            } catch (e: Exception) {
                sender.sendMessage(error)
                return false
            }
            sender.performCommand("/deform x/=$size;y/=$size;z/=$size")
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
