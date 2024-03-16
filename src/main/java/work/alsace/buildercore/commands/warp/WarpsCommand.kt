// WarpsCommand.java
package work.alsace.buildercore.commands.warp

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore
import java.util.function.Consumer

class WarpsCommand(private val plugin: BuilderCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.warps")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        val worldWarps = getWorldWarps(sender)
        if (worldWarps.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "没有可用的传送点")
            return false
        }
        sender.sendMessage(ChatColor.GRAY.toString() + "可用传送点:")
        for ((worldName, warps) in worldWarps) {
            val message = TextComponent(ChatColor.GRAY.toString() + worldName + ": ")
            for (warp in warps) {
                val warpComponent = createClickableWarpComponent(warp)
                message.addExtra(warpComponent)
                message.addExtra(" §7| ")
            }
            sender.spigot().sendMessage(message)
        }
        return true
    }

    private fun getWorldWarps(sender: Player): Map<String, MutableList<String>> {
        val worldWarps: MutableMap<String, MutableList<String>> = HashMap()
        plugin.warpProfiles["warps"]?.getWarps()?.forEach(Consumer { warp: String ->
            val worldName = plugin.warpProfiles["warps"]?.getWarpWorld(warp)?.name
            val alias = plugin.warpProfiles["warps"]?.getWarpAlias(warp)
            if (sender.hasPermission("multiverse.access.$worldName")) {
                worldName?.let {
                    worldWarps.computeIfAbsent(it) { ArrayList() }
                        .add(alias ?: warp)
                }
            }
        })
        return worldWarps
    }

    private fun createClickableWarpComponent(warp: String): TextComponent {
        val realWarp = plugin.warpProfiles["warps"]?.getRealWarpName(warp)
        val warpComponent = TextComponent(ChatColor.GOLD.toString() + warp)
        warpComponent.clickEvent =
            ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp $realWarp")
        val hoverText = TextComponent("点击快捷传送")
        warpComponent.hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            arrayOf<BaseComponent>(hoverText)
        )
        return warpComponent
    }
}
