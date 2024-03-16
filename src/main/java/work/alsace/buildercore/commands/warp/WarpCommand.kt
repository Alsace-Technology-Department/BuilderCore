package work.alsace.buildercore.commands.warp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;
import work.alsace.buildercore.service.WarpDataLoader;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private final BuilderCore plugin;

    public WarpCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.warp")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length == 1) {
            String warpName = args[0];
            WarpDataLoader warpDataLoader = plugin.getWarpProfiles().get("warps");
            if (warpName == null) {
                sender.sendMessage(ChatColor.RED + "传送点" + warpName + "不存在");
                return false;
            }
            Location location = warpDataLoader.getWarp(warpName);
            if (location == null) {
                sender.sendMessage(ChatColor.RED + "传送点" + warpName + "不存在");
                return false;
            }
            World warpWorld = location.getWorld();
            if (warpWorld != null) {
                if (sender.hasPermission("multiverse.access." + warpWorld.getName())) {
                    Player player = (Player) sender;
                    player.teleport(location);
                    sender.sendMessage(ChatColor.GRAY + "已传送至" + warpName);
                } else {
                    sender.sendMessage(ChatColor.RED + "你没有权限传送至" + warpName);
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "世界" + warpWorld + "不存在");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/warp <传送点> §7- 传送至指定传送点");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (args.length != 1)
            return new ArrayList<>(0);
        return new ArrayList<>(getAccessWarps((Player) sender));

    }

    private List<String> getAccessWarps(Player sender) {
        List<String> warps = new ArrayList<>();
        plugin.getWarpProfiles().get("warps").getWarps().forEach(warp -> {
            Location location = plugin.getWarpProfiles().get("warps").getWarp(warp);
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
