package work.alsace.buildercore.commands.home

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
import java.util.stream.Collectors

class HomesCommand(private val plugin: BuilderCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (!sender.hasPermission("buildercore.commands.homes")) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
            return false
        }
        val homeDataLoader = plugin.homeProfiles[sender.uniqueId]
        val homeComponents = homeDataLoader!!.getHomes().stream()
            .map { home: String? -> createClickableHomeComponent(home) }
            .collect(Collectors.toList())
        if (homeComponents.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "你没有任何传送点")
            return false
        }
        val message = TextComponent(ChatColor.GRAY.toString() + "你的家: ")
        for (component in homeComponents) {
            message.addExtra(component)
            message.addExtra(" §7| ")
        }
        sender.spigot().sendMessage(message)
        return true
    }

    private fun createClickableHomeComponent(home: String?): TextComponent {
        val homeComponent = TextComponent(ChatColor.GOLD.toString() + home)
        homeComponent.clickEvent =
            ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home $home")
        val hoverText = TextComponent("点击快捷传送")
        homeComponent.hoverEvent = HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            arrayOf<BaseComponent>(hoverText)
        )
        return homeComponent
    }
}
