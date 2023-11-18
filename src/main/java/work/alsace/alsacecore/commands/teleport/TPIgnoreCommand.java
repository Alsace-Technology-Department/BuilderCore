package work.alsace.alsacecore.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;

public class TPIgnoreCommand implements CommandExecutor {
    private final AlsaceCore plugin;

    public TPIgnoreCommand(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家使用");
            return true;
        } else if (!sender.hasPermission("alsace.commands.tpignore")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return true;
        } else {
            boolean now = this.plugin.hasIgnored.get(sender.getName());
            if (now) {
                this.plugin.hasIgnored.put(sender.getName(), false);
                sender.sendMessage(ChatColor.GRAY + "已取消屏蔽强制传送");
            } else {
                this.plugin.hasIgnored.put(sender.getName(), true);
                sender.sendMessage(ChatColor.GRAY + "已成功屏蔽强制传送");
            }

            return true;
        }
    }
}
