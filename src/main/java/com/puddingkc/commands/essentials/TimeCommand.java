package com.puddingkc.commands.essentials;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    private final String error = ChatColor.GRAY + "正确指令:\n§f/time <时间> §7- 设置你当前世界的时间\n§f/time <时间> <世界> §7- 设置指定世界的时间";
    private static final List<String> times1 = Arrays.asList("sunrise", "day", "morning", "noon", "afternoon", "sunset", "night", "midnight", "5000");

    private static final List<String> times0 = Arrays.asList("sunrise", "day", "morning", "noon", "afternoon", "sunset", "night", "midnight", "5000", "set", "add");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.time")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            World world = player.getWorld();
            long timeTicks = getTimeFromKeyword(strings[0]);
            if (timeTicks == -1) {
                player.sendMessage(error);
                return false;
            }
            world.setTime(timeTicks);
            player.sendMessage(ChatColor.GRAY + "已将当前世界时间设置为 §f" + timeTicks + " ticks");
            return true;
        }

        if (strings.length == 2) {
            Player player = (Player) sender;
            switch (strings[0]) {
                case "set" -> {
                    if (!player.hasPermission("alsace.commands.time")) {
                        player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                        return false;
                    }
                    long timeTicks = getTimeFromKeyword(strings[1]);
                    if (timeTicks == -1) {
                        sender.sendMessage(error);
                        return false;
                    }
                    player.getWorld().setTime(timeTicks);
                    player.sendMessage(ChatColor.GRAY + "已将当前世界时间设置为 §f" + strings[1] + " ticks");
                    return true;
                }
                case "add" -> {
                    if (!player.hasPermission("alsace.commands.time")) {
                        player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                        return false;
                    }
                    if (!StringUtils.isNumeric(strings[1])) {
                        sender.sendMessage(error);
                        return false;
                    }
                    long var = Long.parseLong(strings[1]);
                    long var1 = player.getWorld().getTime();
                    player.getWorld().setTime(var + var1);
                    player.sendMessage(ChatColor.GRAY + "已将当前世界时间增加 §f" + var + " ticks");
                    return true;
                }
            }
            if (!sender.hasPermission("alsace.commands.time.other")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            World world = Bukkit.getWorld(strings[1]);
            if (world == null) {
                sender.sendMessage(ChatColor.RED + "指定的世界不存在");
                return false;
            }
            long timeTicks = getTimeFromKeyword(strings[0]);
            if (timeTicks == -1) {
                sender.sendMessage(error);
                return false;
            }
            world.setTime(timeTicks);
            sender.sendMessage(ChatColor.GRAY + "已将世界 §f" + strings[1] + " §7的时间设置为 §f" + timeTicks + " ticks");
            return true;
        }
        sender.sendMessage(error);
        return false;
    }

    private long getTimeFromKeyword(String keyword) {
        switch (keyword.toLowerCase()) {
            case "sunrise" -> {
                return 23000;
            }
            case "day" -> {
                return 0;
            }
            case "morning" -> {
                return 1000;
            }
            case "noon" -> {
                return 6000;
            }
            case "afternoon" -> {
                return 9000;
            }
            case "sunset" -> {
                return 12000;
            }
            case "night" -> {
                return 14000;
            }
            case "midnight" -> {
                return 18000;
            }
            default -> {
                try {
                    return Long.parseLong(keyword);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender.hasPermission("alsace.commands.time")) {
            return times0;
        }
        if (strings.length == 2 && strings[0].equals("set") && sender.hasPermission("alsace.commands.time")) {
            return times1;
        }
        if (strings.length == 2 && strings[0].equals("add") && sender.hasPermission("alsace.commands.time")) {
            List<String> list = new ArrayList<>();
            list.add("1000");
            return list;
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
