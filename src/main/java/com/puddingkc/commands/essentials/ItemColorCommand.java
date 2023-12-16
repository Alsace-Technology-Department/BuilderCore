package com.puddingkc.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public class ItemColorCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/itemcolor <16位颜色代码> §7- 修改手中物品(皮革)的颜色";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {

        if (strings.length == 1 && sender instanceof Player player) {
            if (!sender.hasPermission("alsace.commands.itemcolor")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "你手中没有物品");
                return false;
            }

            if (!isValidColorCode(strings[0].replace("#",""))) {
                player.sendMessage(ChatColor.RED + "无效的颜色代码");
                return false;
            }

            if (itemInHand.getItemMeta() instanceof LeatherArmorMeta leatherMeta) {
                try {
                    int colorCode = Integer.parseInt(strings[0].replace("#",""), 16);
                    leatherMeta.setColor(org.bukkit.Color.fromRGB(colorCode));
                    itemInHand.setItemMeta(leatherMeta);
                    player.sendMessage(ChatColor.GRAY + "物品颜色已成功修改");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "无效的颜色代码");
                    return false;
                }
            } else {
                player.sendMessage(ChatColor.RED + "该物品不能修改颜色");
            }
            return true;
        }
        sender.sendMessage(error);
        return false;
    }

    private boolean isValidColorCode(String colorCode) {
        return colorCode.matches("[0-9A-Fa-f]{6}");
    }
}
