package com.puddingkc.commands.fawe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DerotCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("alsace.commands.derot")) {
            return false;
        } else if (strings.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + "正确指令: //derot [轴线 X,Y,Z] [度数]");
            return false;
        } else {
            int degrees;
            try {
                degrees = Integer.parseInt(strings[1]);
            } catch (Exception var8) {
                sender.sendMessage(ChatColor.YELLOW + "正确指令: //derot [轴线 X,Y,Z] [度数]");
                return false;
            }

            float radiansPerDegree = 0.0174533F;
            float radian = (float)degrees * radiansPerDegree;
            if (strings[0].equalsIgnoreCase("x")) {
                ((Player)sender).performCommand("/deform rotate(y,z," + radian + ")");
            } else if (strings[0].equalsIgnoreCase("y")) {
                ((Player)sender).performCommand("/deform rotate(x,z," + radian + ")");
            } else if (strings[0].equalsIgnoreCase("z")) {
                ((Player)sender).performCommand("/deform rotate(x,y," + radian + ")");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "正确指令: //derot [轴线 X,Y,Z] [度数]");
            }

            return true;
        }
    }
}
