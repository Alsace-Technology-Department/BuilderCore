package com.puddingkc.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class gamemode implements CommandExecutor, TabCompleter {

    private static final List<String> modes = Arrays.asList("adventure", "creative", "spectator", "survival");
    private final String error = "§7正确指令:\n§f/gamemode <模式> §7- 设置你的游戏模式\n§f/gamemode <模式> [玩家] §7- 设置指定玩家的游戏模式";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.gamemode")) {
                player.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            if (Objects.equals(strings[0], "creative") || Objects.equals(strings[0], "1")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage("§7已将你的游戏模式设置为 §f创造");
                return true;
            } else if (Objects.equals(strings[0], "survival") || Objects.equals(strings[0], "0")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage("§7已将你的游戏模式设置为 §f生存");
                return true;
            } else if (Objects.equals(strings[0], "spectator") || Objects.equals(strings[0], "3")) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage("§7已将你的游戏模式设置为 §f观察者");
                return true;
            } else if (Objects.equals(strings[0], "adventure") || Objects.equals(strings[0], "2")) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage("§7已将你的游戏模式设置为 §f冒险");
                return true;
            } else {
                player.sendMessage(error);
                return false;
            }
        }

        if (strings.length == 2) {
            if (!sender.hasPermission("alsace.commands.gamemode.other")) {
                sender.sendMessage("§c你没有使用该命令的权限");
                return false;
            }

            Player player = Bukkit.getPlayer(strings[1]);
            if (player == null || !player.isOnline()) {
                sender.sendMessage("§c指定的玩家不在线或不存在");
                return false;
            }

            if (Objects.equals(strings[0], "creative") || Objects.equals(strings[0], "1")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage("§7已将你的游戏模式设置为 §f创造");
                sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的游戏模式设置为 §f创造");
                return true;
            } else if (Objects.equals(strings[0], "survival") || Objects.equals(strings[0], "0")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage("§7已将你的游戏模式设置为 §f生存");
                sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的游戏模式设置为 §f生存");
                return true;
            } else if (Objects.equals(strings[0], "spectator") || Objects.equals(strings[0], "3")) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage("§7已将你的游戏模式设置为 §f观察者");
                sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的游戏模式设置为 §f观察者");
                return true;
            } else if (Objects.equals(strings[0], "adventure") || Objects.equals(strings[0], "2")) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage("§7已将你的游戏模式设置为 §f冒险");
                sender.sendMessage("§7已将玩家 §f" + player.getName() + " §7的游戏模式设置为 §f冒险");
                return true;
            } else {
                sender.sendMessage(error);
                return false;
            }
        }
        sender.sendMessage(error);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender.hasPermission("alsace.commands.gamemode")) {
            return modes;
        }
        if (strings.length == 2 && sender.hasPermission("alsace.commands.gamemode.other")) {
            List<String> list = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
