package work.alsace.buildercore.commands.alias;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConvexCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f//convex §7- 快捷执行创世神sel指令";

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("buildercore.aliases")) {
                player.sendMessage(error);
                return false;
            }
            player.performCommand("/sel convex");
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
