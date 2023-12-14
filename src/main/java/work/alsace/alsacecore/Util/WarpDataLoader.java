package work.alsace.alsacecore.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import work.alsace.alsacecore.AlsaceCore;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpDataLoader {
    private final Map<String, Location> warps = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, String> aliases = Collections.synchronizedMap(new HashMap<>());
    private final File warpsFile;
    private YamlConfiguration warpConfig;

    public WarpDataLoader(AlsaceCore plugin) {
        this.warpsFile = new File(plugin.getDataFolder() + File.separator + "warps.yml");
        this.loadWarps();
    }

    private synchronized void loadWarps() {
        if (!warpsFile.exists()) {
            try {
                warpsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        warpConfig = YamlConfiguration.loadConfiguration(warpsFile);

        if (warpConfig.getConfigurationSection("Warps") != null) {
            for (String i : warpConfig.getConfigurationSection("Warps").getKeys(false)) {
                warps.put(i, new Location(
                        Bukkit.getWorld(Objects.requireNonNull(warpConfig.getString("Warps." + i + ".World"))),
                        warpConfig.getDouble("Warps." + i + ".X"),
                        warpConfig.getDouble("Warps." + i + ".Y"),
                        warpConfig.getDouble("Warps." + i + ".Z"),
                        warpConfig.getLong("Warps." + i + ".Yaw"),
                        warpConfig.getLong("Warps." + i + ".Pitch")));

                // 加载别名
                String alias = warpConfig.getString("Aliases." + i);
                if (alias != null) {
                    aliases.put(alias, i);
                }
            }
        }
    }

    public void addWarp(String name, String alias, Location location) {
        synchronized (warps) {
            warps.put(name, location);
            warpConfig.set("Warps." + name + ".World", Objects.requireNonNull(location.getWorld()).getName());
            warpConfig.set("Warps." + name + ".X", location.getX());
            warpConfig.set("Warps." + name + ".Y", location.getY());
            warpConfig.set("Warps." + name + ".Z", location.getZ());
            warpConfig.set("Warps." + name + ".Yaw", location.getYaw());
            warpConfig.set("Warps." + name + ".Pitch", location.getPitch());

            if (alias != null && !alias.isEmpty()) {
                warpConfig.set("Aliases." + alias, name);
                aliases.put(alias, name);
            }

            try {
                warpConfig.save(warpsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delWarp(String name) {
        synchronized (warps) {
            warps.remove(name);
            warpConfig.set("Warps." + name, null);

            // 删除别名
            String alias = getWarpAlias(name);
            if (alias != null) {
                warpConfig.set("Aliases." + alias, null);
                aliases.remove(alias);
            }

            try {
                warpConfig.save(warpsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Location getWarp(String nameOrAlias) {
        synchronized (warps) {
            Location location = warps.get(nameOrAlias);
            if (location == null) {
                String realName = aliases.get(nameOrAlias);
                if (realName != null) {
                    location = warps.get(realName);
                }
            }
            return location;
        }
    }

    public Set<String> getWarps() {
        return new HashSet<>(warps.keySet());
    }

    public World getWarpWorld(String name) {
        Location location = getWarp(name);
        return (location != null) ? location.getWorld() : null;
    }

    public String getWarpAlias(String name) {
        return aliases.get(name);
    }

    public String getRealWarpName(String alias) {
        return aliases.getOrDefault(alias, alias);
    }
}
