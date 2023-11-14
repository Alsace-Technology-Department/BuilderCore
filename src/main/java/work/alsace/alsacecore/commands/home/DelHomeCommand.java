package work.alsace.alsacecore.commands.home;

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
import java.util.Collections;
import java.util.List;

public class DelHomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.delhome")) {
            sender.sendMessage("§c你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            HomeDataLoader homeDataLoader;
            String homeName;
            Player player = (Player) sender;
            if (args[0].contains(":") && (sender.hasPermission("alsace.commands.delhome.other"))) {
                String username = args[0].split(":", 2)[0];
                homeName = args[0].replaceFirst(username + ":", "");
                OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(player.getUniqueId());
                if (i.isOnline()) {
                    homeDataLoader = AlsaceCore.instance.homeProfiles.get(i.getUniqueId());
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage("§c玩家不存在");
                    return false;
                } else {
                    homeDataLoader = new HomeDataLoader(i.getUniqueId());
                }
            } else {
                homeName = args[0];
                homeDataLoader = AlsaceCore.instance.homeProfiles.get(((Player) sender).getUniqueId());
            }
            if (homeDataLoader.getHome(homeName) != null) {
                homeDataLoader.delHome(homeName);
                sender.sendMessage(String.format("§a已删除传送点%s", homeName));
            } else {
                sender.sendMessage("§c不存在传送点" + homeName);
            }
        } else{
            sender.sendMessage("§7正确指令:\n§f/delhome <传送点> §7- 删除你的传送点\n§f/delhome <玩家>:<传送点> §7- 删除指定玩家的传送点");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length != 1)
            return new ArrayList<>(0);
        List<String> list = new ArrayList<>();
        if (sender instanceof Player && args[0].contains(":")) {
            String user = args[0].split(":", 2)[0];
            OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(((Player) sender).getUniqueId());
            HomeDataLoader homeDataLoaderProfile;
            if (i.isOnline()) {
                homeDataLoaderProfile = AlsaceCore.instance.homeProfiles.get(i.getUniqueId());
            } else if (!i.hasPlayedBefore()) {
                return new ArrayList<>(0);
            } else {
                homeDataLoaderProfile = new HomeDataLoader(i.getUniqueId());
            }
            list.addAll(homeDataLoaderProfile.getHomes().stream().map(s1 -> user + ":" + s1).toList());
            return list;
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
            if (sender instanceof Player) {
                list.addAll(AlsaceCore.instance.homeProfiles.get(((Player) sender).getUniqueId()).getHomes());
            }
            return list;
        }
    }
}
