package work.alsace.buildercore.service

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import work.alsace.buildercore.BuilderCore
import java.io.File
import java.io.IOException
import java.util.*

class HomeDataLoader(playerUUID: UUID, plugin: BuilderCore) {
    private val homes = HashMap<String, Location>()
    private var userFile: File =
        File(plugin.dataFolder.toString() + File.separator + "userdata" + File.separator + playerUUID.toString() + ".yml")
    private var userConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(userFile)
    private var maxHomes: Int = 5
    private val plugin: BuilderCore? = null

    init {
        Bukkit.getServer().scheduler.runTaskAsynchronously(plugin, Runnable {
            if (!userFile.exists()) {
                try {
                    userFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (userConfig.getConfigurationSection("Home") != null) {
                for (i in Objects.requireNonNull(userConfig.getConfigurationSection("Home"))?.getKeys(false)!!) {
                    homes[i] = Location(
                        Objects.requireNonNull(userConfig.getString("Home.$i.World"))
                            ?.let { Bukkit.getServer().getWorld(it) },
                        userConfig.getDouble("Home.$i.X"),
                        userConfig.getDouble("Home.$i.Y"),
                        userConfig.getDouble("Home.$i.Z"),
                        userConfig.getLong("Home.$i.Yaw").toFloat(),
                        userConfig.getLong("Home.$i.Pitch").toFloat()
                    )
                }
            }
        })
        val player = Bukkit.getServer().getPlayer(playerUUID)
        if (player != null) {
            for (i in player.effectivePermissions) {
                if (i.permission.equals("buildercore.home.max.unlimited", ignoreCase = true)) {
                    maxHomes = Int.MAX_VALUE
                    break
                }
                if (i.permission.startsWith("buildercore.home.max.")) {
                    val value = i.permission.replace("buildercore.home.max.", "").toInt()
                    if (value > maxHomes) {
                        maxHomes = value
                    }
                }
            }
        }
    }

    fun addHome(name: String, location: Location) {
        Bukkit.getServer().scheduler.runTaskAsynchronously(plugin!!, Runnable {
            homes[name] = location
            userConfig["Home.$name.World"] = Objects.requireNonNull(location.world)?.name
            userConfig["Home.$name.X"] = location.x
            userConfig["Home.$name.Y"] = location.y
            userConfig["Home.$name.Z"] = location.z
            userConfig["Home.$name.Yaw"] = location.yaw
            userConfig["Home.$name.Pitch"] = location.pitch
            try {
                userConfig.save(userFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun delHome(name: String?) {
        Bukkit.getServer().scheduler.runTaskAsynchronously(plugin!!, Runnable {
            for (i in homes.keys) {
                if (i.equals(name, ignoreCase = true)) {
                    homes.remove(i)
                    userConfig["Home.$i"] = null
                    break
                }
            }
            try {
                userConfig.save(userFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun getHome(name: String): Location? {
        for (i in homes.keys) {
            if (i.equals(name, ignoreCase = true)) {
                return homes[i]
            }
        }
        return null
    }

    fun getHomes(): Set<String> {
        return homes.keys
    }

    fun getMaxHomes(): Int {
        return maxHomes
    }
}
