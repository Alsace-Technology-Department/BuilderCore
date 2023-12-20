package work.alsace.alsacecore.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPHereCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/tphere <玩家> §7- 将指定玩家传送到你的位置";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length == 1) {
            if (!sender.hasPermission("alsace.commands.tphere")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            Player player1 = Bukkit.getPlayer(args[0]);
            if (player1 == null || !player1.isOnline()) {
                sender.sendMessage(ChatColor.RED + "指定的玩家不在线或不存在");
                return false;
            }

            player1.teleport(player.getLocation());
            player1.sendMessage("§f" + sender.getName() + " §7将你传送到了他的位置");
            sender.sendMessage("§7已将 §f" + player1.getName() + " §7传送到你的位置");
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
