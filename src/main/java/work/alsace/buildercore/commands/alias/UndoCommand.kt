package work.alsace.buildercore.commands.alias

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UndoCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        return if (sender !is Player) {
            false
        } else if (!sender.hasPermission("buildercore.aliases")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            false
        } else {
            sender.performCommand("/undo")
            true
        }
    }
}
