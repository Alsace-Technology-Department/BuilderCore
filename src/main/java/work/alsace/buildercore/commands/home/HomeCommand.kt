package work.alsace.buildercore.commands.home;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class HomeCommand implements CommandExecutor, TabCompleter {
    private final BuilderCore plugin;

    public HomeCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.home")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String homeName = args[0];
            Player player = (Player) sender;
            if (homeName.contains(":") && sender.hasPermission("buildercore.commands.home.other")) {
                String user = homeName.split(":", 2)[0];
                OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());
                HomeDataLoader homeDataLoaderProfile;
                if (i.isOnline()) {
                    homeDataLoaderProfile = plugin.getHomeProfiles().get(i.getUniqueId());
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED + "玩家" + user + "不存在");
                    return false;
                } else {
                    homeDataLoaderProfile = new HomeDataLoader(player.getUniqueId(), plugin);
                }
                homeName = homeName.replaceFirst(user + ":", "");
                Location location = homeDataLoaderProfile.getHome(homeName);
                if (homeDataLoaderProfile.getHome(homeName) != null) {
                    if (location.getWorld() == null) {
                        sender.sendMessage(ChatColor.RED + "世界" + location.getWorld() + "不存在");
                        return false;
                    }
                    player.teleport(location);
                    sender.sendMessage(ChatColor.GRAY + "已传送至玩家" + user + "的家" + homeName);
                } else {
                    sender.sendMessage(ChatColor.RED + "玩家" + user + "没有传送点" + homeName);
                    return false;
                }
            } else {
                HomeDataLoader homeDataLoader = plugin.getHomeProfiles().get(player.getUniqueId());
                Location loc = homeDataLoader.getHome(homeName);
                if (loc == null) {
                    sender.sendMessage(ChatColor.RED + "你没有传送点" + homeName);
                    return false;
                } else {
                    if (loc.getWorld() == null) {
                        sender.sendMessage(ChatColor.RED + "世界" + loc.getWorld() + "不存在");
                        return false;
                    }
                    player.teleport(loc);
                    sender.sendMessage(ChatColor.GRAY + "已传送至家" + homeName);
                }
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/home <传送点> §7- 传送至你的家\n§f/home <玩家>:<传送点> §7- 传送至指定玩家的家");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
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
            return list;
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            if (sender instanceof Player) {
                list.addAll(plugin.getHomeProfiles().get(((Player) sender).getUniqueId()).getHomes());
            }
            return list;
        }
    }
}
