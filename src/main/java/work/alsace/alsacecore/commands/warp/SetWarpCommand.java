package work.alsace.alsacecore.commands.warp;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.WarpDataLoader;

import java.util.regex.Pattern;

public class SetWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.setwarp")) {
            sender.sendMessage("§c你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String warpName = args[0];
            WarpDataLoader warpDataLoader;
            Player player = (Player) sender;
            warpDataLoader = AlsaceCore.instance.warpProfiles.get("warps");
            if (warpDataLoader.getWarp(warpName) != null) {
                sender.sendMessage("§c已经存在送点" + warpName);
                return false;
            }
            String separator = AlsaceCore.instance.getConfig().getString("illegal-characters");
            Pattern pattern = null;
            if (separator != null) {
                pattern = Pattern.compile(separator);
            }
            if (pattern != null) {
                for (String i : warpName.split(pattern.pattern())) {
                    if (pattern.matcher(i).matches()) {
                        sender.sendMessage("§c传送点名称包含非法字符");
                        return false;
                    }
                }
            }
            warpDataLoader.addWarp(warpName, player.getLocation());
            sender.sendMessage(String.format("§a成功设置传送点%s", warpName));
        } else {
            sender.sendMessage("§7正确指令:\n§f/setwarp <传送点> §7- 设置传送点");
        }
        return true;
    }
}