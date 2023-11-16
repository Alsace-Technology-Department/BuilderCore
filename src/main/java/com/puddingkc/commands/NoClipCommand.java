package com.puddingkc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;

public class NoClipCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.noclip")) {
            return false;
        } else {
            Player player = (Player)sender;
            if (AlsaceCore.noclip.contains(player)) {
                AlsaceCore.noclip.remove(player);
                player.sendMessage(ChatColor.GREEN + "已禁用自动穿墙");
            } else {
                AlsaceCore.noclip.add(player);
                player.sendMessage(ChatColor.GREEN + "已启用自动穿墙");
            }
//TODO 修改noclip的位置（alsace.ins）
            return true;
        }
    }
}
