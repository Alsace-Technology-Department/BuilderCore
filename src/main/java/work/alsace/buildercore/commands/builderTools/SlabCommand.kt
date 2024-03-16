package work.alsace.buildercore.commands.builderTools

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import work.alsace.buildercore.listeners.BlockListener

class SlabCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        return if (sender !is Player) {
            false
        } else if (!sender.hasPermission("buildercore.commands.slab")) {
            false
        } else {
            if (BlockListener.slabs.contains(sender)) {
                BlockListener.slabs.remove(sender)
                sender.sendMessage(ChatColor.GRAY.toString() + "已禁用半砖破坏模式")
            } else {
                BlockListener.slabs.add(sender)
                sender.sendMessage(ChatColor.GRAY.toString() + "已启用半砖破坏模式")
            }
            true
        }
    }
}
