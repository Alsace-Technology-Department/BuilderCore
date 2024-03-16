package work.alsace.buildercore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class FoxListener implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.STICK && player.hasPermission("buildercore.protect.place") && player.isSneaking()) {
            if (event.getRightClicked() instanceof Fox fox) {
                event.setCancelled(true);
                switch (fox.getFoxType()) {
                    case SNOW -> fox.setFoxType(Fox.Type.RED);
                    case RED -> fox.setFoxType(Fox.Type.SNOW);
                }
            }
        } else if (player.getInventory().getItemInMainHand().getType() == Material.STICK && player.hasPermission("buildercore.protect.place")) {
            event.setCancelled(true);
            if (event.getRightClicked() instanceof Fox fox) {
                if (fox.isSitting()) {
                    fox.setSitting(false);
                    fox.setSleeping(true);
                } else if (fox.isSleeping()) {
                    fox.setSitting(false);
                    fox.setSleeping(false);
                } else {
                    fox.setSitting(true);
                    fox.setSleeping(false);
                }
            }
        }
    }
}
