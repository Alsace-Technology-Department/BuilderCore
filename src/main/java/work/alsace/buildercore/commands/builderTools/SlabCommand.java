package work.alsace.buildercore.commands.builderTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.listeners.BlockListener;

public class SlabCommand implements CommandExecutor {
    public SlabCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("buildercore.commands.slab")) {
            return false;
        } else {
            Player player = (Player) sender;
            if (BlockListener.slabs.contains(player)) {
                BlockListener.slabs.remove(player);
                player.sendMessage(ChatColor.GRAY + "已禁用半砖破坏模式");
            } else {
                BlockListener.slabs.add(player);
                player.sendMessage(ChatColor.GRAY + "已启用半砖破坏模式");
            }

            return true;
        }
    }
}
