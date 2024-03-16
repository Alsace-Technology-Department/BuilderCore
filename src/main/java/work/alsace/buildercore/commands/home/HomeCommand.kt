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

class HomeCommand(private val plugin: BuilderCore) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.home")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        if (args.size == 1) {
            var homeName = args[0]
            if (homeName.contains(":") && sender.hasPermission("buildercore.commands.home.other")) {
                val user = homeName.split(":".toRegex(), limit = 2).toTypedArray()[0]
                val i = Bukkit.getServer().getOfflinePlayer(sender.uniqueId)
                val homeDataLoaderProfile: HomeDataLoader? = if (i.isOnline) {
                    plugin.homeProfiles[i.uniqueId]
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED.toString() + "玩家" + user + "不存在")
                    return false
                } else {
                    HomeDataLoader(sender.uniqueId, plugin)
                }
                homeName = homeName.replaceFirst("$user:".toRegex(), "")
                val location = homeDataLoaderProfile?.getHome(homeName)
                if (homeDataLoaderProfile != null) {
                    if (homeDataLoaderProfile.getHome(homeName) != null) {
                        if (location!!.world == null) {
                            sender.sendMessage(ChatColor.RED.toString() + "世界" + location.world + "不存在")
                            return false
                        }
                        sender.teleport(location)
                        sender.sendMessage(ChatColor.GRAY.toString() + "已传送至玩家" + user + "的家" + homeName)
                    } else {
                        sender.sendMessage(ChatColor.RED.toString() + "玩家" + user + "没有传送点" + homeName)
                        return false
                    }
                }
            } else {
                val homeDataLoader = plugin.homeProfiles[sender.uniqueId]
                val loc = homeDataLoader!!.getHome(homeName)
                if (loc == null) {
                    sender.sendMessage(ChatColor.RED.toString() + "你没有传送点" + homeName)
                    return false
                } else {
                    if (loc.world == null) {
                        sender.sendMessage(ChatColor.RED.toString() + "世界" + loc.world + "不存在")
                        return false
                    }
                    sender.teleport(loc)
                    sender.sendMessage(ChatColor.GRAY.toString() + "已传送至家" + homeName)
                }
            }
        } else {
            sender.sendMessage(
                """
    ${ChatColor.GRAY}正确指令:
    §f/home <传送点> §7- 传送至你的家
    §f/home <玩家>:<传送点> §7- 传送至指定玩家的家
    """.trimIndent()
            )
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, s: String, args: Array<String>): List<String> {
        if (args.size != 1) return ArrayList(0)
        val list: MutableList<String> = ArrayList()
        return if (sender is Player && args[0].contains(":")) {
            val user = args[0].split(":".toRegex(), limit = 2).toTypedArray()[0]
            val i = Bukkit.getServer().getOfflinePlayer(sender.uniqueId)
            val homeDataLoaderProfile: HomeDataLoader? = if (i.isOnline) {
                plugin.homeProfiles[i.uniqueId]
            } else if (!i.hasPlayedBefore()) {
                return ArrayList(0)
            } else {
                HomeDataLoader(i.uniqueId, plugin)
            }
            if (homeDataLoaderProfile != null) {
                list.addAll(homeDataLoaderProfile.getHomes().stream().map { s1: String? -> "$user:$s1" }.toList())
            }
            list
        } else {
            for (p in Bukkit.getOnlinePlayers()) {
                list.add(p.name)
            }
            if (sender is Player) {
                plugin.homeProfiles[sender.uniqueId]?.let { list.addAll(it.getHomes()) }
            }
            list
        }
    }
}
