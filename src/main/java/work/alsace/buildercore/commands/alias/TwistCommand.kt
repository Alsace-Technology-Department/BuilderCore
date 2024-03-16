package work.alsace.buildercore.commands.alias

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TwistCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f//twist [轴线 X,Y,Z] [度数] §7- 快捷执行创世神deform指令
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 2 && sender is Player) {
            if (!sender.hasPermission("buildercore.aliases")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val degrees: Int = try {
                args[1].toInt()
            } catch (e: Exception) {
                sender.sendMessage(error)
                return false
            }
            val radiansPerDegree = 0.0174533f
            val radian = degrees * radiansPerDegree
            if (args[0].equals("x", ignoreCase = true)) {
                sender.performCommand("/deform rotate(y,z," + radian / 2.0f + "*(x+1))")
            } else if (args[0].equals("y", ignoreCase = true)) {
                sender.performCommand("/deform rotate(x,z," + radian / 2.0f + "*(y+1))")
            } else if (args[0].equals("z", ignoreCase = true)) {
                sender.performCommand("/deform rotate(x,y," + radian / 2.0f + "*(z+1))")
            } else {
                sender.sendMessage(error)
                return false
            }
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
