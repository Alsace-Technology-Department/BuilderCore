package work.alsace.buildercore.listeners

import org.bukkit.Location
import org.bukkit.entity.Ageable
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import kotlin.math.atan2

class SpawnListener : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.player.isSneaking) {
            if (event.item != null && event.item!!.type.toString().endsWith("SPAWN_EGG") &&
                event.player.hasPermission("buildercore.protect.place")
            ) {
                if (event.action == Action.LEFT_CLICK_BLOCK) {
                    event.isCancelled = true
                    spawnBaby(event)
                } else if (event.action == Action.RIGHT_CLICK_BLOCK) {
                    event.isCancelled = true
                    spawnDefault(event)
                }
            }
        }
    }

    private fun spawnBaby(event: PlayerInteractEvent) {
        val entityType: EntityType = if (Objects.requireNonNull(event.item)?.type.toString().contains("MOOSHROOM")) {
            EntityType.MUSHROOM_COW
        } else {
            EntityType.valueOf(event.item!!.type.toString().replace("_SPAWN_EGG", ""))
        }
        val location = Objects.requireNonNull(event.clickedBlock)?.location
        val entity = location?.let {
            event.player.world.spawnEntity(
                it.add(0.0, 1.0, 0.0), entityType
            )
        } as LivingEntity
        (entity as? Ageable)?.setBaby()
        entity.setAI(false)
        //修正面向角度
        val deltaX = event.player.location.x - entity.location.x
        val deltaZ = event.player.location.z - entity.location.z
        val angle = atan2(deltaZ, deltaX)
        val newYaw = Math.toDegrees(angle) - 90
        entity.teleport(Location(event.player.world, location.x, location.y, location.z, newYaw.toFloat(), 0f))
    }

    private fun spawnDefault(event: PlayerInteractEvent) {
        val entityType: EntityType = if (Objects.requireNonNull(event.item)?.type.toString().contains("MOOSHROOM")) {
            EntityType.MUSHROOM_COW
        } else {
            EntityType.valueOf(event.item!!.type.toString().replace("_SPAWN_EGG", ""))
        }
        val location = Objects.requireNonNull(event.clickedBlock)?.location
        val entity = location?.let {
            event.player.world.spawnEntity(
                it.add(0.0, 1.0, 0.0), entityType
            )
        } as LivingEntity
        entity.setAI(false)
        //修正面向角度
        val deltaX = event.player.location.x - entity.location.x
        val deltaZ = event.player.location.z - entity.location.z
        val angle = atan2(deltaZ, deltaX)
        val newYaw = Math.toDegrees(angle) - 90
        entity.teleport(Location(event.player.world, location.x, location.y, location.z, newYaw.toFloat(), 0f))
    }
}
