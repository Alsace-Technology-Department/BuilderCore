package work.alsace.alsacecore.commands.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameModeCommand implements CommandExecutor, TabCompleter {

    private static final List<String> modes = Arrays.asList("adventure", "creative", "spectator", "survival");
    private final String error = ChatColor.GRAY + "正确指令:\n§f/gamemode <模式> §7- 设置你的游戏模式\n§f/gamemode <模式> [玩家] §7- 设置指定玩家的游戏模式";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length != 1 && strings.length != 2) {
            sender.sendMessage(error);
            return false;
        }

        if (!(sender instanceof Player) && strings.length == 1) {
            sender.sendMessage(ChatColor.RED + "控制台无法执行该命令");
            return false;
        }

        Player targetPlayer;
        if (strings.length == 2) {
            if (!sender.hasPermission("alsace.commands.gamemode.other")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            targetPlayer = Bukkit.getPlayer(strings[1]);
            if (targetPlayer == null || !targetPlayer.isOnline()) {
                sender.sendMessage(ChatColor.RED + "指定的玩家不在线或不存在");
                return false;
            }
        } else {
            targetPlayer = (Player) sender;
            if (!targetPlayer.hasPermission("alsace.commands.gamemode")) {
                targetPlayer.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
        }

        GameMode gameMode = parseGameMode(strings[0]);
        if (gameMode == null) {
            sender.sendMessage(error);
            return false;
        }

        targetPlayer.setGameMode(gameMode);
        targetPlayer.sendMessage(ChatColor.GRAY + "已将你的游戏模式设置为 §f" + gameMode);

        if (strings.length == 2) {
            sender.sendMessage(ChatColor.GRAY + "已将玩家 §f" + targetPlayer.getName() + " §7的游戏模式设置为 §f" + gameMode);
        }

        return true;
    }

    private GameMode parseGameMode(String input) {
        return switch (input.toLowerCase()) {
            case "creative", "1" -> GameMode.CREATIVE;
            case "survival", "0" -> GameMode.SURVIVAL;
            case "spectator", "3" -> GameMode.SPECTATOR;
            case "adventure", "2" -> GameMode.ADVENTURE;
            default -> null;
        };
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
