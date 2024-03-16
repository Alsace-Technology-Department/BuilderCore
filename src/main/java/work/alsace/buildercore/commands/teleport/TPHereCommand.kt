package work.alsace.buildercore.commands.teleport

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TPHereCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/tphere <玩家> §7- 将指定玩家传送到你的位置
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player && args.size == 1) {
            if (!sender.hasPermission("buildercore.commands.tphere")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val player1 = Bukkit.getPlayer(args[0])
            if (player1 == null || !player1.isOnline) {
                sender.sendMessage(ChatColor.RED.toString() + "指定的玩家不在线或不存在")
                return false
            }
            player1.teleport(sender.location)
            player1.sendMessage("§f" + sender.getName() + " §7将你传送到了他的位置")
            sender.sendMessage("§7已将 §f" + player1.name + " §7传送到你的位置")
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
