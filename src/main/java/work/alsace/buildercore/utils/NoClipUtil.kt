package work.alsace.buildercore.utils

import org.bukkit.GameMode
import org.bukkit.entity.Player

class NoClipUtil {
    fun checkBlock() {
        val var1: Iterator<Player> = noclip.iterator()
        while (true) {
            var id: Player
            do {
                do {
                    if (!var1.hasNext()) {
                        return
                    }
                    id = var1.next()
                } while (false)
            } while (!id.isOnline)
            var noClip: Boolean
            if (id.gameMode == GameMode.CREATIVE) {
                noClip = if (id.location.add(0.0, -0.1, 0.0).block.type.isSolid && id.isSneaking) {
                    true
                } else {
                    shouldNoClip(id)
                }
                if (noClip) {
                    id.gameMode = GameMode.SPECTATOR
                }
            } else if (id.gameMode == GameMode.SPECTATOR) {
                noClip = if (id.location.add(0.0, -0.1, 0.0).block.type.isSolid) {
                    true
                } else {
                    shouldNoClip(id)
                }
                if (!noClip) {
                    id.gameMode = GameMode.CREATIVE
                }
            }
        }
    }

    private fun shouldNoClip(player: Player?): Boolean {
        return player!!.location.add(0.4, 0.0, 0.0).block.type.isSolid || player.location.add(
            -0.4,
            0.0,
            0.0
        ).block.type.isSolid || player.location.add(0.0, 0.0, 0.4).block.type.isSolid || player.location.add(
            0.0,
            0.0,
            -0.4
        ).block.type.isSolid || player.location.add(0.4, 1.0, 0.0).block.type.isSolid || player.location.add(
            -0.4,
            1.0,
            0.0
        ).block.type.isSolid || player.location.add(0.0, 1.0, 0.4).block.type.isSolid || player.location.add(
            0.0,
            1.0,
            -0.4
        ).block.type.isSolid || player.location.add(0.0, 1.9, 0.0).block.type.isSolid
    }

    companion object {
        var noclip: MutableSet<Player> = HashSet()
    }
}
