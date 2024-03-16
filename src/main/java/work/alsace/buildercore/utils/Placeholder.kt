package work.alsace.buildercore.utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import work.alsace.buildercore.BuilderCore

class Placeholder(private val plugin: BuilderCore) : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "buildercore"
    }

    override fun getAuthor(): String {
        return plugin.description.authors.toTypedArray().contentToString()
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String? {
        return if (params.equals("afk", ignoreCase = true)) {
            if (player.hasMetadata("afk")) {
                plugin.afkPrefix?.replace("&", "§")
            } else {
                ""
            }
        } else null
    }
}
