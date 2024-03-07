package work.alsace.alsacecore.commands.builderTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.listeners.BlockListener;

public class SlabCommand implements CommandExecutor {
    public SlabCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.slab")) {
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
