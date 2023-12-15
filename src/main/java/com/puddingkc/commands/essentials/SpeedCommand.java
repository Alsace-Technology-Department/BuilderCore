package com.puddingkc.commands.essentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpeedCommand implements CommandExecutor, TabCompleter {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/speed <速度> §7- 设置你的飞行或步行速度(0.1~15)\n§f/speed <类型> <速度> [玩家] §7- 设置指定玩家的指定类型速度";
    private static final List<String> types = Arrays.asList("walk", "fly", "1", "1.5", "1.75", "2");
    private static final List<String> speeds = Arrays.asList("1", "1.5", "1.75", "2");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.speed")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            try {
                float speed = Float.parseFloat(strings[0]);
                if (speed > 15 || speed < 0.1) {
                    player.sendMessage(error);
                    return false;
                }
                if (player.isFlying()) {
                    player.setFlySpeed(speed/10F);
                    player.sendMessage(ChatColor.GRAY + "已将你的 §f飞行 §7速度设置为 §f" + speed);
                } else {
                    player.setWalkSpeed(speed/10F);
                    player.sendMessage(ChatColor.GRAY + "已将你的 §f步行 §7速度设置为 §f" + speed);
                }
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(error);
                return false;
            }
        }

        if (strings.length == 2 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.speed")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            switch (strings[0]) {
                case "walk" -> {
                    try {
                        float speed = Float.parseFloat(strings[1]);
                        if (speed > 15 || speed < 0.1) {
                            player.sendMessage(error);
                            return false;
                        }
                        player.setWalkSpeed(speed / 10F);
                        player.sendMessage(ChatColor.GRAY + "已将你的 §f步行 §7速度设置为 §f" + speed);
                        return true;
                    } catch (NumberFormatException e) {
                        player.sendMessage(error);
                        return false;
                    }
                }
                case "fly" -> {
                    try {
                        float speed = Float.parseFloat(strings[1]);
                        if (speed > 15 || speed < 0.1) {
                            player.sendMessage(error);
                            return false;
                        }
                        player.setFlySpeed(speed / 10F);
                        player.sendMessage(ChatColor.GRAY + "已将你的 §f飞行 §7速度设置为 §f" + speed);
                        return true;
                    } catch (NumberFormatException e) {
                        player.sendMessage(error);
                        return false;
                    }
                }
                default -> {
                    player.sendMessage(error);
                    return false;
                }
            }
        }

        if (strings.length == 3) {
            if (!sender.hasPermission("alsace.commands.speed.other")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            Player player = Bukkit.getPlayer(strings[2]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage(ChatColor.RED + "指定的玩家不在线或不存在");
                return false;
            }

            switch (strings[0]) {
                case "walk" -> {
                    try {
                        float speed = Float.parseFloat(strings[1]);
                        if (speed > 15 || speed < 0.1) {
                            sender.sendMessage(error);
                            return false;
                        }
                        player.setWalkSpeed(speed / 10F);
                        sender.sendMessage(ChatColor.GRAY + "已将玩家 §f" + player.getName() + " §7的 §f步行 §7速度设置为 §f" + speed);
                        player.sendMessage(ChatColor.GRAY + "已将你的 §f步行 §7速度设置为 §f" + speed);
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(error);
                        return false;
                    }
                }
                case "fly" -> {
                    try {
                        float speed = Float.parseFloat(strings[1]);
                        if (speed > 15 || speed < 0.1) {
                            sender.sendMessage(error);
                            return false;
                        }
                        player.setFlySpeed(speed / 10F);
                        sender.sendMessage(ChatColor.GRAY + "已将玩家 §f" + player.getName() + " §7的 §f飞行 §7速度设置为 §f" + speed);
                        player.sendMessage(ChatColor.GRAY + "已将你的 §f飞行 §7速度设置为 §f" + speed);
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(error);
                        return false;
                    }
                }
                default -> {
                    sender.sendMessage(error);
                    return false;
                }
            }
        }
        sender.sendMessage(error);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender.hasPermission("alsace.commands.speed")) {
            return types;
        }
        if (strings.length == 2 && sender.hasPermission("alsace.commands.speed")) {
            return speeds;
        }
        if (strings.length == 3 && sender.hasPermission("alsace.commands.speed.other")) {
            List<String> list = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
