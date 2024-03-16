package work.alsace.buildercore.commands.builderTools

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.utils.NoClipUtil

class NoClipCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        return if (sender !is Player) {
            false
        } else if (!sender.hasPermission("buildercore.commands.noclip")) {
            false
        } else {
            if (NoClipUtil.noclip.contains(sender)) {
                NoClipUtil.noclip.remove(sender)
                sender.sendMessage(ChatColor.GRAY.toString() + "已禁用自动穿墙")
            } else {
                NoClipUtil.noclip.add(sender)
                sender.sendMessage(ChatColor.GRAY.toString() + "已启用自动穿墙")
            }
            true
        }
    }
}
