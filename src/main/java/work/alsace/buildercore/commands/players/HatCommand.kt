package work.alsace.buildercore.commands.players

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

class HatCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "该指令仅限玩家使用")
        } else {
            val inventory: PlayerInventory = sender.inventory
            val item = inventory.itemInMainHand
            inventory.setItemInMainHand(inventory.helmet)
            inventory.helmet = item
        }
        return true
    }
}
