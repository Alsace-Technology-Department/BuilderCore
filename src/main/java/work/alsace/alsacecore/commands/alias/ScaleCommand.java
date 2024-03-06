package work.alsace.alsacecore.commands.alias;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScaleCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f//scale [数值] §7- 快捷执行创世神deform指令";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.aliases")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            double size;
            try {
                size = Double.parseDouble(args[0]);
            } catch (Exception e) {
                sender.sendMessage(error);
                return false;
            }

            player.performCommand("/deform x/=" + size + ";y/=" + size + ";z/=" + size);
            return true;
        }

        sender.sendMessage(error);
        return false;
    }
}
