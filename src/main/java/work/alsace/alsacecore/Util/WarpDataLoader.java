package work.alsace.alsacecore.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import work.alsace.alsacecore.AlsaceCore;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class WarpDataLoader {

    private final Map<String, Location> warps = Collections.synchronizedMap(new HashMap<>());
    private final File warpsFile;
    private YamlConfiguration warpConfig;

    public WarpDataLoader(String name) {
        // 初始化时直接加载数据
        this.warpsFile = new File(AlsaceCore.instance.getDataFolder(), name + ".yml");
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
            }
        }
    }

    public void addWarp(String name, Location location) {
        synchronized (warps) {
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
                e.printStackTrace(); // 最好记录错误
            }
        }
    }

    public void delWarp(String name) {
        synchronized (warps) {
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
                e.printStackTrace();
            }
        }
    }

    public Location getWarp(String name) {
        synchronized (warps) {
            for (String i : warps.keySet()) {
                if (i.equalsIgnoreCase(name)) {
                    return warps.get(i);
                }
            }
        }
        return null;
    }

    public Set<String> getWarps() {
        return new HashSet<>(warps.keySet());
    }

    public Set<String> getWarpsWorld() {
        // 返回带world的warp
        return warps.keySet().stream()
                .collect(Collectors.toMap(warp -> warp + " " + warps.get(warp), warp -> warps.get(warp).getWorld()))
                .keySet();
    }
}
