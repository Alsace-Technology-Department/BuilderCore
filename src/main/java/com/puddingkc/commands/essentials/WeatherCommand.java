package com.puddingkc.commands.essentials;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeatherCommand implements CommandExecutor, TabCompleter {

    private final String error = "§7正确指令:\n§f/weather <天气> §7- 设置你当前世界的天气";
    private static final List<String> weathers = Arrays.asList("clear", "rain", "thunder");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.weather")) {
                player.sendMessage("§c你没有使用该命令的权限");
                return false;
            }
            World world = player.getWorld();
            switch (strings[0]) {
                case "clear" -> {
                    world.setStorm(false);
                    world.setThundering(false);
                    player.sendMessage("已将当前世界天气设置为 §f晴天");
                    return true;
                }
                case "rain" -> {
                    world.setStorm(true);
                    world.setThundering(false);
                    player.sendMessage("已将当前世界天气设置为 §f雨天");
                    return true;
                }
                case "thunder" -> {
                    world.setStorm(false);
                    world.setThundering(true);
                    player.sendMessage("已将当前世界天气设置为 §f雷暴");
                    return true;
                }
                default -> {
                    player.sendMessage(error);
                    return false;
                }
            }
        }
        sender.sendMessage(error);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 1 && sender.hasPermission("alsace.commands.weather")) {
            return weathers;
        }
        return Collections.emptyList();
    }
}
