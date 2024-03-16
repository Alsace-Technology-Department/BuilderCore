package work.alsace.buildercore.commands.home

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import work.alsace.buildercore.service.HomeDataLoader

class SetHomeCommand(private val plugin: BuilderCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.sethome")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        if (args.size == 1) {
            var homeName = args[0]
            val homeDataLoader: HomeDataLoader?
            if (homeName.contains(":") && sender.hasPermission("buildercore.commands.sethome.other")) {
                val username = args[0].split(":".toRegex(), limit = 2).toTypedArray()[0]
                homeName = args[0].replaceFirst("$username:".toRegex(), "")
                val i = Bukkit.getServer().getOfflinePlayer(sender.uniqueId)
                homeDataLoader = if (i.isOnline) {
                    plugin.homeProfiles[i.uniqueId]
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED.toString() + "玩家不存在")
                    return false
                } else {
                    HomeDataLoader(i.uniqueId, plugin)
                }
            } else {
                homeDataLoader = plugin.homeProfiles[sender.uniqueId]
            }
            if (homeDataLoader?.getHome(homeName) != null) {
                sender.sendMessage(ChatColor.RED.toString() + "已经存在传送点" + homeName)
                return false
            }
            if (homeDataLoader?.getHomes()?.size!! >= homeDataLoader.getMaxHomes() && !sender.hasPermission("buildercore.commands.sethome.other")) {
                sender.sendMessage(ChatColor.RED.toString() + "你的家已经达到最大传送点数量")
                return false
            }
            homeDataLoader.addHome(homeName, sender.location)
            sender.sendMessage(String.format(ChatColor.GRAY.toString() + "成功设置传送点%s", homeName))
        } else {
            sender.sendMessage(
                """
    ${ChatColor.GRAY}正确指令:
    §f/sethome <传送点> §7- 设置你的家
    §f/sethome <玩家>:<传送点> §7- 设置指定玩家的家
    """.trimIndent()
            )
        }
        return true
    }
}
