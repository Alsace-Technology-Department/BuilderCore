package com.puddingkc.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class PWeatherCommand implements CommandExecutor, TabCompleter {
    private final String error = ChatColor.GRAY + "正确指令:\n§f/pweather <天气> §7- 设置你自己客户端的天气\n§f/pweather <天气> <玩家> §7- 设置指定玩家的客户端天气";
    private static final List<String> weathers = Arrays.asList("clear", "rain", "thunder");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        //TODO 未完成
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        //TODO 未完成
        return null;
    }
}
