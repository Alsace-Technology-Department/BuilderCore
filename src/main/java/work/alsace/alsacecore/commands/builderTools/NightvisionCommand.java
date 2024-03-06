package work.alsace.alsacecore.commands.builderTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightvisionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.nightvision")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.sendMessage( ChatColor.GRAY + "已移除你身上的夜视药水效果");
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 3,false,false));
                player.sendMessage( ChatColor.GRAY + "已为你添加永久夜视药水效果");
            }
            return true;
        }
        return false;
    }
}
