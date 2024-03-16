package work.alsace.buildercore.commands.itemEdit

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class ItemNbtCommand : CommandExecutor {
    private val error = """
         ${ChatColor.GRAY}正确指令:
         §f/itemnbt §7- 获取手中物品的详细信息
         """.trimIndent()

    override fun onCommand(sender: CommandSender, command: Command, label: String, strings: Array<String>): Boolean {
        if (sender is Player) {
            if (!sender.hasPermission("buildercore.commands.itemnbt")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            val itemInHand: ItemStack = sender.inventory.itemInMainHand
            if (itemInHand.type.isAir) {
                sender.sendMessage(ChatColor.RED.toString() + "你手中没有物品")
                return false
            }
            val component = TextComponent("§7物品NBT: §a" + Objects.requireNonNull(itemInHand.itemMeta).toString())
            component.clickEvent = ClickEvent(
                ClickEvent.Action.COPY_TO_CLIPBOARD,
                itemInHand.itemMeta.toString()
            )
            val hoverText = TextComponent("点击复制")
            component.hoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                arrayOf<BaseComponent>(hoverText)
            )
            sender.sendMessage("§7物品信息")
            sender.sendMessage("§7物品ID: §f" + itemInHand.type)
            sender.sendMessage("§7物品名称: §f" + (Objects.requireNonNull(itemInHand.itemMeta)?.displayName))
            sender.spigot().sendMessage(component)
            if (itemInHand.itemMeta!!.hasCustomModelData()) {
                sender.sendMessage("§7CustomModelData: §f" + itemInHand.itemMeta!!.customModelData)
            }
            return true
        }
        sender.sendMessage(error)
        return false
    }
}
