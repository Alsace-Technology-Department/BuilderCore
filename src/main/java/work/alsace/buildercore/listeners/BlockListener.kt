package work.alsace.buildercore.listeners

import org.bukkit.block.Block
import org.bukkit.block.data.Levelled
import org.bukkit.block.data.type.Slab
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import kotlin.math.abs

class BlockListener : Listener {
    @EventHandler
    fun onBlockPhysics(event: BlockFadeEvent) {
        if (event.block.type.name.contains("CORAL")) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerInteractLight(e: PlayerInteractEvent) {
        if (e.clickedBlock != null) {
            if (e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock!!.type.name.equals("light", ignoreCase = true)) {
                val block = e.clickedBlock
                val levelled = block!!.blockData as Levelled
                val level = levelled.level
                if (level in 0..14) {
                    levelled.level = level + 1
                } else if (level == 15) {
                    levelled.level = 0
                }
                e.isCancelled = true
                block.setBlockData(levelled, true)
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onBlockBreak(e: BlockBreakEvent) {
        if (!e.isCancelled && slabs.contains(e.player) && e.player.hasPermission("buildercore.commands.slab")) {
            val type = e.player.inventory.itemInMainHand.type
            if (type.toString().lowercase(Locale.getDefault()).contains("slab")) {
                if (e.isCancelled) {
                    return
                }
                if (e.block.type.toString().lowercase(Locale.getDefault()).contains("slab")) {
                    val blockData: Slab
                    if (isTop(e.player, e.block)) {
                        blockData = e.block.blockData as Slab
                        if (blockData.type == Slab.Type.DOUBLE) {
                            blockData.type = Slab.Type.BOTTOM
                            e.block.setBlockData(blockData, true)
                            e.isCancelled = true
                        }
                    } else {
                        blockData = e.block.blockData as Slab
                        if (blockData.type == Slab.Type.DOUBLE) {
                            blockData.type = Slab.Type.TOP
                            e.block.setBlockData(blockData, true)
                            e.isCancelled = true
                        }
                    }
                }
            }
        }
    }

    private fun isTop(player: Player, block: Block): Boolean {
        val start = player.eyeLocation.clone()
        while (start.block != block && start.distance(player.eyeLocation) < 6.0) {
            start.add(player.location.direction.multiply(0.05))
        }
        return if (start.y > 0) {
            start.y % 1.0 > 0.5
        } else {
            abs(start.y) % 1.0 < 0.5
        }
    }

    companion object {
        var slabs: MutableSet<Player> = HashSet()
    }
}
