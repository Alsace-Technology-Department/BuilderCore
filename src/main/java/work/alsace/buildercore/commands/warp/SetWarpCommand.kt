package work.alsace.buildercore.commands.warp

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import work.alsace.buildercore.service.WarpDataLoader

class SetWarpCommand(private val plugin: BuilderCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.setwarp")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        if (args.isNotEmpty()) {
            val warpName = args[0]
            val alias = if (args.size == 2) args[1] else null // 获取别名，如果提供的话
            val warpDataLoader: WarpDataLoader? = plugin.warpProfiles["warps"]
            if (warpDataLoader?.getWarp(warpName) != null) {
                sender.sendMessage(ChatColor.RED.toString() + "已经存在传送点" + warpName)
                return false
            }
            warpDataLoader?.addWarp(warpName, alias, sender.location)
            sender.sendMessage(String.format(ChatColor.GRAY.toString() + "成功设置传送点 %s", warpName))
        } else {
            sender.sendMessage(
                """
    ${ChatColor.GRAY}正确指令:
    §f/setwarp <传送点> §7- 设置传送点
    """.trimIndent()
            )
        }
        return true
    }
}
