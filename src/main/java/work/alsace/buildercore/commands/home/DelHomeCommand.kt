package work.alsace.buildercore.commands.home

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import work.alsace.buildercore.service.HomeDataLoader

class DelHomeCommand(private val plugin: BuilderCore) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.delhome")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        if (args.size == 1) {
            val homeDataLoader: HomeDataLoader?
            val homeName: String
            if (args[0].contains(":") && sender.hasPermission("buildercore.commands.delhome.other")) {
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
                homeName = args[0]
                homeDataLoader = plugin.homeProfiles[sender.uniqueId]
            }
            if (homeDataLoader?.getHome(homeName) != null) {
                homeDataLoader.delHome(homeName)
                sender.sendMessage(String.format(ChatColor.GRAY.toString() + "已删除传送点%s", homeName))
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "不存在传送点" + homeName)
            }
        } else {
            sender.sendMessage(
                """
    ${ChatColor.GRAY}正确指令:
    §f/delhome <传送点> §7- 删除你的传送点
    §f/delhome <玩家>:<传送点> §7- 删除指定玩家的传送点
    """.trimIndent()
            )
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, s: String, args: Array<String>): List<String> {
        if (args.size != 1) return ArrayList(0)
        val list: MutableList<String> = ArrayList()
        if (sender is Player && args[0].contains(":")) {
            val user = args[0].split(":".toRegex(), limit = 2).toTypedArray()[0]
            val i = Bukkit.getServer().getPlayer(user)?.let { Bukkit.getServer().getOfflinePlayer(it.uniqueId) }
            val homeDataLoaderProfile: HomeDataLoader? = if (i?.isOnline == true) {
                plugin.homeProfiles[i.uniqueId]
            } else if (!i?.hasPlayedBefore()!!) {
                return ArrayList(0)
            } else {
                HomeDataLoader(i.uniqueId, plugin)
            }
            homeDataLoaderProfile?.getHomes()?.stream()?.map { s1: String? -> "$user:$s1" }?.let {
                list.addAll(
                    it
                        .toList()
                )
            }
        } else {
            for (p in Bukkit.getOnlinePlayers()) {
                list.add(p.name)
            }
            if (sender is Player) {
                plugin.homeProfiles[sender.uniqueId]?.let { homeDataLoader ->
                    homeDataLoader.getHomes().let { list.addAll(it) }
                }

            }
        }
        return list
    }
}
