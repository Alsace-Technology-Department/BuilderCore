package work.alsace.buildercore.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class SpawnListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().isSneaking()) {
            if (event.getItem() != null && event.getItem().getType().toString().endsWith("SPAWN_EGG") &&
                    event.getPlayer().hasPermission("buildercore.protect.place")) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    spawnBaby(event);
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    spawnDefault(event);
                }
            }
        }
    }

    private void spawnBaby(PlayerInteractEvent event) {
        EntityType entityType;
        if (Objects.requireNonNull(event.getItem()).getType().toString().contains("MOOSHROOM")) {
            entityType = EntityType.MUSHROOM_COW;
        } else {
            entityType = EntityType.valueOf(event.getItem().getType().toString().replace("_SPAWN_EGG", ""));
        }
        Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        LivingEntity entity = (LivingEntity) event.getPlayer().getWorld().spawnEntity(
                location.add(0, 1, 0), entityType);
        if (entity instanceof Ageable ageable) {
            ageable.setBaby();
        }
        entity.setAI(false);
        //修正面向角度
        double deltaX = event.getPlayer().getLocation().getX() - entity.getLocation().getX();
        double deltaZ = event.getPlayer().getLocation().getZ() - entity.getLocation().getZ();
        double angle = Math.atan2(deltaZ, deltaX);
        double newYaw = Math.toDegrees(angle) - 90;
        entity.teleport(new Location(event.getPlayer().getWorld(), location.getX(), location.getY(), location.getZ(), (float) newYaw, 0));
    }

    private void spawnDefault(PlayerInteractEvent event) {
        EntityType entityType;
        if (Objects.requireNonNull(event.getItem()).getType().toString().contains("MOOSHROOM")) {
            entityType = EntityType.MUSHROOM_COW;
        } else {
            entityType = EntityType.valueOf(event.getItem().getType().toString().replace("_SPAWN_EGG", ""));
        }
        Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        LivingEntity entity = (LivingEntity) event.getPlayer().getWorld().spawnEntity(
                location.add(0, 1, 0), entityType);
        entity.setAI(false);
        //修正面向角度
        double deltaX = event.getPlayer().getLocation().getX() - entity.getLocation().getX();
        double deltaZ = event.getPlayer().getLocation().getZ() - entity.getLocation().getZ();
        double angle = Math.atan2(deltaZ, deltaX);
        double newYaw = Math.toDegrees(angle) - 90;
        entity.teleport(new Location(event.getPlayer().getWorld(),location.getX(),location.getY(),location.getZ(), (float) newYaw,0));
    }
}
