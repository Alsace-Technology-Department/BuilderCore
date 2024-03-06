package work.alsace.alsacecore.commands.builderTools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadCommand implements CommandExecutor {

    private final String error = ChatColor.GRAY + "正确指令:\n§f/head <玩家名称> §7- 获取指定玩家的皮肤头颅";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length == 1 && sender instanceof Player player) {
            if (!player.hasPermission("alsace.commands.head")) {
                player.sendMessage(ChatColor.RED + "你没有使用该命令的权限");
                return false;
            }
            ItemStack head = createPlayerHead(strings[0]);
            player.getInventory().addItem(head);
            player.sendMessage(ChatColor.GRAY + "成功获得了玩家 §f" + strings[0] + " §7的头颅");
            return true;
        }
        sender.sendMessage(error);
        return false;
    }

    private ItemStack createPlayerHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        head.setItemMeta(meta);
        return head;
    }
}
