package com.puddingkc.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConvexCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.convex")) {
            return false;
        } else {
            ((Player) sender).performCommand("/sel convex");
            return true;
        }
    }
}
