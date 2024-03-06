package work.alsace.alsacecore.commands.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 0 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.fly")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.GRAY + "已将你的飞行模式设置为 §c关闭");
            } else {
                player.setAllowFlight(true);
                player.sendMessage(ChatColor.GRAY + "已将你的飞行模式设置为 §a开启");
            }
            return true;
        }

        if (strings.length == 1) {
            if (!sender.hasPermission("alsace.commands.fly.other")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            Player player = Bukkit.getPlayer(strings[0]);
            if (player != null && player.isOnline()) {
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    sender.sendMessage(ChatColor.GRAY + "已将玩家 §f" + player.getName() + " §7的飞行模式设置为 §c关闭");
                    player.sendMessage(ChatColor.GRAY + "已将你的飞行模式设置为 §c关闭");
                } else {
                    player.setAllowFlight(true);
                    sender.sendMessage(ChatColor.GRAY + "已将玩家 §f" + player.getName() + " §7的飞行模式设置为 §a开启");
                    player.sendMessage(ChatColor.GRAY + "已将你的飞行模式设置为 §a开启");
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "指定的玩家不在线或不存在");
                return false;
            }
        }

        sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/fly §7- 设置你的飞行状态\n§f/fly <玩家> §7- 设置指定玩家的飞行状态");
        return false;
    }
}
