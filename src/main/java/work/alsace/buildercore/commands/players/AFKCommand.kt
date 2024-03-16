package work.alsace.buildercore.commands.players

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import work.alsace.buildercore.BuilderCore

class AFKCommand(private val plugin: BuilderCore) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家执行")
            return false
        }
        if (args.isEmpty()) {
            toggleAFK(sender)
            return true
        } else {
            sender.sendMessage(
                """
    ${ChatColor.RED}正确指令:
    §f/afk §7- 进入暂离状态
    """.trimIndent()
            )
        }
        return false
    }

    private fun toggleAFK(player: Player) {
        if (player.hasMetadata("afk")) {
            player.sendMessage(ChatColor.GRAY.toString() + "你已取消暂离状态")
            player.removeMetadata("afk", plugin)
            Bukkit.broadcastMessage("§7玩家 §f" + player.name + " §7回来了")
        } else {
            player.sendMessage(ChatColor.GRAY.toString() + "你已进入暂离状态")
            player.setMetadata("afk", FixedMetadataValue(plugin, true))
            Bukkit.broadcastMessage("§7玩家 §f" + player.name + " §7暂时离开了")
        }
    }
}
