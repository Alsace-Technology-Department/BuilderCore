package work.alsace.alsacecore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import work.alsace.alsacecore.AlsaceCore;

public class AFKCommand implements CommandExecutor {
    private final AlsaceCore plugin;

    public AFKCommand(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "该指令仅限玩家执行");
            return false;
        }

        if (args.length == 0) {
            toggleAFK(player);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "正确指令:\n§f/afk §7- 进入暂离状态");
        }

        return false;
    }

    private void toggleAFK(Player player) {
        if (player.hasMetadata("afk")) {
            player.sendMessage(ChatColor.GRAY + "你已取消暂离状态");
            player.removeMetadata("afk", plugin);
            Bukkit.broadcastMessage("§7玩家 §f" + player.getName() + " §7回来了");
        } else {
            player.sendMessage(ChatColor.GRAY + "你已进入暂离状态");
            player.setMetadata("afk", new FixedMetadataValue(plugin, true));
            Bukkit.broadcastMessage("§7玩家 §f" + player.getName() + " §7暂时离开了");
        }
    }
}
