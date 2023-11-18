package com.puddingkc.commands.fawe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UndoCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.undo")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        } else {
            ((Player) sender).performCommand("/undo");
            return true;
        }
    }
}
