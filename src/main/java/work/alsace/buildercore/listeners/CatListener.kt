package work.alsace.buildercore.listeners

import org.bukkit.Material
import org.bukkit.entity.Cat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class CatListener : Listener {
    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        if (player.inventory.itemInMainHand.type == Material.DEBUG_STICK && player.hasPermission("buildercore.protect.place") && player.isSneaking) {
            if (event.rightClicked is Cat) {
                val cat: Cat = event.rightClicked as Cat
                event.isCancelled = true
                val newType = getNextCatType(cat.catType)
                cat.catType = newType
            }
        } else if (player.inventory.itemInMainHand.type == Material.DEBUG_STICK && player.hasPermission("buildercore.protect.place")) {
            event.isCancelled = true
            if (event.rightClicked is Cat) {
                val cat: Cat = event.rightClicked as Cat
                cat.isSitting = !cat.isSitting
            }
        }
    }

    private fun getNextCatType(currentType: Cat.Type): Cat.Type {
        return when (currentType) {
            Cat.Type.BLACK -> Cat.Type.BRITISH_SHORTHAIR
            Cat.Type.BRITISH_SHORTHAIR -> Cat.Type.CALICO
            Cat.Type.CALICO -> Cat.Type.JELLIE
            Cat.Type.JELLIE -> Cat.Type.PERSIAN
            Cat.Type.PERSIAN -> Cat.Type.RAGDOLL
            Cat.Type.RAGDOLL -> Cat.Type.RED
            Cat.Type.RED -> Cat.Type.SIAMESE
            Cat.Type.SIAMESE -> Cat.Type.TABBY
            Cat.Type.TABBY -> Cat.Type.ALL_BLACK
            Cat.Type.ALL_BLACK -> Cat.Type.WHITE
            Cat.Type.WHITE -> Cat.Type.BLACK
        }
    }
}
