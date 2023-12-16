package work.alsace.alsacecore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.broadcastMessage("§a欢迎新玩家 §f" + event.getPlayer().getName() + " §a加入§f阿尔萨斯§a服务器");
        }
    }
}
