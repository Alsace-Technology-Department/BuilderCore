package work.alsace.alsacecore.commands.warp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class WarpCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("alsace.commands.warp")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String warpName = args[0];
            WarpDataLoader warpDataLoader = AlsaceCore.instance.warpProfiles.get("warps");
            Location location = warpDataLoader.getWarp(warpName);
            World warpWorld = location.getWorld();
            if (warpWorld != null) {
                if (sender.hasPermission("multiverse.access." + warpWorld.getName())) {
                    Player player = (Player) sender;
                    if (location.getWorld() == null) {
                        sender.sendMessage(ChatColor.RED + "世界" + location.getWorld() + "不存在");
                        return false;
                    }
                    player.teleport(location);
                    sender.sendMessage(ChatColor.GRAY + "已传送至" + warpName);
                } else {
                    sender.sendMessage(ChatColor.RED + "你没有权限传送至" + warpName);
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "传送点" + location.getWorld() + "不存在");
                return false;
            }


        } else {
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/warp <传送点> §7- 传送至指定传送点");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length != 1)
            return new ArrayList<>(0);
        return new ArrayList<>(getAccessWarps((Player) sender));

    }

    private List<String> getAccessWarps(Player sender) {
        List<String> warps = new ArrayList<>();
        AlsaceCore.instance.warpProfiles.get("warps").getWarps().forEach(warp -> {
            Location location = AlsaceCore.instance.warpProfiles.get("warps").getWarp(warp);
            World world = location.getWorld();
            if (world != null) {
                if (sender.hasPermission("multiverse.access." + world.getName())) {
                    warps.add(warp);
                }
            }
        });
        return warps;
    }

}
