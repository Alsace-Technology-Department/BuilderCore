package work.alsace.buildercore.commands.warp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;
import work.alsace.buildercore.service.WarpDataLoader;

public class SetWarpCommand implements CommandExecutor {
    private final BuilderCore plugin;

    public SetWarpCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }
        if (!sender.hasPermission("buildercore.commands.setwarp")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        }
        if (args.length >= 1) {
            String warpName = args[0];
            String alias = (args.length == 2) ? args[1] : null;  // 获取别名，如果提供的话
            WarpDataLoader warpDataLoader;
            Player player = (Player) sender;
            warpDataLoader = plugin.getWarpProfiles().get("warps");
            if (warpDataLoader.getWarp(warpName) != null) {
                sender.sendMessage(ChatColor.RED + "已经存在传送点" + warpName);
                return false;
            }

            warpDataLoader.addWarp(warpName, alias, player.getLocation());
            sender.sendMessage(String.format(ChatColor.GRAY + "成功设置传送点 %s", warpName));
        } else {
            sender.sendMessage(ChatColor.GRAY + "正确指令:\n§f/setwarp <传送点> §7- 设置传送点");
        }
        return true;
    }
}
