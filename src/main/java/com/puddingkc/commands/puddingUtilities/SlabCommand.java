package com.puddingkc.commands.puddingUtilities;

import com.puddingkc.events.BlockEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            if (BlockEvent.slabs.contains(player)) {
                BlockEvent.slabs.remove(player);
                player.sendMessage(ChatColor.GREEN + "已禁用半砖破坏模式");
            } else {
                BlockEvent.slabs.add(player);
                player.sendMessage(ChatColor.GREEN + "已启用半砖破坏模式");
            }

            return true;
        }
    }
}
