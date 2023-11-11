package work.alsace.alsacecore.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import work.alsace.alsacecore.AlsaceCore;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpDataLoader {
    private static final HashMap<String, Location> warps = new HashMap<>();
    private final File warpsFile = new File(AlsaceCore.instance.getDataFolder() + "warps.yml");

    private final YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpsFile);

    public WarpDataLoader() {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(AlsaceCore.instance, new Runnable() {
            @Override
            public void run() {
                if (!warpsFile.exists()) {
                    try {
                        warpsFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (WarpDataLoader.this.warpConfig.getConfigurationSection("Warps") != null) {
                    for (String i : WarpDataLoader.this.warpConfig.getConfigurationSection("Warps").getKeys(false)) {
                        warps.put(i, new Location(
                                Bukkit.getServer().getWorld(Objects.requireNonNull(WarpDataLoader.this.warpConfig.getString("Warps." + i + ".world"))),
                                WarpDataLoader.this.warpConfig.getDouble("Warps." + i + ".X"),
                                WarpDataLoader.this.warpConfig.getDouble("Warps." + i + ".Y"),
                                WarpDataLoader.this.warpConfig.getDouble("Warps." + i + ".Z"),
                                WarpDataLoader.this.warpConfig.getLong("Warps." + i + ".Yaw"),
                                WarpDataLoader.this.warpConfig.getLong("Warps." + i + ".Pitch")));
                    }
                }
            }
        });
    }

    public void addWarp(String name, Location location) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(AlsaceCore.instance, new Runnable() {
            @Override
            public void run() {
                warps.put(name, location);
                warpConfig.set("Warps." + name + ".World", Objects.requireNonNull(location.getWorld()).getName());
                warpConfig.set("Warps." + name + ".X", location.getX());
                warpConfig.set("Warps." + name + ".Y", location.getY());
                warpConfig.set("Warps." + name + ".Z", location.getZ());
                warpConfig.set("Warps." + name + ".Yaw", location.getYaw());
                warpConfig.set("Warps." + name + ".Pitch", location.getPitch());
                try {
                    warpConfig.save(warpsFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void delWarp(String name) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(AlsaceCore.instance, new Runnable() {
            @Override
            public void run() {
                for (String i : warps.keySet()) {
                    if (i.equalsIgnoreCase(name)) {
                        warps.remove(i);
                        warpConfig.set("Warps." + i, null);
                        break;
                    }
                }
                try {
                    warpConfig.save(warpsFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public Location getWarp(String name) {
        for (String i : warps.keySet()) {
            if (i.equalsIgnoreCase(name)) {
                return warps.get(i);
            }
        }
        return null;
    }

    public Set<String> getWarps() {
        return warps.keySet();
    }
}