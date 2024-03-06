package work.alsace.alsacecore.commands.alias;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DerotCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f//derot [轴线 X,Y,Z] [度数] §7- 快捷执行创世神deform指令";
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (strings.length == 2 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.aliases")) {
                player.sendMessage(error);
                return false;
            }

            int degrees;
            try {
                degrees = Integer.parseInt(strings[1]);
            } catch (Exception var8) {
                player.sendMessage(error);
                return false;
            }

            float radiansPerDegree = 0.0174533F;
            float radian = (float)degrees * radiansPerDegree;
            if (strings[0].equalsIgnoreCase("x")) {
                player.performCommand("/deform rotate(y,z," + radian + ")");
            } else if (strings[0].equalsIgnoreCase("y")) {
                player.performCommand("/deform rotate(x,z," + radian + ")");
            } else if (strings[0].equalsIgnoreCase("z")) {
                player.performCommand("/deform rotate(x,y," + radian + ")");
            } else {
                player.sendMessage(error);
                return false;
            }
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
