package work.alsace.buildercore.listeners

import org.bukkit.Material
import org.bukkit.entity.Fox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class FoxListener : Listener {
    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        if (player.inventory.itemInMainHand.type == Material.STICK && player.hasPermission("buildercore.protect.place") && player.isSneaking) {
            if (event.rightClicked is Fox) {
                val fox: Fox = event.rightClicked as Fox
                event.isCancelled = true
                when (fox.foxType) {
                    Fox.Type.SNOW -> fox.foxType = Fox.Type.RED
                    Fox.Type.RED -> fox.foxType = Fox.Type.SNOW
                }
            }
        } else if (player.inventory.itemInMainHand.type == Material.STICK && player.hasPermission("buildercore.protect.place")) {
            event.isCancelled = true
            if (event.rightClicked is Fox) {
                val fox: Fox = event.rightClicked as Fox
                if (fox.isSitting) {
                    fox.isSitting = false
                    fox.isSleeping = true
                } else if (fox.isSleeping) {
                    fox.isSitting = false
                    fox.isSleeping = false
                } else {
                    fox.isSitting = true
                    fox.isSleeping = false
                }
            }
        }
    }
}
