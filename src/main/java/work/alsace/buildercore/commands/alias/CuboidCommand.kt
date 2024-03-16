package work.alsace.buildercore.commands.alias

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CuboidCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f//convex §7- 快捷执行创世神sel指令
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (sender is Player) {
            if (!sender.hasPermission("buildercore.aliases")) {
                sender.sendMessage(error)
                return false
            }
            sender.performCommand("/sel cuboid")
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
