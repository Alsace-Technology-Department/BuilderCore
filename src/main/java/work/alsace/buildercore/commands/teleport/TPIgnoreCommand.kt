package work.alsace.buildercore.commands.teleport

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore

class TPIgnoreCommand(private val plugin: BuilderCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        return if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家使用")
            true
        } else if (!sender.hasPermission("buildercore.commands.tpignore")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            true
        } else {
            val now = plugin.hasIgnored[sender.getName()]
            if (now != null) {
                if (now) {
                    plugin.hasIgnored[sender.getName()] = false
                    sender.sendMessage(ChatColor.GRAY.toString() + "已取消屏蔽强制传送")
                } else {
                    plugin.hasIgnored[sender.getName()] = true
                    sender.sendMessage(ChatColor.GRAY.toString() + "已成功屏蔽强制传送")
                }
            } else {
                plugin.hasIgnored[sender.getName()] = true
                sender.sendMessage(ChatColor.GRAY.toString() + "已成功屏蔽强制传送")
            }
            true
        }
    }
}
