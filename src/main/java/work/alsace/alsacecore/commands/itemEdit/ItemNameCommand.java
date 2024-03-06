package work.alsace.alsacecore.commands.itemEdit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemNameCommand implements CommandExecutor {
    private final String error = ChatColor.GRAY + "正确指令:\n§f/itemname <物品名称> §7- 修改手中物品名称";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!sender.hasPermission("alsace.commands.itemname")) {
                sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().isAir()) {
                player.sendMessage(ChatColor.RED + "你手中没有物品");
                return false;
            }

            String itemName = ChatColor.translateAlternateColorCodes('&', strings[0]);

            ItemMeta itemMeta = itemInHand.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemInHand.setItemMeta(itemMeta);
            player.sendMessage(ChatColor.GREEN + "§7已将手中物品名称修改为§r " + itemName);
            return true;
        }
        sender.sendMessage(error);
        return false;
    }
}
