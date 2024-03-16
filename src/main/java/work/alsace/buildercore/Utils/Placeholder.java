package work.alsace.buildercore.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.buildercore.BuilderCore;

import java.util.Arrays;

public class Placeholder extends PlaceholderExpansion {

    private final BuilderCore plugin;

    public Placeholder(BuilderCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "buildercore";
    }

    @Override
    public @NotNull String getAuthor() {
        return Arrays.toString(plugin.getDescription().getAuthors().toArray());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        if (params.equalsIgnoreCase("afk")) {
            if (player.hasMetadata("afk")) {
                return plugin.getAfkPrefix().replace("&", "§");
            } else {
                return "";
            }
        }

        return null;
    }
}
