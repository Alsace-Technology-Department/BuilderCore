package work.alsace.buildercore.commands.teleport

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import work.alsace.buildercore.service.TPAHandler

class TPACommand(private val plugin: BuilderCore) : CommandExecutor {
    private val tpaHandler: TPAHandler = plugin.getTPAHandler()

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.tp")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return true
        }
        if (label.equals("tpahere", ignoreCase = true)) {
            if (args.size != 1) {
                sender.sendMessage(
                    """
    ${ChatColor.RED}正确指令:
    §f/tpahere <玩家> §7- 请求玩家传送到你的位置
    """.trimIndent()
                )
                return false
            }
            val target = plugin.server.getPlayer(args[0])
            if (target === sender) {
                sender.sendMessage(ChatColor.RED.toString() + "你不能向自己发送传送请求")
                return false
            }
            return if (target != null && target.isOnline) {
                tpaHandler.sendTPARequest(sender, target)
                true
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "玩家 " + args[0] + " 不在线。")
                false
            }
        } else if (label.equals("tpaccept", ignoreCase = true)) {
            tpaHandler.acceptTPA(sender)
            sender.sendMessage(ChatColor.GRAY.toString() + "你接受了传送请求")
            return true
        } else if (label.equals("tpdeny", ignoreCase = true)) {
            tpaHandler.denyTPA(sender)
            sender.sendMessage(ChatColor.GRAY.toString() + "你拒绝了传送请求")
            return true
        }
        return false
    }
}
