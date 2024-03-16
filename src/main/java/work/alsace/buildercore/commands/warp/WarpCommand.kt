package work.alsace.buildercore.commands.warp

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import java.util.function.Consumer

class WarpCommand(private val plugin: BuilderCore) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.warp")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        if (args.size == 1) {
            val warpName = args[0]
            val warpDataLoader = plugin.warpProfiles["warps"]
            val location = warpDataLoader!!.getWarp(warpName)
            if (location == null) {
                sender.sendMessage(ChatColor.RED.toString() + "传送点" + warpName + "不存在")
                return false
            }
            val warpWorld = location.world
            if (warpWorld != null) {
                if (sender.hasPermission("multiverse.access." + warpWorld.name)) {
                    sender.teleport(location)
                    sender.sendMessage(ChatColor.GRAY.toString() + "已传送至" + warpName)
                } else {
                    sender.sendMessage(ChatColor.RED.toString() + "你没有权限传送至" + warpName)
                    return false
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "世界" + warpWorld + "不存在")
                return false
            }
        } else {
            sender.sendMessage(
                """
    ${ChatColor.GRAY}正确指令:
    §f/warp <传送点> §7- 传送至指定传送点
    """.trimIndent()
            )
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, s: String, args: Array<String>): List<String> {
        return if (args.size != 1) ArrayList(0) else ArrayList(
            getAccessWarps(sender as Player)
        )
    }

    private fun getAccessWarps(sender: Player): List<String> {
        val warps: MutableList<String> = ArrayList()
        plugin.warpProfiles["warps"]?.getWarps()?.forEach(Consumer { warp: String ->
            val location = plugin.warpProfiles["warps"]?.getWarp(warp)
            val world = location!!.world
            if (world != null) {
                if (sender.hasPermission("multiverse.access." + world.name)) {
                    warps.add(warp)
                }
            }
        })
        return warps
    }
}
