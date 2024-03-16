package work.alsace.buildercore.commands.alias

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DerotCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f//derot [轴线 X,Y,Z] [度数] §7- 快捷执行创世神deform指令
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.size == 2 && sender is Player) {
            if (!sender.hasPermission("buildercore.aliases")) {
                sender.sendMessage(error)
                return false
            }
            val degrees: Int = try {
                strings[1].toInt()
            } catch (var8: Exception) {
                sender.sendMessage(error)
                return false
            }
            val radiansPerDegree = 0.0174533f
            val radian = degrees.toFloat() * radiansPerDegree
            if (strings[0].equals("x", ignoreCase = true)) {
                sender.performCommand("/deform rotate(y,z,$radian)")
            } else if (strings[0].equals("y", ignoreCase = true)) {
                sender.performCommand("/deform rotate(x,z,$radian)")
            } else if (strings[0].equals("z", ignoreCase = true)) {
                sender.performCommand("/deform rotate(x,y,$radian)")
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
