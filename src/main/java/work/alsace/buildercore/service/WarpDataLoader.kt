package work.alsace.buildercore.service

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import work.alsace.buildercore.BuilderCore
import java.io.File
import java.io.IOException
import java.util.*

class WarpDataLoader(plugin: BuilderCore) {
    private val warps = Collections.synchronizedMap(HashMap<String, Location>())
    private val aliases = Collections.synchronizedMap(HashMap<String, String>())
    private val warpsFile: File
    private var warpConfig: YamlConfiguration? = null

    init {
        warpsFile = File(plugin.dataFolder.toString() + File.separator + "warps.yml")
        loadWarps()
    }

    @Synchronized
    private fun loadWarps() {
        if (!warpsFile.exists()) {
            try {
                warpsFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        warpConfig = YamlConfiguration.loadConfiguration(warpsFile)
        if (warpConfig!!.getConfigurationSection("Warps") != null) {
            for (i in Objects.requireNonNull(warpConfig!!.getConfigurationSection("Warps"))?.getKeys(false)!!) {
                warps[i] = Location(
                    Objects.requireNonNull(warpConfig!!.getString("Warps.$i.World"))?.let { Bukkit.getWorld(it) },
                    warpConfig!!.getDouble("Warps.$i.X"),
                    warpConfig!!.getDouble("Warps.$i.Y"),
                    warpConfig!!.getDouble("Warps.$i.Z"),
                    warpConfig!!.getLong("Warps.$i.Yaw").toFloat(),
                    warpConfig!!.getLong("Warps.$i.Pitch").toFloat()
                )

                // 加载别名
                val alias = warpConfig!!.getString("Aliases.$i")
                if (alias != null) {
                    aliases[alias] = i
                }
            }
        }
    }

    fun addWarp(name: String, alias: String?, location: Location) {
        synchronized(warps) {
            warps[name] = location
            warpConfig!!["Warps.$name.World"] = Objects.requireNonNull(location.world)?.name
            warpConfig!!["Warps.$name.X"] = location.x
            warpConfig!!["Warps.$name.Y"] = location.y
            warpConfig!!["Warps.$name.Z"] = location.z
            warpConfig!!["Warps.$name.Yaw"] = location.yaw
            warpConfig!!["Warps.$name.Pitch"] = location.pitch
            if (!alias.isNullOrEmpty()) {
                warpConfig!!["Aliases.$alias"] = name
                aliases[alias] = name
            }
            try {
                warpConfig!!.save(warpsFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun delWarp(name: String) {
        synchronized(warps) {
            warps.remove(name)
            warpConfig!!["Warps.$name"] = null

            // 删除别名
            val alias = getWarpAlias(name)
            if (alias != null) {
                warpConfig!!["Aliases.$alias"] = null
                aliases.remove(alias)
            }
            try {
                warpConfig!!.save(warpsFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getWarp(nameOrAlias: String): Location? {
        synchronized(warps) {
            var location = warps[nameOrAlias]
            if (location == null) {
                val realName = aliases[nameOrAlias]
                if (realName != null) {
                    location = warps[realName]
                }
            }
            return location
        }
    }

    fun getWarps(): Set<String> {
        return HashSet(warps.keys)
    }

    fun getWarpWorld(name: String): World? {
        val location = getWarp(name)
        return location?.world
    }

    fun getWarpAlias(name: String): String? {
        return aliases[name]
    }

    fun getRealWarpName(alias: String): String {
        return aliases.getOrDefault(alias, alias)
    }
}
