package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.HomeDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.home")) {
            sender.sendMessage("§c你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String homeName = args[0];
            Player player = (Player) sender;
            if (homeName.contains(":") && sender.hasPermission("alsace.commands.home.other")) {
                String user = homeName.split(":", 2)[0];
                OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());;
                HomeDataLoader homeDataLoaderProfile;
                if (i.isOnline()) {
                    homeDataLoaderProfile = AlsaceCore.instance.homeProfiles.get(i.getUniqueId());
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage("§c玩家" + user + "不存在");
                    return false;
                } else {
                    homeDataLoaderProfile = new HomeDataLoader(player.getUniqueId());
                }
                homeName = homeName.replaceFirst(user + ":", "");
                if (homeDataLoaderProfile.getHome(homeName) != null) {
                    player.teleport(homeDataLoaderProfile.getHome(homeName));
                    sender.sendMessage("§a已传送至玩家" + user + "的家" + homeName);
                } else {
                    sender.sendMessage("§c玩家" + user + "没有传送点" + homeName);
                }
            } else {
                HomeDataLoader homeDataLoader = AlsaceCore.instance.homeProfiles.get(player.getUniqueId());
                if (homeDataLoader.getHome(homeName) == null) {
                    sender.sendMessage("§c你没有传送点" + homeName);
                } else {
                    player.teleport(homeDataLoader.getHome(homeName));
                    sender.sendMessage("§a已传送至家" + homeName);
                }
            }
        } else {
            sender.sendMessage("§7正确指令:\n§f/home <传送点> §7- 传送至你的家\n§f/home <玩家>:<传送点> §7- 传送至指定玩家的家");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1)
            return new ArrayList<>(0);
        return AlsaceCore.instance.homeProfiles.get(((Player) commandSender).getUniqueId()).getHomes().stream()
                .filter(home -> home.startsWith(args[0]))
                .collect(Collectors.toList());
    }
}
