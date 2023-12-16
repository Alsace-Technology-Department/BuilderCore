package com.puddingkc.commands.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemNbtCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/itemnbt §7- 获取手中物品的详细信息";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (sender instanceof Player player) {
            if (!sender.hasPermission("alsace.commands.itemnbt")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "你手中没有物品");
                return false;
            }

            player.sendMessage("§7物品信息:");
            player.sendMessage("§7物品名称: §f" + Objects.requireNonNull(itemInHand.getItemMeta()).getDisplayName());
            player.sendMessage("§7物品NBT: §f" + itemInHand.getItemMeta().toString());
            if (itemInHand.getItemMeta().hasCustomModelData()) {
                player.sendMessage("§7CustomModelData: §f" + itemInHand.getItemMeta().getCustomModelData());
            }
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
