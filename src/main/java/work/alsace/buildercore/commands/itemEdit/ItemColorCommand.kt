package work.alsace.buildercore.commands.itemEdit

import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class ItemColorCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/itemcolor <16位颜色代码> §7- 修改手中物品(皮革)的颜色
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, strings: Array<String>): Boolean {
        if (strings.size == 1 && sender is Player) {
            if (!sender.hasPermission("buildercore.commands.itemcolor")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val itemInHand: ItemStack = sender.inventory.itemInMainHand
            if (itemInHand.type.isAir) {
                sender.sendMessage(ChatColor.RED.toString() + "你手中没有物品")
                return false
            }
            if (!isValidColorCode(strings[0].replace("#", ""))) {
                sender.sendMessage(ChatColor.RED.toString() + "无效的颜色代码")
                return false
            }
            if (itemInHand.itemMeta is LeatherArmorMeta) {
                try {
                    val colorCode = strings[0].replace("#", "").toInt(16)
                    (itemInHand.itemMeta as LeatherArmorMeta).setColor(Color.fromRGB(colorCode))
                    itemInHand.setItemMeta(itemInHand.itemMeta)
                    sender.sendMessage(ChatColor.GRAY.toString() + "物品颜色已成功修改")
                } catch (e: NumberFormatException) {
                    sender.sendMessage(ChatColor.RED.toString() + "无效的颜色代码")
                    return false
                }
            } else {
                sender.sendMessage(ChatColor.RED.toString() + "该物品不能修改颜色")
            }
            return true
        }
        sender.sendMessage(error)
        return false
    }

    private fun isValidColorCode(colorCode: String): Boolean {
        return colorCode.matches("[0-9A-Fa-f]{6}".toRegex())
    }
}
