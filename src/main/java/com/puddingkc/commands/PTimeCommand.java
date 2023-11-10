package com.puddingkc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PTimeCommand implements CommandExecutor, TabCompleter {

    private final String error = "§7正确指令:\n§f/ptime <时间> §7- 设置你自己的客户端时间\n§f/ptime <时间> <玩家> §7- 设置指定玩家的客户端时间";
    private static final List<String> times = Arrays.asList("day", "night", "reset");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length != 1 && strings.length != 2) {
            sender.sendMessage(error);
            return false;
        }

        if (!(sender instanceof Player) && strings.length == 1) {
            sender.sendMessage("§c控制台无法执行该命令");
            return false;
        }

        Player targetPlayer;
        if (strings.length == 2) {
            if (!sender.hasPermission("alsace.commands.ptime.other")) {
                sender.sendMessage("§c你没有使用该命令的权限");
                return false;
            }

            targetPlayer = Bukkit.getPlayer(strings[1]);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                sender.sendMessage("§c指定的玩家不在线或不存在");
                return false;
            }
        } else {
            targetPlayer = (Player) sender;
            if (!targetPlayer.hasPermission("alsace.commands.ptime")) {
                targetPlayer.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
        }

        long time;
        switch (strings[0].toLowerCase()) {
            case "day" -> time = 0;
            case "night" -> time = 14000;
            case "reset" -> {
                targetPlayer.setPlayerTime(0, false);
                targetPlayer.sendMessage("§7已将你的客户端时间重置");
                if (strings.length == 2) {
                    sender.sendMessage("§7已将玩家 §f" + targetPlayer.getName() + " §7的客户端时间重置");
                }
                return true;
            }
            default -> {
                try {
                    time = Long.parseLong(strings[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(error);
                    return false;
                }
            }
        }

        targetPlayer.setPlayerTime(time, true);
        targetPlayer.sendMessage("§7已将你的客户端时间设置为 §f" + time + " ticks");

        if (strings.length == 2) {
            sender.sendMessage("§7已将玩家 §f" + targetPlayer.getName() + " §7的客户端时间设置为 §f" + time + " ticks");
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender.hasPermission("alsace.commands.ptime")) {
            return times;
        }
        if (strings.length == 2 && sender.hasPermission("alsace.commands.ptime.other")) {
            List<String> list = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
