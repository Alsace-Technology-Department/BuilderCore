package work.alsace.buildercore.commands.tools

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.math.floor

class WhoisCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/whois <玩家> §7- 查询指定玩家信息
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender.hasPermission("buildercore.commands.whois")) {
            val player = sender.server.getPlayer(strings[0])
            if (player == null) {
                sender.sendMessage(ChatColor.RED.toString() + "指定的玩家不在线或不存在")
                return false
            }
            val location = player.location
            val component = TextComponent("§7UUID: §a" + player.uniqueId)
            component.clickEvent = ClickEvent(
                ClickEvent.Action.COPY_TO_CLIPBOARD,
                player.uniqueId.toString()
            )
            val hoverText = TextComponent("点击复制")
            component.hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                arrayOf<BaseComponent>(hoverText)
            )
            sender.sendMessage("§7用户名: §f" + player.name)
            sender.spigot().sendMessage(component)
            sender.sendMessage("§7IP地址: §f" + (Objects.requireNonNull(player.address)?.hostString))
            sender.sendMessage("§7OP权限: §f" + player.isOp)
            sender.sendMessage("§7游戏模式: §f" + player.gameMode)
            sender.sendMessage(
                "§7坐标位置: §fX:" + floor(location.x) + " Y:" + floor(location.y) + " Z:" + floor(
                    location.z
                )
            )
            sender.sendMessage("§7所在世界: §f" + player.world.name)
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
