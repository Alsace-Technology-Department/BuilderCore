package com.puddingkc.commands.fawe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CuboidCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.cuboid")) {
            return false;
        } else {
            ((Player)sender).performCommand("/sel cuboid");
            return true;
        }
    }
}
