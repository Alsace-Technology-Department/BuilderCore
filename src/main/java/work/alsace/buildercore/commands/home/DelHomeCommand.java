package work.alsace.buildercore.commands.home;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;
import work.alsace.buildercore.service.HomeDataLoader;

import java.util.ArrayList;
import java.util.List;

public class DelHomeCommand implements CommandExecutor, TabCompleter {
    private final BuilderCore plugin;

    public DelHomeCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.delhome")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            HomeDataLoader homeDataLoader;
            String homeName;
            Player player = (Player) sender;
            if (args[0].contains(":") && (sender.hasPermission("buildercore.commands.delhome.other"))) {
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
                homeName = args[0];
                homeDataLoader = plugin.getHomeProfiles().get(((Player) sender).getUniqueId());
            }
            if (homeDataLoader.getHome(homeName) != null) {
                homeDataLoader.delHome(homeName);
                sender.sendMessage(String.format(ChatColor.GRAY + "已删除传送点%s", homeName));
            } else {
                sender.sendMessage(ChatColor.RED + "不存在传送点" + homeName);
            }
        } else{
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/delhome <传送点> §7- 删除你的传送点\n§f/delhome <玩家>:<传送点> §7- 删除指定玩家的传送点");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (args.length != 1)
            return new ArrayList<>(0);
        List<String> list = new ArrayList<>();
        if (sender instanceof Player && args[0].contains(":")) {
            String user = args[0].split(":", 2)[0];
            OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(((Player) sender).getUniqueId());
            HomeDataLoader homeDataLoaderProfile;
            if (i.isOnline()) {
                homeDataLoaderProfile = plugin.getHomeProfiles().get(i.getUniqueId());
            } else if (!i.hasPlayedBefore()) {
                return new ArrayList<>(0);
            } else {
                homeDataLoaderProfile = new HomeDataLoader(i.getUniqueId(), plugin);
            }
            list.addAll(homeDataLoaderProfile.getHomes().stream().map(s1 -> user + ":" + s1).toList());
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            if (sender instanceof Player) {
                list.addAll(plugin.getHomeProfiles().get(((Player) sender).getUniqueId()).getHomes());
            }
        }
        return list;
    }
}
