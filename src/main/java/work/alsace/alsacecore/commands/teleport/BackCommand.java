package work.alsace.alsacecore.commands.teleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import work.alsace.alsacecore.AlsaceCore;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

public class BackCommand implements CommandExecutor {
    private final AlsaceCore plugin;
    private final HashMap<UUID, Stack<Location>> locationHistory = new HashMap<>();

    public BackCommand(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "该命令只能由玩家使用");
            return false;
        }

        UUID playerId = player.getUniqueId();

        // 确保该玩家有自己的位置历史栈
        locationHistory.putIfAbsent(playerId, new Stack<>());

        Stack<Location> playerLocationHistory = locationHistory.get(playerId); // 获取玩家的位置历史栈

        if (playerLocationHistory.isEmpty()) {
            player.sendMessage(ChatColor.RED + "没有位置记录");
            return false;
        }

        int backSteps = 1; // 默认返回上一个位置
        if (args.length > 0) {
            try {
                backSteps = Integer.parseInt(args[0]);
                if (backSteps < 1 || backSteps > plugin.backHistory) {
                    player.sendMessage(ChatColor.RED + "无效的参数，只能是1-5");
                    return false;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "无效的参数，只能是数字");
                return false;
            }
        }

        if (playerLocationHistory.size() < backSteps) {
            player.sendMessage(ChatColor.RED + "没有足够的位置记录");
            return false;
        }

        Location targetLocation = null;
        for (int i = 0; i < backSteps; i++) {
            targetLocation = playerLocationHistory.pop();
        }

        if (targetLocation != null) {
            player.teleport(targetLocation);
            player.sendMessage(ChatColor.GRAY + "返回到之前的位置");
        }

        return true;
    }

    public void addToHistory(Player player) {
        Location currentLocation = player.getLocation().clone();
        UUID playerId = player.getUniqueId();

        locationHistory.putIfAbsent(playerId, new Stack<>()); // 确保玩家有自己的栈
        Stack<Location> playerLocationHistory = locationHistory.get(playerId);

        playerLocationHistory.push(currentLocation);

        int maxHistorySize = plugin.backHistory;
        while (playerLocationHistory.size() > maxHistorySize) {
            playerLocationHistory.remove(0);
        }
    }
}
