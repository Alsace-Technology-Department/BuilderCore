package work.alsace.alsacecore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import work.alsace.alsacecore.AlsaceCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKListener implements Listener {
    private final AlsaceCore plugin;
    private final Map<UUID, Long> lastActivity = new HashMap<>();

    public AFKListener(AlsaceCore plugin) {
        this.plugin = plugin;
        new BukkitRunnable() {
            @Override
            public void run() {
                checkAFKPlayers();
            }
        }.runTaskTimer(plugin, 20 * 60, 20 * 60);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());

        cancelAFK(player);
    }

    private void checkAFKPlayers() {
        long afkThreshold = plugin.getConfig().getLong("afk.threshold", 300);
        long currentTime = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();
            long lastActiveTime = lastActivity.getOrDefault(playerId, currentTime);

            if (currentTime - lastActiveTime > afkThreshold * 1000) {
                handleAFK(player);
            } else {
                if (player.hasMetadata("afk")) {
                    player.sendMessage(ChatColor.GRAY + "你已取消暂离状态");
                    //TODO 移除玩家afk状态
                }
            }
        }
    }

    private void handleAFK(Player player) {
        if (!player.hasMetadata("afk")) {
            player.sendMessage(ChatColor.GRAY + "你已进入暂离状态");
            //TODO 为玩家添加afk状态
        }
    }

    private void cancelAFK(Player player) {
        if (player.hasMetadata("afk")) {
            player.sendMessage(ChatColor.GRAY + "你已取消暂离状态");
            //TODO 移除玩家afk状态
        }
    }
}