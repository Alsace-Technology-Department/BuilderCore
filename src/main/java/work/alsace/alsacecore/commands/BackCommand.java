package work.alsace.alsacecore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;

import java.util.Stack;

public class BackCommand implements CommandExecutor {
    private final AlsaceCore plugin;
    private final Stack<Location> locationHistory = new Stack<>();

    public BackCommand(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "该命令只能由玩家使用");
            return false;
        }

        Player player = (Player) sender;

        if (locationHistory.isEmpty()) {
            player.sendMessage(ChatColor.RED + "没有上一个位置记录");
            return false;
        }

        Location lastLocation = locationHistory.pop();
        player.teleport(lastLocation);
        player.sendMessage(ChatColor.GRAY + "返回上一个位置");

        return true;
    }

    public void addToHistory(Player player) {
        Location currentLocation = player.getLocation().clone();
        locationHistory.push(currentLocation);

        int maxHistorySize = 5;
        while (locationHistory.size() > maxHistorySize) {
            locationHistory.remove(0);
        }
    }
}
