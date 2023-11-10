package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.User;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
        }
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("alsace.home")) {
                    String homeName = args[0];
                    Player player = (Player) sender;
                    if (homeName.contains(":") && sender.hasPermission("alsace.home.other")) {
                        String user = homeName.split(":", 2)[0];
                        OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(user);
                        User userProfile;
                        if (i.isOnline()) {
                            userProfile = AlsaceCore.instance.userProfiles.get(i.getUniqueId());
                        } else if (!i.hasPlayedBefore()) {
                            sender.sendMessage("§c玩家" + user + "不存在");
                            return false;
                        } else {
                            userProfile = new User(player.getUniqueId());
                        }
                        homeName = homeName.replaceFirst(user + ":", "");
                        if (userProfile.getHome(homeName) != null) {
                            player.teleport(userProfile.getHome(homeName));
                            sender.sendMessage("§a已传送至玩家" + user + "的家" + homeName);
                        } else {
                            sender.sendMessage("§c玩家" + user + "没有传送点" + homeName);
                        }
                    } else {
                        User user = AlsaceCore.instance.userProfiles.get(player.getUniqueId());
                        if (user.getHome(homeName) == null) {
                            sender.sendMessage("§c你没有传送点" + homeName);
                        } else {
                            player.teleport(user.getHome(homeName));
                            sender.sendMessage("§a已传送至家" + homeName);
                        }
                    }
                } else {
                    sender.sendMessage("§c你没有使用该命令的权限");
                }
            } else {
                if (sender.hasPermission("alsace.home.other")) {
                    sender.sendMessage("§c此命令只有管理员可以执行");
                } else {
                    sender.sendMessage("§7正确指令:\n§f/home <传送点> §7- 传送至你的家\n§f/home <玩家>:<传送点> §7- 传送至指定玩家的家");
                }
            }
        }
        return false;
    }
}
