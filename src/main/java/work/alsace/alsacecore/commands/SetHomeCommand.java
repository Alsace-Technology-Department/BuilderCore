package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.User;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
        }
        if (sender instanceof Player) {
            if (args.length == 1) {
                String homeName = args[0];
                User user = null;
                Player player = (Player) sender;
                if (homeName.contains(":") && sender.hasPermission("alsace.sethome.other")) {
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
                } else {
                    user = AlsaceCore.instance.userProfiles.get(player.getUniqueId());
                }
                if (user.getHome(homeName) != null) {
                    sender.sendMessage("§c你已经有传送点" + homeName);
                    return false;
                }
                if (user.getHomes().size() >= user.getMaxHomes() && !player.hasPermission("alsace.sethome.other")) {
                    sender.sendMessage("§c你的家已经达到最大传送点数量");
                    return false;
                }
                for(String i : homeName.split(AlsaceCore.instance.illegalCharacters.toString())){
                    if(AlsaceCore.instance.illegalCharacters.contains(i.toLowerCase())){
                        sender.sendMessage("§c传送点名称包含非法字符");
                        return false;
                    }
                }
                user.addHome(homeName, player.getLocation());
                sender.sendMessage(String.format("§a成功设置传送点%s", homeName));
            } else if (sender.hasPermission("alsace.sethome.other")) {
                sender.sendMessage("§c此命令只有管理员可以执行");
            } else if (sender.hasPermission("alsace.sethome")) {
                sender.sendMessage("§7正确指令:\n§f/sethome <传送点> §7- 设置你的家\n§f/sethome <玩家>:<传送点> §7- 设置指定玩家的家");
            } else {
                sender.sendMessage("§c你没有使用该命令的权限");
            }
        }
        return false;
    }
}