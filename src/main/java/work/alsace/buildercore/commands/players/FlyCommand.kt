package work.alsace.buildercore.commands.players

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty() && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.fly")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            if (sender.allowFlight) {
                sender.allowFlight = false
                sender.sendMessage(ChatColor.GRAY.toString() + "已将你的飞行模式设置为 §c关闭")
            } else {
                sender.allowFlight = true
                sender.sendMessage(ChatColor.GRAY.toString() + "已将你的飞行模式设置为 §a开启")
            }
            return true
        }
        if (strings.size == 1) {
            if (!sender.hasPermission("buildercore.commands.fly.other")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val player = Bukkit.getPlayer(strings[0])
            return if (player != null && player.isOnline) {
                if (player.allowFlight) {
                    player.allowFlight = false
                    sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + player.name + " §7的飞行模式设置为 §c关闭")
                    player.sendMessage(ChatColor.GRAY.toString() + "已将你的飞行模式设置为 §c关闭")
                } else {
                    player.allowFlight = true
                    sender.sendMessage(ChatColor.GRAY.toString() + "已将玩家 §f" + player.name + " §7的飞行模式设置为 §a开启")
                    player.sendMessage(ChatColor.GRAY.toString() + "已将你的飞行模式设置为 §a开启")
                }
                true
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "指定的玩家不在线或不存在")
                false
            }
        }
        sender.sendMessage(
            """
    ${ChatColor.GRAY}正确指令:
    §f/fly §7- 设置你的飞行状态
    §f/fly <玩家> §7- 设置指定玩家的飞行状态
    """.trimIndent()
        )
        return false
    }
}
