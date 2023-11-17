package work.alsace.alsacecore.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.puddingkc.commands.puddingUtilities.AdvanceFlyCommand;
import com.puddingkc.events.BlockEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import work.alsace.alsacecore.AlsaceCore;
import work.alsace.alsacecore.Util.HomeDataLoader;
import work.alsace.alsacecore.Util.NoClipUtil;

public class PlayerListener implements Listener {
    private final AlsaceCore plugin;

    public PlayerListener(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        BlockEvent.slabs.add(event.getPlayer());
        plugin.homeProfiles.put(event.getPlayer().getUniqueId(), new HomeDataLoader(event.getPlayer().getUniqueId()));
        this.plugin.hasIgnored.put(event.getPlayer().getName(), false);
        Player player = event.getPlayer();
        player.sendMessage(AlsaceCore.instance.motd);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        this.plugin.hasIgnored.remove(event.getPlayer().getName());
        plugin.homeProfiles.remove(event.getPlayer().getUniqueId());
        if (NoClipUtil.noclip.contains(event.getPlayer()) || AdvanceFlyCommand.enabledPlayers.contains(event.getPlayer()) || BlockEvent.slabs.contains(event.getPlayer())) {
            NoClipUtil.noclip.remove(event.getPlayer());
            AdvanceFlyCommand.enabledPlayers.remove(event.getPlayer());
            BlockEvent.slabs.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        plugin.homeProfiles.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        msg = this.translateHexColorCodes(msg);
        event.setMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    @EventHandler
    public void playerChangeSign(SignChangeEvent event) {
        boolean cnt = false;
    }

    @EventHandler
    public void playerAnvil(PrepareAnvilEvent event) {
        ItemStack item = event.getResult();
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String name = meta.getDisplayName();
                name = ChatColor.translateAlternateColorCodes('&', name);
                meta.setDisplayName(this.translateHexColorCodes(name));
                item.setItemMeta(meta);
                event.setResult(item);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(TeleportCause.SPECTATE) && !event.getPlayer().hasPermission("alsace.event.spectate")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    private String translateHexColorCodes(String message) {
        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 32);

        while (matcher.find()) {
            String group = matcher.group(1);
            char var10002 = group.charAt(0);
            matcher.appendReplacement(buffer, "§x§" + var10002 + "§" + group.charAt(1) + "§" + group.charAt(2) + "§" + group.charAt(3) + "§" + group.charAt(4) + "§" + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }
}
