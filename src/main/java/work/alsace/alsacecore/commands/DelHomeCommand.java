package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.User;

public class DelHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        User user = null;
        String homeName;
        boolean isPlayer = sender instanceof Player;
        if (args.length == 1 && (sender.hasPermission("basichomes.delhome") || !isPlayer)) {
            if (args[0].contains(":") && (sender.hasPermission("basichomes.delhome.other") || !isPlayer)) {
                String username = args[0].split(":", 2)[0];
                homeName = args[0].replaceFirst(username + ":", "");
                OfflinePlayer i = Bukkit.getServer().getOfflinePlayer(username);
                if (i.isOnline()) {
                    user = AlsaceCore.instance.userProfiles.get(i.getUniqueId());
                } else if (!i.hasPlayedBefore()) {
                    sender.sendMessage("§c玩家" + user + "不存在");
                    return false;
                } else {
                    user = new User(i.getUniqueId());
                }
            } else if (!isPlayer) {
                sender.sendMessage("§c此命令只有管理员可以执行");
                return false;
            } else {
                homeName = args[0];
                user = AlsaceCore.instance.userProfiles.get(((Player) sender).getUniqueId());
            }
            if (user.getHome(homeName) != null) {
                user.delHome(homeName);
                sender.sendMessage(String.format("§a已删除传送点%s", homeName));
            } else {
                sender.sendMessage("§c你没有传送点" + homeName);
            }
        } else if (sender.hasPermission("alsace.delhome.other") || !isPlayer) {
            sender.sendMessage("§c此命令只有管理员可以执行");
        } else if (sender.hasPermission("alsace.delhome")) {
            sender.sendMessage("§7正确指令:\n§f/delhome <传送点> §7- 删除你的传送点\n§f/delhome <玩家>:<传送点> §7- 删除指定玩家的传送点");
        } else {
            sender.sendMessage("§c你没有使用该命令的权限");
        }
        return false;
    }
}
