package work.alsace.alsacecore.Util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import work.alsace.alsacecore.AlsaceCore;

import java.util.Arrays;

import static work.alsace.alsacecore.AlsaceCore.afkPrefix;

public class Placeholder extends PlaceholderExpansion {

    private final AlsaceCore plugin;

    public Placeholder(AlsaceCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "alsacecore";
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
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) { return null; }

        if (params.equalsIgnoreCase("afk")) {
            if (player.hasMetadata("afk")) {
                return afkPrefix.replace("&","§");
            } else {
                return "";
            }
        }

        return null;
    }
}
