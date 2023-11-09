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

public class ptime implements CommandExecutor, TabCompleter {

    private final String error = "§7正确指令:\n§f/ptime <时间> §7- 设置你自己的客户端时间\n§f/ptime <时间> <玩家> §7- 设置指定玩家的客户端时间";
    private static final List<String> times = Arrays.asList("day", "night", "reset");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.ptime")) {
                player.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            switch (strings[0]) {
                case "day" -> {
                    player.setPlayerTime(0, true);
                    player.sendMessage("§7已将你的客户端时间设置为 §f" + "0" + " ticks");
                    return true;
                }
                case "night" -> {
                    player.setPlayerTime(14000, true);
                    player.sendMessage("§7已将你的客户端时间设置为 §f" + "14000" + " ticks");
                    return true;
                }
                case "reset" -> {
                    player.setPlayerTime(0,false);
                    player.sendMessage("§7已将你的客户端时间重置");
                    return true;
                }
                default -> {
                    try {
                        player.setPlayerTime(Long.parseLong(strings[0]), true);
                        player.sendMessage("§7已将你的客户端时间设置为 §f" + strings[0] + " ticks");
                        return true;
                    } catch (NumberFormatException e) {
                        player.sendMessage(error);
                        return false;
                    }
                }
            }
        }

        if (strings.length == 2) {
            if (!sender.hasPermission("alsace.commands.ptime.other")) {
                sender.sendMessage("§c你没有使用该命令的权限");
                return false;
            }

            Player player = Bukkit.getPlayer(strings[1]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage("§c指定的玩家不在线或不存在");
                return false;
            }

            switch (strings[0]) {
                case "day" -> {
                    player.setPlayerTime(0, true);
                    player.sendMessage("§7已将你的客户端时间设置为 §f" + "0" + " ticks");
                    sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的客户端时间设置为 §f" + "0" + " ticks");
                    return true;
                }
                case "night" -> {
                    player.setPlayerTime(14000, true);
                    player.sendMessage("§7已将你的客户端时间设置为 §f" + "14000" + " ticks");
                    sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的客户端时间设置为 §f" + "14000" + " ticks");
                    return true;
                }
                case "reset" -> {
                    player.setPlayerTime(0,false);
                    player.sendMessage("§7已将你的客户端时间重置");
                    return true;
                }
                default -> {
                    try {
                        player.setPlayerTime(Long.parseLong(strings[0]), true);
                        player.sendMessage("§7已将你的客户端时间设置为 §f" + strings[0] + " ticks");
                        sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的客户端时间设置为 §f" + strings[0] + " ticks");
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(error);
                        return false;
                    }
                }
            }
        }

        sender.sendMessage(error);
        return false;
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
