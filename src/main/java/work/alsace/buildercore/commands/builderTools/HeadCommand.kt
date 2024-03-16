package work.alsace.buildercore.commands.builderTools

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class HeadCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/head <玩家名称> §7- 获取指定玩家的皮肤头颅
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.head")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val head = createPlayerHead(strings[0])
            sender.inventory.addItem(head)
            sender.sendMessage(ChatColor.GRAY.toString() + "成功获得了玩家 §f" + strings[0] + " §7的头颅")
            return true
        }
        sender.sendMessage(error)
        return false
    }

    private fun createPlayerHead(playerName: String): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta?
        meta?.setOwningPlayer(Bukkit.getOfflinePlayer(playerName))
        head.setItemMeta(meta)
        return head
    }
}
