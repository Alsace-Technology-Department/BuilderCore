package work.alsace.buildercore.commands.home;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;
import work.alsace.buildercore.service.HomeDataLoader;

public class SetHomeCommand implements CommandExecutor {
    private final BuilderCore plugin;

    public SetHomeCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.sethome")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String homeName = args[0];
            HomeDataLoader homeDataLoader;
            Player player = (Player) sender;
            if (homeName.contains(":") && sender.hasPermission("buildercore.commands.sethome.other")) {
                String username = args[0].split(":", 2)[0];
                homeName = args[0].replaceFirst(username + ":", "");
                OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());
                if (i.isOnline()) {
                    homeDataLoader = plugin.getHomeProfiles().get(i.getUniqueId());
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED + "玩家不存在");
                    return false;
                } else {
                    homeDataLoader = new HomeDataLoader(i.getUniqueId(), plugin);
                }
            } else {
                homeDataLoader = plugin.getHomeProfiles().get(player.getUniqueId());
            }
            if (homeDataLoader.getHome(homeName) != null) {
                sender.sendMessage(ChatColor.RED + "已经存在传送点" + homeName);
                return false;
            }
            if (homeDataLoader.getHomes().size() >= homeDataLoader.getMaxHomes() && !player.hasPermission("buildercore.commands.sethome.other")) {
                sender.sendMessage(ChatColor.RED + "你的家已经达到最大传送点数量");
                return false;
            }
            homeDataLoader.addHome(homeName, player.getLocation());
            sender.sendMessage(String.format(ChatColor.GRAY + "成功设置传送点%s", homeName));
        } else {
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/sethome <传送点> §7- 设置你的家\n§f/sethome <玩家>:<传送点> §7- 设置指定玩家的家");
        }
        return true;
    }
}
