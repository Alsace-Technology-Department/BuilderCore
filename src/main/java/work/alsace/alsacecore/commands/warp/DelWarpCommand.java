package work.alsace.alsacecore.commands.warp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.WarpDataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DelWarpCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.delwarp")) {
            sender.sendMessage("§c你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String warpName = args[0];
            WarpDataLoader warpDataLoader = AlsaceCore.instance.warpProfiles.get("warps");
            Location location = warpDataLoader.getWarp(warpName);
            World warpWorld = location.getWorld();
            if (warpWorld != null) {
                if (sender.hasPermission("multiverse.access." + warpWorld.getName())) {
                    warpDataLoader.delWarp(warpName);
                    sender.sendMessage(String.format("§a已删除传送点%s", warpName));
                } else {
                    sender.sendMessage("§c你没有权限删除" + warpName);
                    return false;
                }
            } else {
                sender.sendMessage("§c传送点" + location.getWorld() + "不存在");
                return false;
            }
        } else {
            sender.sendMessage("§7正确指令:\n§f/delwarp <传送点> §7- 删除传送点");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length != 1)
            return new ArrayList<>(0);
        //TODO 根据玩家是否有权限进入世界来过滤传送点
        return AlsaceCore.instance.warpProfiles.get("warps").getWarps().stream()
                .filter(warps -> sender.hasPermission("multiverse.access" + warps))
                .collect(Collectors.toList());
    }
}
