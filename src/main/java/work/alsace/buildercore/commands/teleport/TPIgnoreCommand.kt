package work.alsace.buildercore.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;

public class TPIgnoreCommand implements CommandExecutor {
    private final BuilderCore plugin;

    public TPIgnoreCommand(BuilderCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家使用");
            return true;
        } else if (!sender.hasPermission("buildercore.commands.tpignore")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return true;
        } else {
            Boolean now = this.plugin.getHasIgnored().get(sender.getName());
            if (now != null) {
                if (now) {
                    this.plugin.getHasIgnored().put(sender.getName(), false);
                    sender.sendMessage(ChatColor.GRAY + "已取消屏蔽强制传送");
                } else {
                    this.plugin.getHasIgnored().put(sender.getName(), true);
                    sender.sendMessage(ChatColor.GRAY + "已成功屏蔽强制传送");
                }
            } else {
                this.plugin.getHasIgnored().put(sender.getName(), true);
                sender.sendMessage(ChatColor.GRAY + "已成功屏蔽强制传送");
            }
            return true;
        }
    }
}
