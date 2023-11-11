package work.alsace.alsacecore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HatCommand implements CommandExecutor {
    public HatCommand() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c该指令仅限玩家使用");
        } else {
            PlayerInventory inventory = player.getInventory();
            ItemStack item = inventory.getItemInMainHand();
            inventory.setItemInMainHand(inventory.getHelmet());
            inventory.setHelmet(item);
        }
        return true;
    }
}
