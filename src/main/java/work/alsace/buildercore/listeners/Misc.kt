package work.alsace.buildercore.listeners

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import work.alsace.buildercore.BuilderCore
import java.util.*

class Misc(private val plugin: BuilderCore) : Listener {
    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        val command = event.message
        if (plugin.blockedCommands?.contains(command.lowercase(Locale.getDefault())) == true) {
            if (event.player.hasPermission("buildercore.blocked.bypass")) {
                return
            }
            event.isCancelled = true
            event.player.sendMessage(ChatColor.WHITE.toString() + "Unknown command. Type \"/help\" for help.")
        }
    }
}
