package work.alsace.buildercore.commands.alias;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UndoCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("buildercore.aliases")) {
            sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
            return false;
        } else {
            ((Player) sender).performCommand("/undo");
            return true;
        }
    }
}
