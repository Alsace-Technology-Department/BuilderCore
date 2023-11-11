package work.alsace.alsacecore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.WarpDataLoader;

import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private static List<String> warps;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
            return false;
        }
        if (args.length == 1) {
            String warpName = args[0];
            if (sender.hasPermission("alsace.commands.warp." + warpName)) {
                Player player = (Player) sender;
                WarpDataLoader warpDataLoader = AlsaceCore.instance.warpProfiles.get("warps");
                if (warpDataLoader.getWarp(warpName) == null) {
                    sender.sendMessage("§c传送点" + warpName + "不存在");
                } else {
                    player.teleport(warpDataLoader.getWarp(warpName));
                    sender.sendMessage("§a已传送至" + warpName);
                }

            } else {
                sender.sendMessage("§c你没有权限传送至" + warpName);
            }
        } else {
            sender.sendMessage("§7正确指令:\n§f/warp <传送点> §7- 传送至指定传送点");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        //TODO tab
        return null;
    }
}
