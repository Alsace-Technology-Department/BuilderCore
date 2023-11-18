package com.puddingkc.commands.fawe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CuboidCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f//convex §7- 快捷执行创世神sel指令";
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.cuboid")) {
                player.sendMessage(error);
                return false;
            }
            player.performCommand("/sel cuboid");
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
