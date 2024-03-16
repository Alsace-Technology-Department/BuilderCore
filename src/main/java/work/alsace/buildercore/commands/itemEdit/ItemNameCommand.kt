package work.alsace.buildercore.commands.itemEdit

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemNameCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/itemname <物品名称> §7- 修改手中物品名称
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.itemname")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val itemInHand: ItemStack = sender.inventory.itemInMainHand
            if (itemInHand.type.isAir) {
                sender.sendMessage(ChatColor.RED.toString() + "你手中没有物品")
                return false
            }
            val itemName = ChatColor.translateAlternateColorCodes('&', strings[0])
            val itemMeta = itemInHand.itemMeta
            itemMeta?.setDisplayName(itemName)
            itemInHand.setItemMeta(itemMeta)
            sender.sendMessage(ChatColor.GREEN.toString() + "§7已将手中物品名称修改为§r " + itemName)
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
