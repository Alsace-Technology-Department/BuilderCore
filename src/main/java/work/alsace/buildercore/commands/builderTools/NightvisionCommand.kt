package work.alsace.buildercore.commands.builderTools

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class NightvisionCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (!sender.hasPermission("buildercore.commands.nightvision")) {
                sender.sendMessage(ChatColor.RED.toString() + "你没有使用该命令的权限")
                return false
            }
            if (sender.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                sender.removePotionEffect(PotionEffectType.NIGHT_VISION)
                sender.sendMessage(ChatColor.GRAY.toString() + "已移除你身上的夜视药水效果")
            } else {
                sender.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 3, false, false))
                sender.sendMessage(ChatColor.GRAY.toString() + "已为你添加永久夜视药水效果")
            }
            return true
        }
        return false
    }
}
