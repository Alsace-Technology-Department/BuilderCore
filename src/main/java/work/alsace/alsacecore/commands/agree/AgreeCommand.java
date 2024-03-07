package work.alsace.alsacecore.commands.agree;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Utils.DataBaseManager;

import java.util.UUID;

public class AgreeCommand implements CommandExecutor {
    private final AlsaceCore plugin;
    private final DataBaseManager databaseManager;

    public AgreeCommand(AlsaceCore plugin) {
        this.plugin = plugin;
        this.databaseManager = plugin.getDatabaseManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (!databaseManager.hasPlayerAgreed(playerUUID)) {
                databaseManager.setPlayerAgreed(playerUUID, true);
                plugin.hasAgree.put(player.getName(), false);
                player.sendMessage(ChatColor.GREEN + "您已经同意用户协议，现在可以正常游玩！");
            } else {
                player.sendMessage(ChatColor.GREEN + "你已经同意过用户协议！");
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "只有玩家可以使用这个命令！");
            return false;
        }
        return true;
    }
}

