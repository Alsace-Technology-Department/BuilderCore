package work.alsace.buildercore.commands.builderTools

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DebugStickCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        return if (sender !is Player) {
            false
        } else if (!sender.hasPermission("buildercore.commands.debugstick")) {
            false
        } else {
            val itemStack = ItemStack(Material.DEBUG_STICK, 1)
            sender.inventory.addItem(itemStack)
            sender.sendMessage(ChatColor.GRAY.toString() + "你获得了一个调试棒")
            true
        }
    }
}
