package work.alsace.buildercore.commands.builderTools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DebugStickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("buildercore.commands.debugstick")) {
            return false;
        } else {
            Player player = (Player) sender;
            ItemStack itemStack = new ItemStack(Material.DEBUG_STICK, 1);
            player.getInventory().addItem(itemStack);
            player.sendMessage(ChatColor.GRAY + "你获得了一个调试棒");
            return true;
        }
    }
}
