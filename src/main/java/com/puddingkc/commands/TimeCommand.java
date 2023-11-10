package com.puddingkc.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {

    private final String error = "§7正确指令:\n§f/time <时间> §7- 设置你当前世界的时间\n§f/time <时间> <世界> §7- 设置指定世界的时间";
    private static final List<String> times = Arrays.asList("day", "night", "5000");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.time")) {
                player.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            World world = player.getWorld();
            switch (strings[0]) {
                case "day" -> {
                    world.setTime(0);
                    player.sendMessage("§7已将当前世界时间设置为 §f" + "0" + " ticks");
                    return true;
                }
                case "night" -> {
                    world.setTime(14000);
                    player.sendMessage("§7已将当前世界时间设置为 §f" + "14000" + " ticks");
                    return true;
                }
                default -> {
                    try {
                        world.setTime(Long.parseLong(strings[0]));
                        player.sendMessage("§7已将当前世界时间设置为 §f" + strings[0] + " ticks");
                        return true;
                    } catch (NumberFormatException e) {
                        player.sendMessage(error);
                        return false;
                    }
                }
            }
        }

        if (strings.length == 2) {
            if (!sender.hasPermission("alsace.commands.time.other")) {
                sender.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            World world = Bukkit.getWorld(strings[1]);
            if (world == null) {
                sender.sendMessage("§c指定的世界不存在");
                return false;
            }
            switch (strings[0]) {
                case "day" -> {
                    world.setTime(0);
                    sender.sendMessage("§7已将世界 §f" + strings[1] + " §7的时间设置为 §f" + "0" + " ticks");
                    return true;
                }
                case "night" -> {
                    world.setTime(14000);
                    sender.sendMessage("§7已将世界 §f" + strings[1] + " §7的时间设置为 §f" + "14000" + " ticks");
                    return true;
                }
                default -> {
                    try {
                        world.setTime(Long.parseLong(strings[0]));
                        sender.sendMessage("§7已将世界 §f" + strings[1] + " §7的时间设置为 §f" + strings[0] + " ticks");
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
        if (strings.length == 1 && sender.hasPermission("alsace.commands.time")) {
            return times;
        }
        if (strings.length == 2 && sender.hasPermission("alsace.commands.time.other")) {
            List<String> list = new ArrayList<>();
            for (World w : Bukkit.getWorlds()) {
                list.add(w.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
