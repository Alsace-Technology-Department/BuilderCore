package com.puddingkc.commands.fawe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TwistCommand implements CommandExecutor {

    private final String error = "§7正确指令:\n§f//twist [轴线 X,Y,Z] [度数] §7- 快捷执行创世神deform指令";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.twist")) {
                player.sendMessage("§c你没有使用该命令的权限");
                return false;
            }

            int degrees;
            try {
                degrees = Integer.parseInt(args[1]);
            } catch (Exception e) {
                player.sendMessage(error);
                return false;
            }

            float radiansPerDegree = 0.0174533F;
            float radian = degrees * radiansPerDegree;
            if (args[0].equalsIgnoreCase("x")) {
                player.performCommand("/deform rotate(y,z," + (radian / 2.0F) + "*(x+1))");
            } else if (args[0].equalsIgnoreCase("y")) {
                player.performCommand("/deform rotate(x,z," + (radian / 2.0F) + "*(y+1))");
            } else if (args[0].equalsIgnoreCase("z")) {
                player.performCommand("/deform rotate(x,y," + (radian / 2.0F) + "*(z+1))");
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
