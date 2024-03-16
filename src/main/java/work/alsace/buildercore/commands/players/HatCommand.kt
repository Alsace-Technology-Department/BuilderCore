package work.alsace.buildercore.commands.players;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class HatCommand implements CommandExecutor {
    public HatCommand() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家使用");
        } else {
            PlayerInventory inventory = player.getInventory();
            ItemStack item = inventory.getItemInMainHand();
            inventory.setItemInMainHand(inventory.getHelmet());
            inventory.setHelmet(item);
        }
        return true;
    }
}
