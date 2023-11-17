package com.puddingkc.commands.puddingUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugStickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.debugstick")) {
            return false;
        } else {
            Player player = (Player)sender;
            ItemStack itemStack = new ItemStack(Material.DEBUG_STICK, 1);
            player.getInventory().addItem(itemStack);
            player.sendMessage(ChatColor.GREEN + "你获得了一个调试棒");
            return true;
        }
    }
}
