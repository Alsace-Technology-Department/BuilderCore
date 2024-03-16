package work.alsace.buildercore

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import work.alsace.buildercore.commands.alias.*
import work.alsace.buildercore.commands.builderTools.*
import work.alsace.buildercore.commands.home.DelHomeCommand
import work.alsace.buildercore.commands.home.HomeCommand
import work.alsace.buildercore.commands.home.HomesCommand
import work.alsace.buildercore.commands.home.SetHomeCommand
import work.alsace.buildercore.commands.itemEdit.ItemColorCommand
import work.alsace.buildercore.commands.itemEdit.ItemNameCommand
import work.alsace.buildercore.commands.itemEdit.ItemNbtCommand
import work.alsace.buildercore.commands.players.*
import work.alsace.buildercore.commands.teleport.*
import work.alsace.buildercore.commands.time.PTimeCommand
import work.alsace.buildercore.commands.time.TimeCommand
import work.alsace.buildercore.commands.tools.BuilderCoreCommand
import work.alsace.buildercore.commands.tools.WhoisCommand
import work.alsace.buildercore.commands.warp.DelWarpCommand
import work.alsace.buildercore.commands.warp.SetWarpCommand
import work.alsace.buildercore.commands.warp.WarpCommand
import work.alsace.buildercore.commands.warp.WarpsCommand
import work.alsace.buildercore.commands.weather.PWeatherCommand
import work.alsace.buildercore.commands.weather.WeatherCommand
import work.alsace.buildercore.listeners.*
import work.alsace.buildercore.service.HomeDataLoader
import work.alsace.buildercore.service.TPAHandler
import work.alsace.buildercore.service.WarpDataLoader
import work.alsace.buildercore.utils.NoClipUtil
import work.alsace.buildercore.utils.Placeholder
import java.io.File
import java.util.*

class BuilderCore() : JavaPlugin() {
    val hasIgnored: MutableMap<String?, Boolean?> = HashMap()
    val homeProfiles = HashMap<UUID?, HomeDataLoader?>()
    val warpProfiles = HashMap<String?, WarpDataLoader?>()
    private lateinit var tPAHandler: TPAHandler
    var backHistory = 0
        private set
    var afkPrefix: String? = null
        private set
    var blockedCommands: List<String>? = null
        private set

    override fun onEnable() {
        loadConfig()
        this.tPAHandler = TPAHandler(this)
        registerCommands()
        registerListeners()
        Bukkit.getScheduler().runTaskTimer(this, Runnable { NoClipUtil().checkBlock() }, 1L, 1L)
        logger.info("插件已完全加载完成")
    }

    override fun onDisable() {
        homeProfiles.clear()
        warpProfiles.clear()
        logger.info("插件已卸载")
    }

    private fun registerCommands() {
        instance = this
        Objects.requireNonNull(getCommand("fly"))?.setExecutor(FlyCommand())
        Objects.requireNonNull(getCommand("speed"))?.setExecutor(SpeedCommand())
        Objects.requireNonNull(getCommand("speed"))?.tabCompleter = SpeedCommand()
        Objects.requireNonNull(getCommand("gamemode"))?.setExecutor(GameModeCommand())
        Objects.requireNonNull(getCommand("gamemode"))?.tabCompleter = GameModeCommand()
        Objects.requireNonNull(getCommand("time"))?.setExecutor(TimeCommand())
        Objects.requireNonNull(getCommand("time"))?.tabCompleter = TimeCommand()
        Objects.requireNonNull(getCommand("weather"))?.setExecutor(WeatherCommand())
        Objects.requireNonNull(getCommand("weather"))?.tabCompleter = WeatherCommand()
        Objects.requireNonNull(getCommand("ptime"))?.setExecutor(PTimeCommand())
        Objects.requireNonNull(getCommand("ptime"))?.tabCompleter = PTimeCommand()
        Objects.requireNonNull(getCommand("pweather"))?.setExecutor(PWeatherCommand())
        Objects.requireNonNull(getCommand("pweather"))?.tabCompleter = PWeatherCommand()
        Objects.requireNonNull(getCommand("head"))?.setExecutor(HeadCommand())
        Objects.requireNonNull(getCommand("night"))?.setExecutor(NightvisionCommand())
        Objects.requireNonNull(getCommand("advfly"))?.setExecutor(AdvanceFlyCommand())
        Objects.requireNonNull(getCommand("debugstick"))?.setExecutor(DebugStickCommand())
        Objects.requireNonNull(getCommand("slab"))?.setExecutor(SlabCommand())
        Objects.requireNonNull(getCommand("noclip"))?.setExecutor(NoClipCommand())
        Objects.requireNonNull(getCommand("/convex"))?.setExecutor(ConvexCommand())
        Objects.requireNonNull(getCommand("/derot"))?.setExecutor(DerotCommand())
        Objects.requireNonNull(getCommand("/cuboid"))?.setExecutor(CuboidCommand())
        Objects.requireNonNull(getCommand("/twist"))?.setExecutor(TwistCommand())
        Objects.requireNonNull(getCommand("/scale"))?.setExecutor(ScaleCommand())
        Objects.requireNonNull(getCommand("undo"))?.setExecutor(UndoCommand())
        Objects.requireNonNull(getCommand("hat"))?.setExecutor(HatCommand())
        Objects.requireNonNull(getCommand("back"))?.setExecutor(BackCommand(this))
        Objects.requireNonNull(getCommand("afk"))?.setExecutor(AFKCommand(this))
        Objects.requireNonNull(getCommand("tp"))?.setExecutor(TPCommand(this))
        Objects.requireNonNull(getCommand("tphere"))?.setExecutor(TPHereCommand())
        Objects.requireNonNull(getCommand("tpignore"))?.setExecutor(TPIgnoreCommand(this))
        Objects.requireNonNull(getCommand("tpahere"))?.setExecutor(TPACommand(this))
        Objects.requireNonNull(getCommand("tpaccept"))?.setExecutor(TPACommand(this))
        Objects.requireNonNull(getCommand("tpdeny"))?.setExecutor(TPACommand(this))
        Objects.requireNonNull(getCommand("home"))?.setExecutor(HomeCommand(this))
        Objects.requireNonNull(getCommand("home"))?.tabCompleter = HomeCommand(this)
        Objects.requireNonNull(getCommand("homes"))?.setExecutor(HomesCommand(this))
        Objects.requireNonNull(getCommand("sethome"))?.setExecutor(SetHomeCommand(this))
        Objects.requireNonNull(getCommand("delhome"))?.setExecutor(DelHomeCommand(this))
        Objects.requireNonNull(getCommand("delhome"))?.tabCompleter = DelHomeCommand(this)
        Objects.requireNonNull(getCommand("warp"))?.setExecutor(WarpCommand(this))
        Objects.requireNonNull(getCommand("warp"))?.tabCompleter = WarpCommand(this)
        Objects.requireNonNull(getCommand("warps"))?.setExecutor(WarpsCommand(this))
        Objects.requireNonNull(getCommand("setwarp"))?.setExecutor(SetWarpCommand(this))
        Objects.requireNonNull(getCommand("delwarp"))?.setExecutor(DelWarpCommand(this))
        Objects.requireNonNull(getCommand("delwarp"))?.tabCompleter = DelWarpCommand(this)
        Objects.requireNonNull(getCommand("buildercore"))?.setExecutor(BuilderCoreCommand(this))
        Objects.requireNonNull(getCommand("buildercore"))?.tabCompleter = BuilderCoreCommand(this)
        Objects.requireNonNull(getCommand("whois"))?.setExecutor(WhoisCommand())
        Objects.requireNonNull(getCommand("itemname"))?.setExecutor(ItemNameCommand())
        Objects.requireNonNull(getCommand("itemnbt"))?.setExecutor(ItemNbtCommand())
        Objects.requireNonNull(getCommand("itemcolor"))?.setExecutor(ItemColorCommand())
        blockedCommands = mutableListOf("/plugins", "/?", "/help", "/bukkit")
        logger.info("指令注册完成")
    }

    private fun registerListeners() {
        instance = this
        server.pluginManager.registerEvents(Protect(), this)
        server.pluginManager.registerEvents(PlayerListener(this), this)
        server.pluginManager.registerEvents(BlockListener(), this)
        server.pluginManager.registerEvents(Misc(this), this)
        server.pluginManager.registerEvents(AdvanceFlyCommand(), this)
        server.pluginManager.registerEvents(SpawnListener(), this)
        server.pluginManager.registerEvents(CatListener(), this)
        server.pluginManager.registerEvents(HorseListener(), this)
        server.pluginManager.registerEvents(FoxListener(), this)
        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            Placeholder(this).register()
            server.pluginManager.registerEvents(AFKListener(this), this)
        } else {
            logger.warning("前置插件 PlaceholderAPI 不存在，Afk功能注册失败")
        }
        logger.info("事件注册完成")
    }

    fun loadConfig() {
        saveDefaultConfig()
        reloadConfig()
        val folder = File(dataFolder, "userdata")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        for (i in Bukkit.getServer().onlinePlayers) {
            homeProfiles[i.uniqueId] = HomeDataLoader(i.uniqueId, this)
        }
        warpProfiles["warps"] = WarpDataLoader(this)
        afkPrefix = Objects.requireNonNull(this.config.getConfigurationSection("afk"))?.getString("placeholder")
        backHistory = this.config.getInt("backHistory")
    }

    companion object {
        var instance: BuilderCore? = null
            private set
    }

    fun getTPAHandler(): TPAHandler {
        return tPAHandler
    }
}
