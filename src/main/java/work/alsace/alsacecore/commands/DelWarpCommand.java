package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.WarpDataLoader;

public class DelWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
            return false;
        }
        WarpDataLoader warpDataLoader;
        String warpName;
        if (args.length == 1 && (sender.hasPermission("alsace.commands.delwarp"))) {
            warpName = args[0];
            warpDataLoader = AlsaceCore.instance.warpProfiles.get("warps");
            if (warpDataLoader.getWarp(warpName) != null) {
                warpDataLoader.delWarp(warpName);
                sender.sendMessage(String.format("§a已删除传送点%s", warpName));
            } else {
                sender.sendMessage("§c你没有传送点" + warpName);
            }
        }
        else if (sender.hasPermission("alsace.commands.delwarp")) {
            sender.sendMessage("§7正确指令:\n§f/delwarp <传送点> §7- 删除传送点");
        } else {
            sender.sendMessage("§c你没有使用该命令的权限");
        }
        return false;
    }
}
