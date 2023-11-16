package com.puddingkc.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 0 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.fly")) {
                player.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage("§7已将你的飞行模式设置为 §c关闭");
            } else {
                player.setAllowFlight(true);
                player.sendMessage("§7已将你的飞行模式设置为 §a开启");
            }
            return true;
        }

        if (strings.length == 1) {
            if (!sender.hasPermission("alsace.commands.fly.other")) {
                sender.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            Player player = Bukkit.getPlayer(strings[0]);
            if (player != null && player.isOnline()) {
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的飞行模式设置为 §c关闭");
                    player.sendMessage("§7已将你的飞行模式设置为 §c关闭");
                } else {
                    player.setAllowFlight(true);
                    sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的飞行模式设置为 §a开启");
                    player.sendMessage("§7已将你的飞行模式设置为 §a开启");
                }
                return true;
            } else {
                sender.sendMessage("§c指定的玩家不在线或不存在");
                return false;
            }
        }

        sender.sendMessage("§7正确指令:\n§f/fly §7- 设置你的飞行状态\n§f/fly <玩家> §7- 设置指定玩家的飞行状态");
        return false;
    }
}
