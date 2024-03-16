package work.alsace.buildercore.listeners

import org.bukkit.Material
import org.bukkit.entity.Horse
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class HorseListener : Listener {
    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        if (player.inventory.itemInMainHand.type == Material.DEBUG_STICK && player.hasPermission("buildercore.protect.place") && player.isSneaking) {
            if (event.rightClicked is Horse) {
                val horse: Horse = event.rightClicked as Horse
                event.isCancelled = true
                val newColor = getNextHorseColor(horse.color)
                horse.color = newColor
            }
        } else if (player.inventory.itemInMainHand.type == Material.DEBUG_STICK && player.hasPermission("buildercore.protect.place")) {
            if (event.rightClicked is Horse) {
                val horse: Horse = event.rightClicked as Horse
                event.isCancelled = true
                val newStyle = getNextHorseStyle(horse.style)
                horse.style = newStyle
            }
        }
    }

    private fun getNextHorseColor(currentType: Horse.Color): Horse.Color {
        return when (currentType) {
            Horse.Color.BLACK -> Horse.Color.BROWN
            Horse.Color.BROWN -> Horse.Color.GRAY
            Horse.Color.GRAY -> Horse.Color.WHITE
            Horse.Color.WHITE -> Horse.Color.CHESTNUT
            Horse.Color.CHESTNUT -> Horse.Color.CREAMY
            Horse.Color.CREAMY -> Horse.Color.DARK_BROWN
            Horse.Color.DARK_BROWN -> Horse.Color.BLACK
        }
    }

    private fun getNextHorseStyle(currentType: Horse.Style): Horse.Style {
        return when (currentType) {
            Horse.Style.NONE -> Horse.Style.BLACK_DOTS
            Horse.Style.BLACK_DOTS -> Horse.Style.WHITE_DOTS
            Horse.Style.WHITE_DOTS -> Horse.Style.WHITE
            Horse.Style.WHITE -> Horse.Style.WHITEFIELD
            Horse.Style.WHITEFIELD -> Horse.Style.NONE
        }
    }
}
